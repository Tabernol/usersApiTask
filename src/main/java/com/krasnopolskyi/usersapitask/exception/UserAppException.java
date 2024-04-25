package com.krasnopolskyi.usersapitask.exception;

public class UserAppException extends GlobalAppException{
    public UserAppException() {
    }

    public UserAppException(String message) {
        super(message);
    }

    public UserAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
