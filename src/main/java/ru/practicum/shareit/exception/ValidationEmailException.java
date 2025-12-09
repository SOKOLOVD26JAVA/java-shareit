package ru.practicum.shareit.exception;

public class ValidationEmailException extends RuntimeException {
    public ValidationEmailException(String message) {
        super(message);
    }
}
