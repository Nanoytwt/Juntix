package com.juntix.service;

import com.juntix.dao.IUsuarioDAO;
import com.juntix.dao.UsuarioDAOImpl;
import com.juntix.model.Usuario;
import com.juntix.util.PasswordUtil;
import com.juntix.exception.ServiceException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

/**
 * AuthServiceImpl
 * Implementación del servicio de autenticación. Maneja hashing y validaciones.
 */
public class AuthServiceImpl implements IAuthService {

    private final IUsuarioDAO usuarioDAO;

    public AuthServiceImpl() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @Override
    public Usuario registrar(String email, String password, String rol, String telefono) throws ServiceException {
        try {
            if (email == null || password == null || rol == null) throw new ServiceException("Datos incompletos");
            Optional<Usuario> existe = usuarioDAO.findByEmail(email);
            if (existe.isPresent()) throw new ServiceException("Email ya registrado");
            String hash = PasswordUtil.generarHash(password);
            Usuario.Role rolEnum = Usuario.Role.valueOf(rol.toUpperCase());
            Usuario u = new Usuario(email, hash, rolEnum, telefono);
            int id = usuarioDAO.create(u);
            u.setUsuarioId(id);
            return u;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new ServiceException("Error criptográfico", ex);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ServiceException("Error al registrar usuario", ex);
        }
    }

    @Override
    public Usuario iniciarSesion(String email, String password) throws ServiceException {
        try {
            Optional<Usuario> opt = usuarioDAO.findByEmail(email);
            if (!opt.isPresent()) throw new ServiceException("Email o contraseña incorrectos");
            Usuario u = opt.get();
            boolean ok = PasswordUtil.verificarPassword(password, u.getPasswordHash());
            if (!ok) throw new ServiceException("Email o contraseña incorrectos");
            return u;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new ServiceException("Error criptográfico en verificación", ex);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ServiceException("Error al iniciar sesión", ex);
        }
    }

    @Override
    public boolean cambiarPassword(int usuarioId, String currentPassword, String newPassword) throws ServiceException {
        try {
            Optional<Usuario> opt = usuarioDAO.findById(usuarioId);
            if (!opt.isPresent()) throw new ServiceException("Usuario no encontrado");
            Usuario u = opt.get();
            boolean ok = PasswordUtil.verificarPassword(currentPassword, u.getPasswordHash());
            if (!ok) throw new ServiceException("Contraseña actual incorrecta");
            String newHash = PasswordUtil.generarHash(newPassword);
            return usuarioDAO.updatePassword(usuarioId, newHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new ServiceException("Error criptográfico", ex);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ServiceException("Error al cambiar contraseña", ex);
        }
    }
}
