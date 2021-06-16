package by.zhigarev.exeption.impl;

import by.zhigarev.exeption.ServiceException;

public class NoSuchTagException extends ServiceException {
    public NoSuchTagException(String message, String errorCode) {
        super(message, errorCode);
    }

    public NoSuchTagException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public NoSuchTagException(Throwable cause, String errorCode) {
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

    public NoSuchTagException() {
    }

    public NoSuchTagException(String message) {
        super(message);
    }

    public NoSuchTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchTagException(Throwable cause) {
        super(cause);
    }
}
