package com.video.app.auth;

import com.video.app.dto.auth.AuthLoginDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.dto.auth.VerifyOTPDto;
import com.video.app.entities.Role;
import com.video.app.entities.User;
import com.video.app.exceptions.ServiceException;
import com.video.app.jwt.JwtService;
import com.video.app.mail.MailService;
import com.video.app.repositories.UserRepository;
import com.video.app.services.OTPService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public DataResponse login(AuthLoginDto authLoginDto) throws MessagingException {
        User user = findUser(authLoginDto.username());
        if (user == null) {
            return new DataResponse("User not found", null, false);
        }
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authLoginDto.password()
        ));
        if (!authentication.isAuthenticated()) {
            return new DataResponse("Password not valid", null, false);
        }
        Map<String, Object> data = new HashMap<>();
        String accessToken = this.jwtService.generate(user);
        if (!user.getIsTwoFactorAuthentication()) {
            data.put("user", user);
        } else {
            //use email for token to TFA
            User userForOTP = User.builder()
                    .username(user.getEmail())
                    .fullName("Unknown")
                    .email("Unknown")
                    .phone("Unknown")
                    .role(Role.USER)
                    .confirmed(false)
                    .isTwoFactorAuthentication(true)
                    .build();
            userForOTP.setCreatedAt(new Date());
            userForOTP.setUpdatedAt(new Date());
            userForOTP.setId(0L);
            accessToken = this.jwtService.generate(userForOTP);
            this.mailService.sendMailOTP(user);
            data.put("user", userForOTP);
        }
        data.put("token", accessToken);
        data.put("TFA", user.getIsTwoFactorAuthentication());
        return new DataResponse(user.getIsTwoFactorAuthentication()?"Check your OTP in email":"Login successfully!", data, true);
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
        this.userRepository.save(user);
        try {
            mailService.sendMailThankForRegister(user);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
        Map<String, Object> data = new HashMap<>();
        String accessToken = this.jwtService.generate(user);
        data.put("user", user);
        data.put("token", accessToken);
        return new DataResponse(verifyOTP.message(), data, true);
    }

    @Override
    public DataResponse sendOTP(String token) {
        if (!this.jwtService.isTokenValid(token))
            return new DataResponse("Token is expired! Please login with password again!", null, false);
        String email = this.jwtService.extractUsername(token);
        User user = this.userRepository.findByPhone(email);
        try {
            this.mailService.sendMailOTP(user);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new DataResponse("OTP is sent!", null, true);
    }
}
