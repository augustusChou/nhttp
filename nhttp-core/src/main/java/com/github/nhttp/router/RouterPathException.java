package com.github.nhttp.router;

public class RouterPathException extends RuntimeException {

    public RouterPathException() {
    }

    public RouterPathException(String message) {
        super(message);
    }

    public RouterPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterPathException(Throwable cause) {
        super(cause);
    }

    public RouterPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
