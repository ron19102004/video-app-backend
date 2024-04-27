package com.video.app.services;

import com.video.app.entities.User;
import com.video.app.utils.DataResponse;

public interface OTPService {
    String generate(User user);
    DataResponse isOTPValid(String otp, User user);
}
