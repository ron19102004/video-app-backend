package com.video.app.dto.country;

import jakarta.validation.constraints.NotNull;

public record UpdateCountryDto(
        @NotNull
        String name
) {
}