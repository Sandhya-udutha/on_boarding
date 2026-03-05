package ug.daes.onboarding.exceptions;

public class TotpDataException extends RuntimeException {
    public TotpDataException(String message) {
        super(message);
    }

    public TotpDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
