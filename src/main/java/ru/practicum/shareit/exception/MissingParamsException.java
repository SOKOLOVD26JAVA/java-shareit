package ru.practicum.shareit.exception;

public class MissingParamsException extends RuntimeException {
    public MissingParamsException(String message) {
        super(message);
    }
}
