package com.krasnopolskyi.usersapitask.exception;

public class ValidationException extends GlobalAppException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
