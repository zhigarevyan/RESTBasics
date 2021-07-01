package com.epam.esm.exeption.impl;

import com.epam.esm.exeption.ServiceException;

public class InvalidDataException extends ServiceException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, String errorCode) {
        super(message, errorCode);
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
