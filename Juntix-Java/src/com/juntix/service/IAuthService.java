package com.juntix.service;

import com.juntix.model.Usuario;

/**
 * IAuthService
 * Contrato de servicio para autenticación y registro.
 */
public interface IAuthService {
    Usuario registrar(String email, String password, String rol, String telefono) throws Exception;
    Usuario iniciarSesion(String email, String password) throws Exception;
    boolean cambiarPassword(int usuarioId, String currentPassword, String newPassword) throws Exception;
}
