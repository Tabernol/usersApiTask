package com.krasnopolskyi.usersapitask.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAppException extends GlobalAppException{

    private int exceptionStatus;
    public UserAppException() {
    }

    public UserAppException(String message) {
        super(message);
    }

    public UserAppException(String message, Throwable cause) {
        super(message, cause);
    }


}
