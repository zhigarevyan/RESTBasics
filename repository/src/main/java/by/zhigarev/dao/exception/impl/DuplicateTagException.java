package by.zhigarev.dao.exception.impl;

import by.zhigarev.dao.exception.DAOException;

public class DuplicateTagException extends DAOException {
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
