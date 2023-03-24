package com.erswebapp.backend.exception;

public class InvalidActionException extends RuntimeException {

    public InvalidActionException() {
        super();
    }

    public InvalidActionException(String message) {
        super(message);
    }

    public InvalidActionException(Throwable cause) {
        super(cause);
    }

    public InvalidActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
