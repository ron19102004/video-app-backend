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
        User user = findUser(authLoginDto.getUsername());
        if (user == null) {
            return new DataResponse("User not found");
        }
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authLoginDto.getPassword()
        ));
        if (!authentication.isAuthenticated()) {
            return new DataResponse("Password not valid");
        }
        String accessToken = this.jwtService.generate(user);
        Map<String, String> data = new HashMap<>();
        data.put("access-token", accessToken);
        return DataResponse.builder()
                .data(data)
                .message("Login successfully!")
                .status(true)
                .build();
    }

    @Override
    public DataResponse register(AuthRegisterDto authRegisterDto) {
        User userByEmail = this.userRepository.findByEmail(authRegisterDto.getEmail());
        if (userByEmail != null) {
            return new DataResponse("Email is exist!");
        }
        User userByPhone = this.userRepository.findByPhone(authRegisterDto.getPhone());
        if (userByPhone != null) {
            return new DataResponse("Phone is exist!");
        }
        User userByUsername = this.userRepository.findByUsername(authRegisterDto.getUsername()).orElse(null);
        if (userByUsername != null) {
            return new DataResponse("Username is exist!");
        }
        User user = User
                .builder()
                .username(authRegisterDto.getUsername())
                .email(authRegisterDto.getEmail())
                .confirmed(false)
                .role(Role.USER)
                .password(this.passwordEncoder.encode(authRegisterDto.getPassword()))
                .fullName(authRegisterDto.getFullName())
                .phone(authRegisterDto.getPhone())
                .build();
        this.userRepository.save(user);
        return new DataResponse("Sign up successfully!", true);
    }
}
