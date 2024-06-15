package com.video.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String PHONE_REGEX = "^(\\+[0-9]{1,3})?[0-9]{10,}$";
    public static final String AWS_REGEX = "^https:\\/\\/s3\\.ap-southeast-2\\.amazonaws\\.com\\/.*$\n";
    public static boolean isAwsURL(String url){
        Pattern pattern = Pattern.compile(AWS_REGEX);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {

        Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    public static boolean isPhone(String phone) {

        Pattern phonePattern = Pattern.compile(PHONE_REGEX);
        Matcher phoneMatcher = phonePattern.matcher(phone);
        return phoneMatcher.matches();
    }
}
