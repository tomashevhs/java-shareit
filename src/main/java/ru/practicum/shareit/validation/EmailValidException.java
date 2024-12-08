package ru.practicum.shareit.validation;

public class EmailValidException extends RuntimeException {
    public EmailValidException(String message) {
        super(message);
    }
}