package com.video.app.dto.auth;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record VerifyOTPDto(
        @NotNull
        @Length(min = 6, max = 6)
        String otp,
        @NotNull
        String token
) {
}
