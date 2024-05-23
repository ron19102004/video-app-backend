package com.video.app.dto.comment;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateCommentDto(
        @NotNull
        Long videoId,
        Long parentCommentId,
        @NotNull
        @Length(min = 10)
        String content
) {
}
