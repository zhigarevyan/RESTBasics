package by.zhigarev.exeption.impl;

import by.zhigarev.exeption.ServiceException;

public class NoSuchTagException extends ServiceException {

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
