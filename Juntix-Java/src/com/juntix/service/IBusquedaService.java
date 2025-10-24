package com.juntix.service;

import com.juntix.model.PerfilTrabajador;

import java.util.List;

/**
 * IBusquedaService
 * Contrato para búsquedas de perfiles por oficio.
 */
public interface IBusquedaService {
    List<PerfilTrabajador> buscarPorOficio(String termino, String localidad, int page, int size) throws Exception;
}
