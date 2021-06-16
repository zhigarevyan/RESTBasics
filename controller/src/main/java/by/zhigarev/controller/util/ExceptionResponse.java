package by.zhigarev.controller.util;

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
