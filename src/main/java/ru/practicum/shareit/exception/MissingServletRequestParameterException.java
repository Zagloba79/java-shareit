package ru.practicum.shareit.exception;

public class MissingServletRequestParameterException extends RuntimeException {
    public MissingServletRequestParameterException(String s) {
        super(s);
    }
}
