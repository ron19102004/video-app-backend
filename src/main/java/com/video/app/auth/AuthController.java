package com.video.app.auth;

import com.video.app.dto.auth.AuthLoginDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.dto.auth.VerifyOTPDto;
import com.video.app.exceptions.GlobalException;
import com.video.app.utils.DataResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController extends GlobalException {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse> login(@RequestBody AuthLoginDto authLoginDto) throws MessagingException {
        return ResponseEntity.ok(this.authService.login(authLoginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponse> register(@RequestBody AuthRegisterDto authRegisterDto) {
        return ResponseEntity.ok(this.authService.register(authRegisterDto));
    }

    @PostMapping("/change-TFA")
    public ResponseEntity<DataResponse> changeTFA() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse("User not authenticated", null, false));
        String username = authentication.getName();
        return ResponseEntity.ok(this.authService.turnOnOffTwoFactorAuthentication(username));
    }
    @PostMapping("/verify-OTP")
    public ResponseEntity<DataResponse> verifyOTP(@RequestBody @NotNull VerifyOTPDto verifyOTPDto){
        return ResponseEntity.ok(this.authService.verifyOTP(verifyOTPDto));
    }
    @PostMapping("/send-OTP")
    public ResponseEntity<DataResponse> sendOTP(@RequestParam("token") @NotNull String token){
        return ResponseEntity.ok(this.authService.sendOTP(token));
    }
}
