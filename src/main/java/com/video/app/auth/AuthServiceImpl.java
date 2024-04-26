package com.video.app.auth;

import com.video.app.dto.auth.AuthLoginDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.entities.Role;
import com.video.app.entities.User;
import com.video.app.jwt.JwtService;
import com.video.app.repositories.UserRepository;
import com.video.app.utils.DataResponse;
import com.video.app.utils.ValidationRegex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public DataResponse login(AuthLoginDto authLoginDto) {
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
        String accessToken = this.jwtService.generate(user);
        Map<String, Object> data = new HashMap<>();
        data.put("access-token", accessToken);
        data.put("user", user);
        return new DataResponse("Login successfully!", data, true);
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
                .build();
        this.userRepository.save(user);
        return new DataResponse("Sign up successfully!", null, true);
    }
}
