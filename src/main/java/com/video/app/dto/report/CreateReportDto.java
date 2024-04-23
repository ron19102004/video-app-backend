package com.video.app.dto.report;

import com.video.app.utils.ValidationRegex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportDto {
    @NotNull
    @Length(min = 15)
    @Pattern(regexp = ValidationRegex.EMAIL_REGEX)
    private String email;
    @NotNull
    private String content;
}
