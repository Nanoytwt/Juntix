package com.juntix.model;

/**
 * Oficio
 * Entidad que representa el catálogo de oficios.
 */
public class Oficio {
    private int oficioId;
    private String nombre;

    public Oficio() {}
    public Oficio(String nombre) { this.nombre = nombre; }

    public int getOficioId() { return oficioId; }
    public void setOficioId(int oficioId) { this.oficioId = oficioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
