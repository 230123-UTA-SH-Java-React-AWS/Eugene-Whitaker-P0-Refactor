package com.erswebapp.backend.exception;

public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException() {
        super();
    }

    public InvalidStatusException(String message) {
        super(message);
    }

    public InvalidStatusException(Throwable cause) {
        super(cause);
    }

    public InvalidStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
