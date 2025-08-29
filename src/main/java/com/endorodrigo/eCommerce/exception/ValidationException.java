package com.endorodrigo.eCommerce.exception;

/**
 * Excepción personalizada para errores de validación.
 */
public class ValidationException extends RuntimeException {
    
    private final String field;
    private final String errorCode;
    
    public ValidationException(String message) {
        super(message);
        this.field = null;
        this.errorCode = "VALIDATION_ERROR";
    }
    
    public ValidationException(String message, String field) {
        super(message);
        this.field = field;
        this.errorCode = "VALIDATION_ERROR";
    }
    
    public ValidationException(String message, String field, String errorCode) {
        super(message);
        this.field = field;
        this.errorCode = errorCode;
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
        this.errorCode = "VALIDATION_ERROR";
    }
    
    public String getField() {
        return field;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
