package com.video.app.dto.playlist;

import jakarta.validation.constraints.NotNull;

public record CreatePlaylistDto(
        @NotNull
        String name,
        @NotNull
        Boolean isPublic
) {
}
