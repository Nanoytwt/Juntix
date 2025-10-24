package com.juntix.model;

/**
 * PerfilTrabajador
 * Entidad que almacena el perfil público del trabajador (oficio, experiencia, etc.).
 */
public class PerfilTrabajador {
    private int perfilId;
    private int usuarioId;
    private int oficioId;
    private String nombreCompleto;
    private String experiencia;
    private String telefono;
    private String localidad;
    private boolean visible;
    private boolean activo = true; // para soft-delete

    public PerfilTrabajador() {}

    // getters y setters
    public int getPerfilId() { return perfilId; }
    public void setPerfilId(int perfilId) { this.perfilId = perfilId; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public int getOficioId() { return oficioId; }
    public void setOficioId(int oficioId) { this.oficioId = oficioId; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}