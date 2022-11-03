package com.devglan.rolebasedoauth2.exceptions;

public class DuplicateInfoFoundException extends RuntimeException{

    public DuplicateInfoFoundException() {
    }

    private static final String DUPLICATE_USER_NAME = "Dupicate Username found";

    public DuplicateInfoFoundException(String userName) {
        super(DUPLICATE_USER_NAME);
    }

    public DuplicateInfoFoundException(Throwable cause) {
        super(cause);
    }

    public DuplicateInfoFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateInfoFoundException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
