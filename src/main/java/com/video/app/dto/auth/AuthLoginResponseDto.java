package com.video.app.dto.auth;

import com.video.app.entities.User;
import com.video.app.entities.VIP;

public record AuthLoginResponseDto(
        User user,
        String token,
        boolean TFA,
        VIP vip
) {
}
