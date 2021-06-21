package com.epam.esm;

import com.epam.esm.util.ExceptionResponse;
import com.epam.esm.exeption.impl.DuplicateTagException;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;


@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String DUPLICATE_TAG = "duplicate_tag";
    private static final String NO_SUCH_TAG = "no_such_tag";
    private static final String NO_SUCH_GIFT = "no_such_gift";
    private static final String INVALID_DATA = "invalid_data";
    private final MessageSource messageSource;


    @Autowired
    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    @ExceptionHandler(DuplicateTagException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateTagException(DuplicateTagException e, Locale locale){
        String message = messageSource.getMessage(DUPLICATE_TAG, new Object[]{}, locale);
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, e.getErrorCode());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchTagException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchTagException(NoSuchTagException e, Locale locale){
        String message = messageSource.getMessage(NO_SUCH_TAG, new Object[]{}, locale);
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, e.getErrorCode());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchGiftException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchTagException(NoSuchGiftException e, Locale locale){
        String message = messageSource.getMessage(NO_SUCH_GIFT, new Object[]{}, locale);
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, e.getErrorCode());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchTagException(InvalidDataException e, Locale locale){
        String message = messageSource.getMessage(INVALID_DATA, new Object[]{}, locale);
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, e.getErrorCode());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

}
