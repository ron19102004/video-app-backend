package com.video.app.dto.auth;

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
public class AuthRegisterDto {
    @NotNull
    private String fullName;
    @NotNull
    @Length(min = 10, max = 11)
    @Pattern(regexp = ValidationRegex.PHONE_REGEX)
    private String phone;
    @NotNull
    @Length(min = 15)
    @Pattern(regexp = ValidationRegex.EMAIL_REGEX)
    private String email;
    @NotNull
    @Length(min = 8)
    private String password;
    @NotNull
    @Length(min = 5)
    private String username;
}
