package com.juntix.dao;

import com.juntix.model.PerfilTrabajador;
import com.juntix.util.DBConnection;
import com.juntix.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PerfilDAOImpl
 * Implementación JDBC de IPerfilDAO.
 */
public class PerfilDAOImpl implements IPerfilDAO {
    @Override
    public int upsert(PerfilTrabajador p) {
        try (Connection c = DBConnection.getConnection()) {
            // Si existe perfil para usuario -> UPDATE, si no -> INSERT
            Optional<PerfilTrabajador> existente = findByUsuarioId(p.getUsuarioId());
            if (existente.isPresent()) {
                String sql = "UPDATE PerfilTrabajador SET oficio_id=?, nombre_completo=?, experiencia=?, telefono=?, localidad=?, visible=?, activo=? WHERE usuario_id=?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, p.getOficioId());
                    ps.setString(2, p.getNombreCompleto());
                    ps.setString(3, p.getExperiencia());
                    ps.setString(4, p.getTelefono());
                    ps.setString(5, p.getLocalidad());
                    ps.setBoolean(6, p.isVisible());
                    ps.setBoolean(7, p.isActivo());
                    ps.setInt(8, p.getUsuarioId());
                    ps.executeUpdate();
                    return existente.get().getPerfilId();
                }
            } else {
                String sql = "INSERT INTO PerfilTrabajador(usuario_id, oficio_id, nombre_completo, experiencia, telefono, localidad, visible, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, p.getUsuarioId());
                    ps.setInt(2, p.getOficioId());
                    ps.setString(3, p.getNombreCompleto());
                    ps.setString(4, p.getExperiencia());
                    ps.setString(5, p.getTelefono());
                    ps.setString(6, p.getLocalidad());
                    ps.setBoolean(7, p.isVisible());
                    ps.setBoolean(8, p.isActivo());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int id = rs.getInt(1);
                            p.setPerfilId(id);
                            return id;
                        }
                    }
                }
            }
            return -1;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al upsert de perfil", ex);
        }
    }

    @Override
    public Optional<PerfilTrabajador> findByUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM PerfilTrabajador WHERE usuario_id = ? AND activo = TRUE";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PerfilTrabajador p = mapFromResultSet(rs);
                    return Optional.of(p);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar perfil por usuario", ex);
        }
    }

    @Override
    public Optional<PerfilTrabajador> findById(int perfilId) {
        String sql = "SELECT * FROM PerfilTrabajador WHERE perfil_id = ? AND activo = TRUE";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, perfilId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PerfilTrabajador p = mapFromResultSet(rs);
                    return Optional.of(p);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar perfil por id", ex);
        }
    }

    @Override
    public List<PerfilTrabajador> searchByOficio(int oficioId, String localidad, int limit, int offset) {
        List<PerfilTrabajador> lista = new ArrayList<>();
        String sql = "SELECT * FROM PerfilTrabajador WHERE oficio_id = ? AND visible = TRUE AND activo = TRUE"
                + (localidad != null && !localidad.isEmpty() ? " AND localidad = ?" : "")
                + " ORDER BY localidad, nombre_completo LIMIT ? OFFSET ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            int idx = 1;
            ps.setInt(idx++, oficioId);
            if (localidad != null && !localidad.isEmpty()) {
                ps.setString(idx++, localidad);
            }
            ps.setInt(idx++, limit);
            ps.setInt(idx, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapFromResultSet(rs));
                }
            }
            return lista;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar perfiles por oficio", ex);
        }
    }

    @Override
    public boolean softDeleteByUsuarioId(int usuarioId) {
        String sql = "UPDATE PerfilTrabajador SET activo = FALSE, visible = FALSE WHERE usuario_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al desactivar perfil", ex);
        }
    }

    private PerfilTrabajador mapFromResultSet(ResultSet rs) throws SQLException {
        PerfilTrabajador p = new PerfilTrabajador();
        p.setPerfilId(rs.getInt("perfil_id"));
        p.setUsuarioId(rs.getInt("usuario_id"));
        p.setOficioId(rs.getInt("oficio_id"));
        p.setNombreCompleto(rs.getString("nombre_completo"));
        p.setExperiencia(rs.getString("experiencia"));
        p.setTelefono(rs.getString("telefono"));
        p.setLocalidad(rs.getString("localidad"));
        p.setVisible(rs.getBoolean("visible"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}
