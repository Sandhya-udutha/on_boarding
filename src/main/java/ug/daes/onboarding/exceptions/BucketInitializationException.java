package ug.daes.onboarding.exceptions;

public class BucketInitializationException extends RuntimeException {

    public BucketInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BucketInitializationException(String message) {
        super(message);
    }
}
