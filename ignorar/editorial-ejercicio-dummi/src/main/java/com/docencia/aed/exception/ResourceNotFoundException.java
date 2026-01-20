package com.docencia.aed.exception;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, String string, Long id) {
        super(message);
    }
    
}
