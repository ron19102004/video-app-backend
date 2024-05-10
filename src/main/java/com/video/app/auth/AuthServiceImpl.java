package com.video.app.auth;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.video.app.dto.auth.AuthLoginRequestDto;
import com.video.app.dto.auth.AuthLoginResponseDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.dto.auth.VerifyOTPDto;
import com.video.app.entities.*;
import com.video.app.exceptions.ServiceException;
import com.video.app.jwt.JwtService;
import com.video.app.mail.MailService;
import com.video.app.repositories.PlaylistRepository;
import com.video.app.repositories.UserRepository;
import com.video.app.services.OTPService;
import com.video.app.services.VIPService;
import com.video.app.utils.DataResponse;
import com.video.app.utils.ValidationRegex;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private OTPService otpService;
    @PersistenceContext
    private EntityManager entityManager;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    @Autowired
    private VIPService vipService;
    @Autowired
    private PlaylistRepository playlistRepository;

    private User findUser(String string) {
        if (ValidationRegex.isEmail(string)) {
            return this.userRepository.findByEmail(string);
        }
        if (ValidationRegex.isPhone(string)) {
            return this.userRepository.findByPhone(string);
        }
        return this.userRepository.findByUsername(string).orElse(null);
    }

    @Override
    public DataResponse login(AuthLoginRequestDto authLoginRequestDto) throws MessagingException {
        User user = findUser(authLoginRequestDto.username());
        if (user == null) {
            return new DataResponse("User not found", null, false);
        }
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authLoginRequestDto.password()
        ));
        if (!authentication.isAuthenticated()) {
            return new DataResponse("Password not valid", null, false);
        }
        //check vip
        VIP vip = null;
        if (!user.getIsTwoFactorAuthentication()) {
            vip = this.vipService.findLatestVIPByUserId(user.getId());
            if (vip != null && this.vipService.isExpired(vip.getExpiredAt()) && vip.getActive()) {
                vip = this.vipService.cancel(vip);
            }
        }
        //create token
        String accessToken = this.jwtService.generate(user);
        User userResponse = user;
        if (user.getIsTwoFactorAuthentication()) {
            User userForOTP = User.builder()
                    .username(user.getEmail())
                    .fullName("Unknown")
                    .email("Unknown")
                    .phone("Unknown")
                    .role(Role.USER)
                    .confirmed(false)
                    .isTwoFactorAuthentication(true)
                    .build();
            userForOTP.setId(0L);
            accessToken = this.jwtService.generate(userForOTP);
            userResponse = userForOTP;
            executorService.submit(() -> {
                try {
                    this.mailService.sendMailOTP(user);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return new DataResponse(
                user.getIsTwoFactorAuthentication() ? "Check your OTP in email" : "Login successfully!",
                new AuthLoginResponseDto(userResponse, accessToken, user.getIsTwoFactorAuthentication(), vip),
                true
        );
    }

    @Override
    public DataResponse register(AuthRegisterDto authRegisterDto) {
        if (!ValidationRegex.isEmail(authRegisterDto.email())) {
            return new DataResponse("Email is invalid!", null, false);
        } else {
            User userByEmail = this.userRepository.findByEmail(authRegisterDto.email());
            if (userByEmail != null) {
                return new DataResponse("Email is exist!", null, false);
            }
        }
        if (!ValidationRegex.isPhone(authRegisterDto.phone())) {
            return new DataResponse("Phone is invalid!", null, false);
        } else {
            User userByPhone = this.userRepository.findByPhone(authRegisterDto.phone());
            if (userByPhone != null) {
                return new DataResponse("Phone is exist!", null, false);
            }
        }
        User userByUsername = this.userRepository.findByUsername(authRegisterDto.username()).orElse(null);
        if (userByUsername != null) {
            return new DataResponse("Username is exist!", null, false);
        }
        User user = User
                .builder()
                .username(authRegisterDto.username())
                .email(authRegisterDto.email())
                .confirmed(false)
                .role(Role.USER)
                .password(this.passwordEncoder.encode(authRegisterDto.password()))
                .fullName(authRegisterDto.fullName())
                .phone(authRegisterDto.phone())
                .isTwoFactorAuthentication(false)
                .build();
        User userSaved = this.userRepository.save(user);
        Playlist playlist = Playlist.builder()
                .name("Watch later")
                .user(userSaved)
                .privacy(Privacy.PRIVATE)
                .build();
        this.playlistRepository.save(playlist);
        executorService.submit(() -> {
            try {
                mailService.sendMailThankForRegister(user);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return new DataResponse("Sign up successfully!", null, true);
    }

    @Override
    public DataResponse turnOnOffTwoFactorAuthentication(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ServiceException("User not found"));
        boolean status = user.getIsTwoFactorAuthentication();
        user.setIsTwoFactorAuthentication(!status);
        this.entityManager.merge(user);
        return new DataResponse("Two-Factor-Authentication(TFA) is turning " +
                (user.getIsTwoFactorAuthentication() ? "on" : "off"), null, true);
    }

    @Override
    public DataResponse verifyOTP(VerifyOTPDto verifyOTPDto) {
        if (!this.jwtService.isTokenValid(verifyOTPDto.token()))
            return new DataResponse("Token is expired! Please login with password again!", null, false);
        String email = this.jwtService.extractUsername(verifyOTPDto.token());
        User user = this.userRepository.findByEmail(email);
        DataResponse verifyOTP = this.otpService.isOTPValid(verifyOTPDto.otp(), user);
        if (!verifyOTP.status()) return verifyOTP;
        VIP vip = this.vipService.findLatestVIPByUserId(user.getId());
        if (vip != null && vip.getActive() && this.vipService.isExpired(vip.getExpiredAt())) {
            vip = this.vipService.cancel(vip);
        }
        String accessToken = this.jwtService.generate(user);
        return new DataResponse(verifyOTP.message(),
                new AuthLoginResponseDto(user, accessToken, user.getIsTwoFactorAuthentication(), vip),
                true);
    }

    @Override
    public DataResponse sendOTP(String token) {
        if (!this.jwtService.isTokenValid(token))
            return new DataResponse("Token is expired! Please login with password again!", null, false);
        String email = this.jwtService.extractUsername(token);
        User user = this.userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User not found");
        executorService.submit(() -> {
            try {
                this.mailService.sendMailOTP(user);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        return new DataResponse("OTP is sent!", null, true);
    }
}
