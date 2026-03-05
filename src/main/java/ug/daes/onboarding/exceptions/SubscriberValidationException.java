package ug.daes.onboarding.exceptions;

public class SubscriberValidationException extends RuntimeException {

    public SubscriberValidationException() {
        super();
    }

    public SubscriberValidationException(String message) {
        super(message);
    }

    public SubscriberValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriberValidationException(Throwable cause) {
        super(cause);
    }
}
