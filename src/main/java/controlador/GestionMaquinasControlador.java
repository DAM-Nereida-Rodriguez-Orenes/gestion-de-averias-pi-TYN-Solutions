/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daoImpl.MaquinariaDAOimpl;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import config.DataSourceFactory;
import daoImpl.EstadoDAOimpl;
import daoImpl.TipoMaquinariaDAOimpl;
import modelo.Estado;
import modelo.TipoMaquinaria;
import java.util.List;
import java.util.Optional;
import modelo.Maquinaria;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionMaquinasControlador {
    //Instancias de los implements (la vista no sabe nada del DAO)
    private MaquinariaDAOimpl mDAOi = new MaquinariaDAOimpl(DataSourceFactory.getDataSource());
    private EstadoDAOimpl eDAO = new EstadoDAOimpl(DataSourceFactory.getDataSource());
    private TipoMaquinariaDAOimpl tDAO = new TipoMaquinariaDAOimpl(DataSourceFactory.getDataSource());

    public GestionMaquinasControlador() {}
    
    //crear nueva máquina
    public boolean crearMaquina(String nombre, int codigoEstadoFK, int tipoMaquinariaFK, Date fechaAlta) {
        LocalDate fechaAltaLDate;
        if (nombre == null || nombre.isBlank()) return false;
        if (codigoEstadoFK <= 0) return false;
        if (tipoMaquinariaFK <= 0) return false;
        if (fechaAlta == null) return false;
        //Fechas
        fechaAltaLDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(fechaAltaLDate.isAfter(LocalDate.now())){
            return false;
        }
        //objetos
        //Objetos
        Estado estado = new Estado();
        estado.setCodigoEstado(codigoEstadoFK);

        TipoMaquinaria tipo = new TipoMaquinaria();
        tipo.setCodigoTipoMaquinaria(tipoMaquinariaFK);
        
        //crear objeto modelo
        Maquinaria m = new Maquinaria();
        m.setNombre(nombre.trim());
        m.setTipoMaquinaria(tipo);
        m.setEstado(estado);
        m.setFechaAlta(fechaAltaLDate);

        //llamar a la daoimpl;
        mDAOi.insertar(m);
        //comunicarse con la vista
        return true;
    }
    
    //actualizar una máquina
     public boolean actualizarMaquina(int codigoMaquinaria, String nombre, int codigoEstadoFK, int tipoMaquinariaFK, Date fechaAltaUtil, Date fechaBajaUtil) {
        // Validaciones básicas
        if (codigoMaquinaria <= 0) return false;
        if (nombre == null || nombre.isBlank()) return false;
        if (codigoEstadoFK <= 0) return false;
        if (tipoMaquinariaFK <= 0) return false;
        if (fechaAltaUtil == null) return false;

        // Convertir Date (util) -> LocalDate
        LocalDate fechaAlta = new java.sql.Date(fechaAltaUtil.getTime()).toLocalDate();
        LocalDate fechaBaja = null;

        if (fechaBajaUtil != null) {
            fechaBaja = new java.sql.Date(fechaBajaUtil.getTime()).toLocalDate();
            if (fechaBaja.isBefore(fechaAlta)) return false; // regla: baja >= alta
        }
        //Objetos
        Estado estado = new Estado();
        estado.setCodigoEstado(codigoEstadoFK);

        TipoMaquinaria tipo = new TipoMaquinaria();
        tipo.setCodigoTipoMaquinaria(tipoMaquinariaFK);

        Maquinaria m = new Maquinaria();
        m.setCodigoMaquinaria(codigoMaquinaria);
        m.setNombre(nombre.trim());
        m.setEstado(estado);
        m.setTipoMaquinaria(tipo);
        m.setFechaAlta(fechaAlta);
        m.setFechaBaja(fechaBaja);
        //llamada a DAO implement
        mDAOi.modificar(m); 
        //comunicarse con la vista
        return true;
    }
    
    //eliminar una máquina
    public boolean eliminarMaquina(int id) {
        if (id <= 0) return false;

        //comprobar existencia
        if (mDAOi.buscarMaquinariaPorId(id).isEmpty()) {
            return false; // no existe
        }

        try {
            mDAOi.eliminar(id);
            return true;
        } catch (RuntimeException ex) {
            // Si hay incongruencias de FK u otros problemas, aquí cae
            return false;
        }
    }

    //listar desde BDD para rellenar comboboxes y tareas similares
    public List<Maquinaria> listarMaquinaria() {
        return mDAOi.listarMaquinaria();
    }
    public List<Estado> listarEstado() {
        return eDAO.listarEstado();
    }

    public List<TipoMaquinaria> listarTipoMaquinaria() {
        return tDAO.listarTipoMaquinaria();
    }
    
    //buscar por id (para actualizar y eliminar)
    public Optional<Maquinaria> buscarMaquinaPorID(int id){
        return mDAOi.buscarMaquinariaPorId(id);
    }
    
    /*FILTROS (la vista recoge los datos y se los pasa al controlador, que validará y mandará las cosas traducidas al DAO*/
    /*ID*/
    private void filtrarPorId(){
        //si el string es "", pues se pasa a null
        //si no, se parsea a int
        //si no es "" o int, pasar un false
    }
    /*nombre*/
    private void filtrarPorNombre(){}
    /*fechaAlta*/
    private void filtrarPorFechaAlta(){}
    /*fechaBaja*/
    private void filtrarPorFechaBaja(){}
    /*estado*/
    private void filtrarPorStatus(){}
    /*tipo*/
    private void filtrarPorTipo(){}
    /*llamar a la función de filtrar del DAO*/
    private void filtrar(){}
}
