package com.juntix.dao;

import com.juntix.model.PerfilTrabajador;
import java.util.List;
import java.util.Optional;

/**
 * IPerfilDAO
 * Contrato para las operaciones de persistencia sobre PerfilTrabajador.
 */
public interface IPerfilDAO {
    int upsert(PerfilTrabajador p) throws Exception;
    Optional<PerfilTrabajador> findByUsuarioId(int usuarioId) throws Exception;
    Optional<PerfilTrabajador> findById(int perfilId) throws Exception;
    List<PerfilTrabajador> searchByOficio(int oficioId, String localidad, int limit, int offset) throws Exception;
    boolean softDeleteByUsuarioId(int usuarioId) throws Exception;
}
