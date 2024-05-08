package com.video.app.dto.user;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ChangePasswordDto(
        @NotNull
        @Length(min = 8)
        String passwordCurrent,
        @NotNull
        @Length(min = 8)
        String passwordNew
) {
}
