package com.juntix.service;

import com.juntix.dao.IPerfilDAO;
import com.juntix.dao.PerfilDAOImpl;
import com.juntix.dao.IOficioDAO;
import com.juntix.dao.OficioDAOImpl;
import com.juntix.model.Oficio;
import com.juntix.model.PerfilTrabajador;
import com.juntix.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * BusquedaServiceImpl
 * Servicio que traduce término de búsqueda a oficio(s) y devuelve perfiles.
 */
public class BusquedaServiceImpl implements IBusquedaService {
    private final IOficioDAO oficioDAO;
    private final IPerfilDAO perfilDAO;

    public BusquedaServiceImpl() {
        this.oficioDAO = new OficioDAOImpl();
        this.perfilDAO = new PerfilDAOImpl();
    }

    @Override
    public List<PerfilTrabajador> buscarPorOficio(String termino, String localidad, int page, int size) throws ServiceException {
        try {
            // buscar oficios por termino (puede devolver varios)
            List<PerfilTrabajador> resultado = new ArrayList<>();
            List<Oficio> oficios = oficioDAO.searchByName(termino);
            for (Oficio o : oficios) {
                List<PerfilTrabajador> perfiles = perfilDAO.searchByOficio(o.getOficioId(), localidad, size, page * size);
                resultado.addAll(perfiles);
            }
            return resultado;
        } catch (Exception ex) {
            throw new ServiceException("Error en búsqueda por oficio", ex);
        }
    }
}
