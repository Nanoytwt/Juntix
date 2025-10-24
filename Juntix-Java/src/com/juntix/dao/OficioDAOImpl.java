package com.juntix.dao;

import com.juntix.model.Oficio;
import com.juntix.util.DBConnection;
import com.juntix.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OficioDAOImpl
 * Implementación JDBC del DAO de Oficios.
 */
public class OficioDAOImpl implements IOficioDAO {

    @Override
    public Optional<Oficio> findById(int id) {
        String sql = "SELECT oficio_id, nombre FROM Oficio WHERE oficio_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Oficio o = new Oficio();
                    o.setOficioId(rs.getInt("oficio_id"));
                    o.setNombre(rs.getString("nombre"));
                    return Optional.of(o);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar oficio por id", ex);
        }
    }

    @Override
    public Optional<Oficio> findByName(String name) {
        String sql = "SELECT oficio_id, nombre FROM Oficio WHERE nombre = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Oficio o = new Oficio();
                    o.setOficioId(rs.getInt("oficio_id"));
                    o.setNombre(rs.getString("nombre"));
                    return Optional.of(o);
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar oficio por nombre", ex);
        }
    }

    @Override
    public List<Oficio> searchByName(String term) {
        List<Oficio> lista = new ArrayList<>();
        String sql = "SELECT oficio_id, nombre FROM Oficio WHERE nombre LIKE CONCAT('%', ?, '%') ORDER BY nombre";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Oficio o = new Oficio();
                    o.setOficioId(rs.getInt("oficio_id"));
                    o.setNombre(rs.getString("nombre"));
                    lista.add(o);
                }
            }
            return lista;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al buscar oficios por término", ex);
        }
    }

    @Override
    public int create(Oficio oficio) {
        String sql = "INSERT INTO Oficio(nombre) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, oficio.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    oficio.setOficioId(id);
                    return id;
                }
            }
            return -1;
        } catch (SQLException ex) {
            throw new DataAccessException("Error al crear oficio", ex);
        }
    }
}
