package ug.daes.onboarding.exceptions;

public class InvalidDateValueException extends RuntimeException {
    public InvalidDateValueException(String message) {
        super(message);
    }

    public InvalidDateValueException(String message, Throwable cause) {
        super(message, cause);
    }
}