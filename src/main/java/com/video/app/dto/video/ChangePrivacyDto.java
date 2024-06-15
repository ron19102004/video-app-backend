package com.video.app.dto.video;

public record ChangePrivacyDto(
        Boolean isVip,
        Boolean isPublic
) {
}
