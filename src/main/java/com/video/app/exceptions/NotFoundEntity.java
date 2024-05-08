package com.video.app.exceptions;

public class NotFoundEntity extends RuntimeException {
    public NotFoundEntity(String message) {
        super(message);
    }
}
