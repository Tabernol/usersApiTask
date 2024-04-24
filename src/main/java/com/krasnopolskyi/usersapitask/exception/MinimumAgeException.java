package com.krasnopolskyi.usersapitask.exception;

public class MinimumAgeException extends GlobalAppException {
    public MinimumAgeException(String message) {
        super(message);
    }

    public MinimumAgeException(String message, Throwable cause) {
        super(message, cause);
    }
}
