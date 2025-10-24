package com.juntix.service;

import com.juntix.model.PerfilTrabajador;

import java.util.List;
import java.util.Optional;

/**
 * IPerfilService
 * Contrato para operaciones de perfil de trabajador.
 */
public interface IPerfilService {
    int guardarPerfil(PerfilTrabajador p) throws Exception;
    Optional<PerfilTrabajador> obtenerPerfilPorUsuario(int usuarioId) throws Exception;
    Optional<PerfilTrabajador> obtenerPerfilPorId(int perfilId) throws Exception;
    List<PerfilTrabajador> buscarPorOficio(int oficioId, String localidad, int page, int size) throws Exception;
    boolean desactivarPerfilPorUsuario(int usuarioId) throws Exception;
}
