package com.video.app.dto.user;

import jakarta.validation.constraints.NotNull;

public record ChangeInfoUserDto(
        @NotNull
        String fullName
) {
}
