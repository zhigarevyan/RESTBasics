package by.zhigarev.exeption.impl;

import by.zhigarev.exeption.ServiceException;

public class InvalidDataException extends ServiceException {
    public InvalidDataException() {
        super();
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, String errorCode) {
        super(message, errorCode);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(Throwable cause, String errorCode) {
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
