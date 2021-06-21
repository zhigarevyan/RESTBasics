package com.epam.esm.exeption.impl;

import com.epam.esm.exeption.ServiceException;

public class NoSuchGiftException extends ServiceException {

    public NoSuchGiftException() {
    }

    public NoSuchGiftException(String message) {
        super(message);
    }

    public NoSuchGiftException(String message, String errorCode) {
        super(message, errorCode);
    }

    public NoSuchGiftException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchGiftException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public NoSuchGiftException(Throwable cause) {
        super(cause);
    }

    public NoSuchGiftException(Throwable cause, String errorCode) {
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
}
