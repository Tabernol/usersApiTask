package com.krasnopolskyi.usersapitask.exception;

public class MinimumAgeException extends UserAppException {
    public MinimumAgeException(String message) {
        super(message);
    }

    public MinimumAgeException(String message, Throwable cause) {
        super(message, cause);
    }
}
