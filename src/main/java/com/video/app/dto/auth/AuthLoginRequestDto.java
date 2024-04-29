package com.video.app.dto.auth;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AuthLoginRequestDto(
        @NotNull
        @Length(min = 5)
        String username,
        @NotNull
        @Length(min = 8)
        String password
) {
}
