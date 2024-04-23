package com.video.app.exceptions;

import com.video.app.utils.DataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public abstract class GlobalException {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception exception) {
//        exception.printStackTrace();
//        return ResponseEntity.badRequest()
//                .body(new DataResponse(exception.getMessage()));
//    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handle(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new DataResponse("Password not true"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handle(AccessDeniedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new DataResponse(exception.getMessage()));
    }
}
