package com.erswebapp.backend.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException() {
        super();
    }

    public InvalidRoleException(String message) {
        super(message);
    }

    public InvalidRoleException(Throwable cause) {
        super(cause);
    }

    public InvalidRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
