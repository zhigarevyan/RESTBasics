package by.zhigarev.exeption.impl;

import by.zhigarev.exeption.ServiceException;

public class DuplicateTagException extends ServiceException {
    public DuplicateTagException() {
        super();
    }

    public DuplicateTagException(String message) {
        super(message);
    }

    public DuplicateTagException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateTagException(Throwable cause) {
        super(cause);
    }
}
