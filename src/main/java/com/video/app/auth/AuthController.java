package com.video.app.auth;

import com.video.app.dto.auth.AuthLoginDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.exceptions.GlobalException;
import com.video.app.utils.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController extends GlobalException {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse> login(@RequestBody AuthLoginDto authLoginDto) {
        return ResponseEntity.ok(this.authService.login(authLoginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponse> register(@RequestBody AuthRegisterDto authRegisterDto) {
        return ResponseEntity.ok(this.authService.register(authRegisterDto));
    }
}
