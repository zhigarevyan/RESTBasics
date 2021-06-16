package by.zhigarev.exeption.impl;

import by.zhigarev.exeption.ServiceException;

public class DuplicateTagException extends ServiceException {
    public DuplicateTagException() {
        super();
    }

    public DuplicateTagException(String message) {
        super(message);
    }

    public DuplicateTagException(String message, String errorCode) {
        super(message, errorCode);
    }

    public DuplicateTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateTagException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public DuplicateTagException(Throwable cause) {
        super(cause);
    }

    public DuplicateTagException(Throwable cause, String errorCode) {
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
