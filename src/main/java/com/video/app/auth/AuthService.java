package com.video.app.auth;

import com.video.app.dto.auth.AuthLoginDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.dto.auth.VerifyOTPDto;
import com.video.app.utils.DataResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    DataResponse login(AuthLoginDto authLoginDto) throws MessagingException;

    DataResponse register(AuthRegisterDto authRegisterDto);

    DataResponse turnOnOffTwoFactorAuthentication(String username);

    DataResponse verifyOTP(VerifyOTPDto verifyOTPDto);
    DataResponse sendOTP(String token);
}
