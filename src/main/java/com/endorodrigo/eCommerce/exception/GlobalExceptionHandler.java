package com.endorodrigo.eCommerce.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja excepciones de validación.
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(ValidationException ex, Model model) {
        logger.error("Error de validación: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        if (ex.getField() != null) {
            errors.put(ex.getField(), ex.getMessage());
        }
        
        model.addAttribute("errors", errors);
        model.addAttribute("errorMessage", ex.getMessage());
        
        return "error/validation-error";
    }

    /**
     * Maneja excepciones del POS.
     */
    @ExceptionHandler(PosException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlePosException(PosException ex, Model model) {
        logger.error("Error en POS: {} - Código: {}", ex.getMessage(), ex.getErrorCode());
        
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());
        model.addAttribute("timestamp", System.currentTimeMillis());
        
        return "error/pos-error";
    }

    /**
     * Maneja excepciones de facturación.
     */
    @ExceptionHandler(FacturaException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleFacturaException(FacturaException ex, Model model) {
        logger.error("Error de facturación: {} - Código: {}", ex.getMessage(), ex.getErrorCode());
        
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());
        model.addAttribute("timestamp", System.currentTimeMillis());
        
        return "error/factura-error";
    }

    /**
     * Maneja excepciones generales.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("Error inesperado: {}", ex.getMessage(), ex);
        
        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo.");
        model.addAttribute("timestamp", System.currentTimeMillis());
        
        return "error/generic-error";
    }

    /**
     * Maneja excepciones de validación en el POS con redirección.
     */
    @ExceptionHandler(ValidationException.class)
    public String handleValidationExceptionRedirect(ValidationException ex, RedirectAttributes redirectAttributes) {
        logger.error("Error de validación (redirect): {}", ex.getMessage());
        
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        if (ex.getField() != null) {
            redirectAttributes.addFlashAttribute("errorField", ex.getField());
        }
        
        return "redirect:/pos";
    }
}
