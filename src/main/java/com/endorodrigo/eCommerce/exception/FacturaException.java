package com.endorodrigo.eCommerce.exception;

/**
 * Excepción personalizada para errores relacionados con la facturación.
 */
public class FacturaException extends RuntimeException {
    
    private final String errorCode;
    
    public FacturaException(String message) {
        super(message);
        this.errorCode = "FACTURA_ERROR";
    }
    
    public FacturaException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public FacturaException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "FACTURA_ERROR";
    }
    
    public FacturaException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
