package com.video.app.utils;

import com.video.app.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Authentication authentication() throws ServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ServiceException("User not authenticated or missing token", HttpStatus.UNAUTHORIZED);
        }
        return authentication;
    }
}
