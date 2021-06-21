package com.epam.esm.util;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private String errorCode;

    public ExceptionResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
