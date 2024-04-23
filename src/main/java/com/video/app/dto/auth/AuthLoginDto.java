package com.video.app.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginDto {
    @NotNull
    @Length(min = 5)
    private String username;
    @NotNull
    @Length(min = 8)
    private String password;
}
