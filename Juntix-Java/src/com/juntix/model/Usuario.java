package com.juntix.model;

import java.time.LocalDateTime;

/**
 * Usuario
 * Representación de la entidad Usuario en el dominio.
 * Incluye atributos requeridos por el prototipo.
 */
public class Usuario {
    private int usuarioId;
    private String email;
    private String passwordHash;
    private String rol; // "CLIENTE" o "TRABAJADOR"
    private String telefono;
    private LocalDateTime fechaAlta;

    public Usuario() {}

    public Usuario(String email, String passwordHash, String rol, String telefono) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.telefono = telefono;
    }

    // getters y setters
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
}

