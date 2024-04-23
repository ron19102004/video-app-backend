package com.video.app.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DataResponse {
    private String message;
    private Object data;
    private boolean status;
    public DataResponse(String message){
        this.data = null;
        this.message = message;
        this.status = false;
    }
    public DataResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
        this.data = null;
    }
}
