package com.video.app.exceptions;

import com.video.app.utils.DataResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public abstract class GlobalException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new DataResponse(exception.getMessage(), null, false));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handle(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DataResponse("Password incorrect", null, false));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handle(AccessDeniedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new DataResponse(exception.getMessage(), null, false));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handle(ServiceException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DataResponse(exception.getMessage(), null, false));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> handle(MessagingException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new DataResponse(exception.getMessage(), null, false));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handle(MissingServletRequestParameterException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DataResponse(exception.getMessage(), null, false));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handle(UsernameNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DataResponse(exception.getMessage(), null, false));
    }
}
