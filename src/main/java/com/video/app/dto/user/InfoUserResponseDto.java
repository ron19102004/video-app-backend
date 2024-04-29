package com.video.app.dto.user;

import com.video.app.entities.VIP;

public record InfoUserResponseDto(
        VIP vip,
        Object user
) {
}
