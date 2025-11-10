package com.juntix.model;

import java.time.LocalDateTime;

/**
 * Usuario
 * Representa a un usuario del sistema (cliente o trabajador).
 */
public class Usuario {
    // Enumerado interno para roles
    public enum Role {
        CLIENTE,
        TRABAJADOR
    }
    private int usuarioId;
    private String email;
    private String passwordHash;
    private Role rol;
    private String telefono;
    private LocalDateTime fechaAlta;

    // Constructores
    public Usuario() {}

    public Usuario(String email, String passwordHash, Role rol, String telefono) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.telefono = telefono;
        this.fechaAlta = LocalDateTime.now();
    }

    // Getters y setters
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    // --- Métodos de utilidad ---
    public boolean esTrabajador() {
        return rol == Role.TRABAJADOR;
    }

    public boolean esCliente() {
        return rol == Role.CLIENTE;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + usuarioId + ", email='" + email + "', rol=" + rol + "}";
    }
}