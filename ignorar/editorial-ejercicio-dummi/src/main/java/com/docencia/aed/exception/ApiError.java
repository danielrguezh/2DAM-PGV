package com.docencia.aed.exception;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public class ApiError {
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
