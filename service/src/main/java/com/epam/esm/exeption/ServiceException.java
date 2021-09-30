package com.epam.esm.exeption;

public class ServiceException extends RuntimeException {
    private String errorCode;


    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
