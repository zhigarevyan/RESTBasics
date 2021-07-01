package com.epam.esm.exeption.impl;

import com.epam.esm.exeption.ServiceException;

public class NoSuchOrderException extends ServiceException {
    public NoSuchOrderException(String message, String errorCode) {
        super(message, errorCode);
    }

    public NoSuchOrderException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public NoSuchOrderException(Throwable cause, String errorCode) {
        super(cause, errorCode);
    }

    @Override
    public String getErrorCode() {
        return super.getErrorCode();
    }

    @Override
    public void setErrorCode(String errorCode) {
        super.setErrorCode(errorCode);
    }

    public NoSuchOrderException() {
    }

    public NoSuchOrderException(String message) {
        super(message);
    }

    public NoSuchOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchOrderException(Throwable cause) {
        super(cause);
    }
}
