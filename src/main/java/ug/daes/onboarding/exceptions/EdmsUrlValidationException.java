package ug.daes.onboarding.exceptions;


    public class EdmsUrlValidationException extends Exception {
        public EdmsUrlValidationException(String message) {
            super(message);
        }

        public EdmsUrlValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }