package com.juntix.dao;

import com.juntix.model.Usuario;
import java.util.Optional;

/**
 * IUsuarioDAO
 * Contrato para las operaciones CRUD sobre Usuario.
 */
public interface IUsuarioDAO {
    int create(Usuario u) throws Exception;
    Optional<Usuario> findByEmail(String email) throws Exception;
    Optional<Usuario> findById(int id) throws Exception;
    boolean updatePassword(int usuarioId, String newHash) throws Exception;
}
