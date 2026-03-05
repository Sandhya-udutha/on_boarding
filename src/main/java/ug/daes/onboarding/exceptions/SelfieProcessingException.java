package ug.daes.onboarding.exceptions;

public class SelfieProcessingException extends Exception {
    public SelfieProcessingException(String message) {
        super(message);
    }

    public SelfieProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
