package com.video.app.utils;

public record DataResponse(
        String message,
        Object data,
        boolean status
){}
