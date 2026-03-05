package ug.daes.onboarding.exceptions;
public class InvalidDateOfBirthException extends RuntimeException {

    public InvalidDateOfBirthException(String message) {
        super(message);
    }
    public InvalidDateOfBirthException(String message, Throwable cause) {
        super(message, cause);
    }
}
