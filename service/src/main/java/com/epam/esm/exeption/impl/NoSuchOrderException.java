package com.epam.esm.exeption.impl;

import com.epam.esm.exeption.ServiceException;

public class NoSuchOrderException extends ServiceException {

    @Override
    public String getErrorCode() {
        return super.getErrorCode();
    }

    @Override
    public void setErrorCode(String errorCode) {
        super.setErrorCode(errorCode);
    }


    public NoSuchOrderException(String message, Throwable cause) {
        super(message, cause);
    }

}
