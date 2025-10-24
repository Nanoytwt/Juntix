package com.juntix.dao;

import com.juntix.model.Oficio;
import java.util.List;
import java.util.Optional;

/**
 * IOficioDAO
 * Contrato para el acceso a datos del catálogo de oficios.
 */
public interface IOficioDAO {
    Optional<Oficio> findById(int id) throws Exception;
    Optional<Oficio> findByName(String name) throws Exception;
    List<Oficio> searchByName(String term) throws Exception;
    int create(Oficio oficio) throws Exception;
}
