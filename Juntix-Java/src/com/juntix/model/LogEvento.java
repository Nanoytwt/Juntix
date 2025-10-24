package com.juntix.model;

import java.time.LocalDateTime;

/**
 * LogEvento
 * Registro de auditoría para acciones importantes del sistema.
 */
public class LogEvento {
    private int logId;
    private Integer usuarioId; // puede ser null para acciones del sistema
    private String accion;
    private String detalle;
    private LocalDateTime fecha;

    // getters y setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
