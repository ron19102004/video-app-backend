package com.video.app.dto.auth;

import com.video.app.utils.ValidationRegex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AuthRegisterDto(
        @NotNull
        String fullName,
        @NotNull
        @Length(min = 10, max = 11)
        @Pattern(regexp = ValidationRegex.PHONE_REGEX)
        String phone,
        @NotNull
        @Length(min = 15)
        @Pattern(regexp = ValidationRegex.EMAIL_REGEX)
        String email,
        @NotNull
        @Length(min = 8)
        String password,
        @NotNull
        @Length(min = 5)
        String username
) {
}
