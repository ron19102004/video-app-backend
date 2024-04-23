package com.video.app.auth;

import com.video.app.dto.auth.AuthLoginDto;
import com.video.app.dto.auth.AuthRegisterDto;
import com.video.app.utils.DataResponse;

public interface AuthService {
    DataResponse login(AuthLoginDto authLoginDto);
    DataResponse register(AuthRegisterDto authRegisterDto);
}
