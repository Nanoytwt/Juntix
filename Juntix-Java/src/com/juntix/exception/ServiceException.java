package com.juntix.exception;

/**
 * ServiceException
 * Excepción runtime que envuelve errores en la capa de servicios.
 */
public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;
    public ServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    public ServiceException(String mensaje) {
        super(mensaje);
    }
}