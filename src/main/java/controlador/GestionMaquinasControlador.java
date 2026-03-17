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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionMaquinasControlador {

    //Instancias de los implements (la vista no sabe nada del DAO)
    private MaquinariaDAOimpl mDAOi = new MaquinariaDAOimpl(DataSourceFactory.getDataSource());
    private EstadoDAOimpl eDAO = new EstadoDAOimpl(DataSourceFactory.getDataSource());
    private TipoMaquinariaDAOimpl tDAO = new TipoMaquinariaDAOimpl(DataSourceFactory.getDataSource());

    public GestionMaquinasControlador() {
    }

    //crear nueva máquina
    public boolean crearMaquina(String nombre, int codigoEstadoFK, int tipoMaquinariaFK, Date fechaAlta) {
        LocalDate fechaAltaLDate;
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        if (codigoEstadoFK <= 0) {
            return false;
        }
        if (tipoMaquinariaFK <= 0) {
            return false;
        }
        if (fechaAlta == null) {
            return false;
        }
        //Fechas
        fechaAltaLDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (fechaAltaLDate.isAfter(LocalDate.now())) {
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
        if (codigoMaquinaria <= 0) {
            return false;
        }
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        if (codigoEstadoFK <= 0) {
            return false;
        }
        if (tipoMaquinariaFK <= 0) {
            return false;
        }
        if (fechaAltaUtil == null) {
            return false;
        }

        // Convertir Date (util) -> LocalDate
        LocalDate fechaAlta = new java.sql.Date(fechaAltaUtil.getTime()).toLocalDate();
        LocalDate fechaBaja = null;

        if (fechaBajaUtil != null) {
            fechaBaja = new java.sql.Date(fechaBajaUtil.getTime()).toLocalDate();
            if (fechaBaja.isBefore(fechaAlta)) {
                return false; // regla: baja >= alta
            }
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
    /*public boolean eliminarMaquina(int id) {
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
    }*/
    // dar de baja una maquina (baja logica)
    public boolean bajaLogicaMaquina(int id) {
        if (id <= 0) {
            return false;
        }

        // comprobar existencia
        Optional<Maquinaria> maquinaOpt = mDAOi.buscarMaquinariaPorId(id);
        if (maquinaOpt.isEmpty()) {
            return false; // no existe
        }

        Maquinaria maquina = maquinaOpt.get();

        // si ya esta dada de baja, no hacemos nada
        if (maquina.getFechaBaja() != null) {
            return false;
        }

        try {
            mDAOi.bajaLogica(id);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    // eliminar una maquina definitivamente (baja fisica)
    public boolean bajaFisicaMaquina(int id) {
        if (id <= 0) {
            return false;
        }

        // comprobar existencia
        Optional<Maquinaria> maquinaOpt = mDAOi.buscarMaquinariaPorId(id);
        if (maquinaOpt.isEmpty()) {
            return false; // no existe
        }

        Maquinaria maquina = maquinaOpt.get();

        // solo se puede eliminar si ya esta dada de baja
        if (maquina.getFechaBaja() == null) {
            return false;
        }

        try {
            mDAOi.bajaFisica(id);
            return true;
        } catch (RuntimeException ex) {
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
    public Optional<Maquinaria> buscarMaquinaPorID(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }

        return mDAOi.buscarMaquinariaPorId(id);
    }

    /*FILTROS (la vista recoge los datos y se los pasa al controlador, que validará y mandará las cosas traducidas al DAO*/
 /*ID con buscarMaquinaPorID*/
 /*nombre*/
    public List<Maquinaria> filtrarPorNombre(String nombre) {
        return mDAOi.buscarMaquinariaPorTexto(nombre);
    }

    /*Buscar por fechas*/
    public List<Maquinaria> filtrarPorFechas(Date fechaAltaUtil, boolean usarFechaAlta, Date fechaBajaUtil, boolean usarFechaBaja) {

        LocalDate fechaAlta = null;
        LocalDate fechaBaja = null;

        if (usarFechaAlta && fechaAltaUtil != null) {
            fechaAlta = fechaAltaUtil.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        if (usarFechaBaja && fechaBajaUtil != null) {
            fechaBaja = fechaBajaUtil.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        return mDAOi.buscarMaquinariaPorFecha(fechaAlta, fechaBaja);
    }

    /*estado*/
    public List<Maquinaria> filtrarPorStatus(Integer codigoEstadoFK) {
        return mDAOi.buscarMaquinariaPorEstado(codigoEstadoFK);
    }

    /*tipo*/
    public List<Maquinaria> filtrarPorTipo(Integer tipoMaquinariaFK) {
        return mDAOi.buscarMaquinariaPorTipo(tipoMaquinariaFK);
    }

    /*llamar a todas las funciones de filtrar*/
    public List<Maquinaria> filtrarMaquinaria(
            Integer id,
            String nombre,
            Date fechaAltaUtil, boolean usarFechaAlta,
            Date fechaBajaUtil, boolean usarFechaBaja,
            Integer codigoEstadoFK,
            Integer tipoMaquinariaFK) {

        List<List<Maquinaria>> listasActivas = new ArrayList<>();

        // Filtro por ID
        if (id != null && id > 0) {
            Optional<Maquinaria> maqOpt = buscarMaquinaPorID(id);
            List<Maquinaria> listaId = new ArrayList<>();
            maqOpt.ifPresent(listaId::add);
            listasActivas.add(listaId);
        }

        // Filtro por nombre
        if (nombre != null && !nombre.isBlank()) {
            listasActivas.add(filtrarPorNombre(nombre.trim()));
        }

        // Filtro por fechas
        if ((usarFechaAlta && fechaAltaUtil != null) || (usarFechaBaja && fechaBajaUtil != null)) {
            listasActivas.add(filtrarPorFechas(fechaAltaUtil, usarFechaAlta, fechaBajaUtil, usarFechaBaja));
        }

        // Filtro por estado
        if (codigoEstadoFK != null && codigoEstadoFK > 0) {
            listasActivas.add(filtrarPorStatus(codigoEstadoFK));
        }

        // Filtro por tipo
        if (tipoMaquinariaFK != null && tipoMaquinariaFK > 0) {
            listasActivas.add(filtrarPorTipo(tipoMaquinariaFK));
        }

        // Si no hay filtros, devolver todo
        if (listasActivas.isEmpty()) {
            return listarMaquinaria();
        }

        // Si hay filtros, hacer intersección
        return intersectarListasPorId(listasActivas);
    }

    //el filtro es un Y
    private List<Maquinaria> intersectarListasPorId(List<List<Maquinaria>> listas) {
        if (listas == null || listas.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Integer, Maquinaria> mapaResultado = new LinkedHashMap<>();
        for (Maquinaria m : listas.get(0)) {
            mapaResultado.put(m.getCodigoMaquinaria(), m);
        }

        for (int i = 1; i < listas.size(); i++) {
            Map<Integer, Maquinaria> mapaActual = new LinkedHashMap<>();

            for (Maquinaria m : listas.get(i)) {
                mapaActual.put(m.getCodigoMaquinaria(), m);
            }

            mapaResultado.keySet().retainAll(mapaActual.keySet());
        }

        return new ArrayList<>(mapaResultado.values());
    }
}
