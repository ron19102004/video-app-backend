package com.video.app.dto.report;

import com.video.app.utils.ValidationRegex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateReportDto(
        @NotNull
        @Length(min = 15)
        @Pattern(regexp = ValidationRegex.EMAIL_REGEX)
        String email,
        @NotNull
        String content
) {
}
