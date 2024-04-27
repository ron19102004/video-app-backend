package com.video.app.utils;

import java.util.concurrent.ThreadLocalRandom;

public class OTPGenerator {
    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
