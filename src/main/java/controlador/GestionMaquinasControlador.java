/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daoImpl.MaquinariaDAOimpl;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import modelo.Maquinaria;
import config.DataSourceFactory;
import daoImpl.EstadoDAOimpl;
import daoImpl.TipoMaquinariaDAOimpl;
import modelo.Estado;
import modelo.TipoMaquinaria;
import java.util.List;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionMaquinasControlador {
    //Instancias de los implements (la vista no sabe nada del DAO)
    private MaquinariaDAOimpl mDAOi = new MaquinariaDAOimpl(DataSourceFactory.getDataSource());
    EstadoDAOimpl eDAO = new EstadoDAOimpl(DataSourceFactory.getDataSource());
    TipoMaquinariaDAOimpl tDAO = new TipoMaquinariaDAOimpl(DataSourceFactory.getDataSource());

    public GestionMaquinasControlador() {}
    
    //crear nueva máquina
    public boolean crearMaquina(String nombre, int codigoEstadoFK, int tipoMaquinariaFK, Date fechaAlta) {
        LocalDate fechaAltaLDate;
        if (nombre == null || nombre.isBlank()) return false;
        if (codigoEstadoFK <= 0) return false;
        if (tipoMaquinariaFK <= 0) return false;
        if (fechaAlta == null) return false;
        fechaAltaLDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(fechaAltaLDate.isAfter(LocalDate.now())){
            return false;
        }
        
        //crear objeto modelo
        Maquinaria m = new Maquinaria();
        m.setNombre(nombre.trim());
        m.setCodigoEstadoFK(codigoEstadoFK);
        m.setTipoMaquinariaFK(tipoMaquinariaFK);
        m.setFechaAlta(fechaAltaLDate);

        //llamar a la daoimpl;
        mDAOi.insertar(m);
        //comunicarse con la vista
        return true;
    }
    
    //actualizar una máquina
     public boolean actualizarMaquina(String nombre, int codigoEstadoFK, int tipoMaquinariaFK, Date fechaAlta) {
        LocalDate fechaAltaLDate;
        if (nombre == null || nombre.isBlank()) return false;
        if (codigoEstadoFK <= 0) return false;
        if (tipoMaquinariaFK <= 0) return false;
        if (fechaAlta == null) return false;
        fechaAltaLDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(fechaAltaLDate.isAfter(LocalDate.now())){
            return false;
        }
        
        //crear objeto modelo
        Maquinaria m = new Maquinaria();
        m.setNombre(nombre.trim());
        m.setCodigoEstadoFK(codigoEstadoFK);
        m.setTipoMaquinariaFK(tipoMaquinariaFK);
        m.setFechaAlta(fechaAltaLDate);

        //llamar a la daoimpl;
        mDAOi.modificar(m);
        //comunicarse con la vista
        return true;
    }

    //listar desde BDD para rellenar comboboxes y tareas similares
    public List<Estado> listarEstado() {
        return eDAO.listarEstado();
    }

    public List<TipoMaquinaria> listarTipoMaquinaria() {
        return tDAO.listarTipoMaquinaria();
    }
}
