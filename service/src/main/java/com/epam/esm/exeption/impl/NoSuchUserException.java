package com.epam.esm.exeption.impl;

import com.epam.esm.exeption.ServiceException;

public class NoSuchUserException extends ServiceException {
    public NoSuchUserException(String message, String errorCode) {
        super(message, errorCode);
    }

    public NoSuchUserException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public NoSuchUserException(Throwable cause, String errorCode) {
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

    public NoSuchUserException() {
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }
}
