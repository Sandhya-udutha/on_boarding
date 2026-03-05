package ug.daes.onboarding.exceptions;

public class DeviceLookupException extends RuntimeException {
    public DeviceLookupException(String message) {
        super(message);
    }

    public DeviceLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}