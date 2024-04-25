package com.video.app.dto.country;

import jakarta.validation.constraints.NotNull;

public record CreateCountryDto(
        @NotNull
        String name
) {
}
