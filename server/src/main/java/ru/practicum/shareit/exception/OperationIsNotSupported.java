package ru.practicum.shareit.exception;

public class OperationIsNotSupported extends RuntimeException {
    public OperationIsNotSupported(String s) {
        super(s);
    }
}
