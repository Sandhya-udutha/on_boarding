package ug.daes.onboarding.exceptions;
public class DuplicateIdentificationException extends RuntimeException {
    private final String idType;

    public DuplicateIdentificationException(String idType, String message) {
        super(message);
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }
}