package com.juntix.dao;

import com.juntix.model.Usuario;
import com.juntix.util.DBConnection;
import com.juntix.exception.DataAccessException;

import java.sql.*;
import java.util.Optional;

/**
 * UsuarioDAOImpl
 * Implementación JDBC de IUsuarioDAO usando PreparedStatements.
 * Maneja errores envolviéndolos en DataAccessException.
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    @Override
    public int create(Usuario u) {
        String sql = "INSERT INTO Usuario(email, password_hash, rol, telefono) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getTelefono());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    u.setUsuarioId(id);
                    return id;
                }
            }
            return -1;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al crear usuario", ex);
        }
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        String sql = "SELECT usuario_id, email, password_hash, rol, telefono, fecha_alta FROM Usuario WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setUsuarioId(rs.getInt("usuario_id"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setRol(rs.getString("rol"));
                    u.setTelefono(rs.getString("telefono"));
                    return Optional.of(u);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar usuario por email", ex);
        }
    }

    @Override
    public Optional<Usuario> findById(int id) {
        String sql = "SELECT usuario_id, email, password_hash, rol, telefono, fecha_alta FROM Usuario WHERE usuario_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setUsuarioId(rs.getInt("usuario_id"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setRol(rs.getString("rol"));
                    u.setTelefono(rs.getString("telefono"));
                    return Optional.of(u);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar usuario por id", ex);
        }
    }

    @Override
    public boolean updatePassword(int usuarioId, String newHash) {
        String sql = "UPDATE Usuario SET password_hash = ? WHERE usuario_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, usuarioId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al actualizar contraseña", ex);
        }
    }
}
