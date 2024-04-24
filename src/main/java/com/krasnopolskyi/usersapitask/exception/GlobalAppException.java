package com.krasnopolskyi.usersapitask.exception;

public class GlobalAppException extends Exception{
    public GlobalAppException() {
    }

    public GlobalAppException(String message) {
        super(message);
    }

    public GlobalAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
