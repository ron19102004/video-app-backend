package com.video.app.dto.video;

import java.util.Date;

public record CreateInfoVideoDto(
        Long countryId,
        Long categoryId,
        String name,
        String description,
        String tag,
        Date release,
        Boolean isVip,
        Boolean isPublic
) {
}
