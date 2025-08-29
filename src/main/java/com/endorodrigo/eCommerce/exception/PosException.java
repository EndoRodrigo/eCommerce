package com.endorodrigo.eCommerce.exception;

/**
 * Excepción personalizada para errores relacionados con el punto de venta (POS).
 */
public class PosException extends RuntimeException {
    
    private final String errorCode;
    
    public PosException(String message) {
        super(message);
        this.errorCode = "POS_ERROR";
    }
    
    public PosException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public PosException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "POS_ERROR";
    }
    
    public PosException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
