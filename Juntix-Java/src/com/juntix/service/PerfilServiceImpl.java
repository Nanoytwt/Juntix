package com.juntix.service;

import com.juntix.dao.IPerfilDAO;
import com.juntix.dao.PerfilDAOImpl;
import com.juntix.model.PerfilTrabajador;
import com.juntix.exception.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * PerfilServiceImpl
 * Implementación del servicio de perfil que delega al DAO.
 */
public class PerfilServiceImpl implements IPerfilService {
    private final IPerfilDAO perfilDAO;

    public PerfilServiceImpl() {
        this.perfilDAO = new PerfilDAOImpl();
    }

    @Override
    public int guardarPerfil(PerfilTrabajador p) throws ServiceException {
        try {
            if (p.getNombreCompleto() == null || p.getTelefono() == null) 
                throw new ServiceException("Campos obligatorios faltantes");
            return perfilDAO.upsert(p);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ServiceException("Error al guardar perfil", ex);
        }
    }

    @Override
    public Optional<PerfilTrabajador> obtenerPerfilPorUsuario(int usuarioId) throws ServiceException {
        try {
            return perfilDAO.findByUsuarioId(usuarioId);
        } catch (Exception ex) {
            throw new ServiceException("Error al obtener perfil por usuario", ex);
        }
    }

    @Override
    public Optional<PerfilTrabajador> obtenerPerfilPorId(int perfilId) throws ServiceException {
        try {
            return perfilDAO.findById(perfilId);
        } catch (Exception ex) {
            throw new ServiceException("Error al obtener perfil por id", ex);
        }
    }

    @Override
    public List<PerfilTrabajador> buscarPorOficio(int oficioId, String localidad, int page, int size) throws ServiceException {
        try {
            int offset = page * size;
            return perfilDAO.searchByOficio(oficioId, localidad, size, offset);
        } catch (Exception ex) {
            throw new ServiceException("Error al buscar perfiles por oficio", ex);
        }
    }

    @Override
    public boolean desactivarPerfilPorUsuario(int usuarioId) throws ServiceException {
        try {
            return perfilDAO.softDeleteByUsuarioId(usuarioId);
        } catch (Exception ex) {
            throw new ServiceException("Error al desactivar perfil", ex);
        }
    }
}
