package com.video.app.dto.user;

import com.video.app.entities.User;

public record InfoConfirmedLoggedInDto(
        User user,
        Boolean isSubscribing
) {
}
