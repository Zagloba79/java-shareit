package ru.practicum.shareit.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String s) {
        super(s);
    }

    public static ValidationException createBadRequestException(String message) {
        return new ValidationException(message);
    }
}