package com.devglan.rolebasedoauth2.exceptions;

public class DatabaseException extends RuntimeException{

    public DatabaseException() {
    }

    private static final String DUPLICATE_USER_NAME = "Database Exception";

    public DatabaseException(String userName) {
        super(DUPLICATE_USER_NAME);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
