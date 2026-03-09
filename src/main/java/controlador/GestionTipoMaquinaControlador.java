/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.TipoMaquinariaDAO;
import daoImpl.TipoMaquinariaDAOimpl;
import java.util.List;
import modelo.TipoMaquinaria;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionTipoMaquinaControlador {
    private TipoMaquinariaDAOimpl tDAO = new TipoMaquinariaDAOimpl(DataSourceFactory.getDataSource());
    
     public List<TipoMaquinaria> listarTiposMaquinaria() {
        return tDAO.listarTipoMaquinaria();
    }

    public TipoMaquinaria obtenerTipoSeleccionado(int codigo, String descripcion) {
        TipoMaquinaria t = new TipoMaquinaria();
        t.setCodigoTipoMaquinaria(codigo);
        t.setDescripcionMaq(descripcion);
        return t;
    }
}
