package com.erswebapp.backend.exception;

public class InvalidReimbursmentTypeException extends RuntimeException {

    public InvalidReimbursmentTypeException() {
        super();
    }

    public InvalidReimbursmentTypeException(String message) {
        super(message);
    }

    public InvalidReimbursmentTypeException(Throwable cause) {
        super(cause);
    }

    public InvalidReimbursmentTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
