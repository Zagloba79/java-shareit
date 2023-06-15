package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ObjectNotFoundException(String s) {
        super(s);
    }
}
