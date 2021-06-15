package by.zhigarev.exeption.impl;

import by.zhigarev.exeption.ServiceException;

public class NoSuchGiftException extends ServiceException {

    public NoSuchGiftException() {
    }

    public NoSuchGiftException(String message) {
        super(message);
    }

    public NoSuchGiftException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchGiftException(Throwable cause) {
        super(cause);
    }
}
