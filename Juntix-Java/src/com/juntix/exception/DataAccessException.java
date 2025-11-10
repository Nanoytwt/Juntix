package com.juntix.exception;

/**
 * DataAccessException
 * Excepción runtime que envuelve errores de acceso a datos en la capa DAO.
 */
public class DataAccessException extends Exception {
    private static final long serialVersionUID = 1L;
    public DataAccessException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    public DataAccessException(String mensaje) {
        super(mensaje);
    }
}