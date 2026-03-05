/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

// Modelos
import modelo.Averia;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;

// DAOs
import daoImpl.AveriaDaoImpl;
import daoImpl.MaquinariaDAOimpl;
import daoImpl.TipoAveriaDaoImpl;
import daoImpl.UsuarioDaoImpl;
import config.DataSourceFactory;

/**
 * Controlador principal para la gestión de Averías.
 * Actúa como puente entre la Vista (Interfaces Java Swing) y el DAO (Base de Datos).
 * * @author yosnavmol
 */
public class AveriaControlador {
    
    private AveriaDaoImpl averiaDao;
    private MaquinariaDAOimpl maquinariaDao;
    private TipoAveriaDaoImpl tipoAveriaDao;
    private UsuarioDaoImpl usuarioDao;

    public AveriaControlador() {
        try {
            javax.sql.DataSource ds = DataSourceFactory.getDataSource();
            this.averiaDao = new AveriaDaoImpl(ds);
            this.maquinariaDao = new MaquinariaDAOimpl(ds);
            this.tipoAveriaDao = new TipoAveriaDaoImpl(ds);
            this.usuarioDao = new UsuarioDaoImpl(ds); 
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar los DAOs en AveriaControlador.");
        }
    }
    
    // =========================================================================
    // 1. MÉTODOS DE LECTURA Y TABLA PRINCIPAL
    // =========================================================================

    public Averia obtenerAveriaPorId(int idAveria) {
        try {
            List<Averia> listaResultados = averiaDao.buscarPorFiltros(idAveria, null, null, null, null, null, null, null); 
            if (listaResultados != null && !listaResultados.isEmpty()) {
                return listaResultados.get(0); 
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la avería por ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Trae todas las averías para mostrar al arrancar la ventana principal.
     */
    public List<Object[]> listarAveriasParaVista() {
        try {
            List<Averia> listaAverias = averiaDao.listar();
            return mapearAveriasParaTabla(listaAverias);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Trae las averías que coincidan con los filtros del JDialog.
     */
    public List<Object[]> obtenerAveriasFiltradas(Integer idAveria, String descripcion, 
                                                  LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                                                  Integer idUsuarioReporta, Integer idTecnico, 
                                                  Integer idMaquinaria, Integer idTipoAveria) {
        try {
            List<Averia> listaFiltrada = averiaDao.buscarPorFiltros(
                idAveria, descripcion, fechaInicio, fechaFin, 
                idUsuarioReporta, idTecnico, idMaquinaria, idTipoAveria
            );
            return mapearAveriasParaTabla(listaFiltrada);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * MÉTODO UNIFICADO: Convierte una lista de objetos Averia en una lista de arrays
     * listos para ser dibujados en el JTable, sustituyendo los IDs por nombres reales.
     */
    private List<Object[]> mapearAveriasParaTabla(List<Averia> listaAverias) {
        List<Object[]> filas = new ArrayList<>();
        if (listaAverias == null || listaAverias.isEmpty()) return filas;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            // 1. Descargamos catálogos para mapear nombres
            List<Maquinaria> listaMaquinas = maquinariaDao.listarMaquinaria();
            List<TipoAveria> listaTipos = tipoAveriaDao.listar();
            List<Usuario> listaUsuarios = usuarioDao.listarUsuarios();

            // 2. Creamos diccionarios para búsqueda ultrarrápida (O(1))
            Map<Integer, String> mapaMaquinas = new HashMap<>();
            for (Maquinaria m : listaMaquinas) mapaMaquinas.put(m.getCodigoMaquinaria(), m.getNombre());

            Map<Integer, String> mapaTipos = new HashMap<>();
            for (TipoAveria t : listaTipos) mapaTipos.put(t.getCodigoTipoAveria(), t.getDescripcionTipoAv());

            Map<Integer, String> mapaUsuarios = new HashMap<>();
            for (Usuario u : listaUsuarios) mapaUsuarios.put(u.getCodigoUsuario(), u.getNombre() + " " + u.getApellido());

            // 3. Construimos el array para cada fila
            for (Averia a : listaAverias) {
                Object[] fila = new Object[12]; 

                fila[0] = a.getCodigoAveria();
                fila[1] = a.getDescInicAveria();
                
                // Usamos getOrDefault para que, si el ID no existe, ponga el número por defecto
                fila[2] = mapaMaquinas.getOrDefault(a.getMaquinariaFK(), "ID: " + a.getMaquinariaFK());
                fila[3] = mapaTipos.getOrDefault(a.getTipoAveriaFK(), "ID: " + a.getTipoAveriaFK());

                // Fechas
                fila[4] = (a.getFechaInicioAver() != null) ? a.getFechaInicioAver().format(formatter) : "";
                fila[5] = (a.getFechaAsigTecnico() != null) ? a.getFechaAsigTecnico().format(formatter) : "-";
                fila[6] = (a.getFechaAcepTecnico() != null) ? a.getFechaAcepTecnico().format(formatter) : "-";
                fila[7] = (a.getFechaFinalizTecnico() != null) ? a.getFechaFinalizTecnico().format(formatter) : "-";

                // Estado lógico
                if (a.getFechaFinalizTecnico() != null) fila[8] = "Finalizada";
                else if (a.getFechaAsigTecnico() != null) fila[8] = "En proceso";
                else fila[8] = "Pendiente";
                
                // Usuarios
                fila[9] = mapaUsuarios.getOrDefault(a.getUsuarioReportaFK(), "ID: " + a.getUsuarioReportaFK());
                
                // Técnico
                if (a.getUsuarioTecnicoFK() != null && a.getUsuarioTecnicoFK() != 0) {
                    fila[10] = mapaUsuarios.getOrDefault(a.getUsuarioTecnicoFK(), "ID: " + a.getUsuarioTecnicoFK());
                } else {
                    fila[10] = "Sin asignar";
                }
                
                fila[11] = a.getProcRealizadoTecnico();

                filas.add(fila);
            }
        } catch (Exception e) {
            System.err.println("Error procesando los datos para la tabla.");
        }
        return filas;
    }
    
    // =========================================================================
    // 2. MÉTODOS DE APOYO (Listas Desplegables y Filtros en Memoria)
    // =========================================================================

    public List<Maquinaria> obtenerTodasLasMaquinas() { return maquinariaDao.listarMaquinaria(); }
    public List<Usuario> obtenerTodosLosUsuarios() { return usuarioDao.listarUsuarios(); }
    public List<TipoAveria> obtenerTiposAveria() { return tipoAveriaDao.listar(); }

    /**
     * Filtra listas en memoria usando Java 8 Streams para mayor eficiencia y limpieza.
     */
    public List<Maquinaria> filtrarMaquinas(List<Maquinaria> listaOriginal, String texto) {
        if (listaOriginal == null) return new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) return new ArrayList<>(listaOriginal);
        
        String t = texto.toLowerCase();
        return listaOriginal.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Usuario> filtrarUsuarios(List<Usuario> listaOriginal, String texto) {
        if (listaOriginal == null) return new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) return new ArrayList<>(listaOriginal);

        String t = texto.toLowerCase();
        return listaOriginal.stream()
                .filter(u -> (u.getNombre() + " " + u.getApellido()).toLowerCase().contains(t))
                .collect(Collectors.toList());
    }
    
    public List<Usuario> obtenerSoloTecnicos(List<Usuario> todosLosUsuarios) {
        if (todosLosUsuarios == null) return new ArrayList<>();
        return todosLosUsuarios.stream()
                .filter(u -> u.getCodigoRolFK() == 703)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // 3. MÉTODOS TRANSACCIONALES (Insertar, Actualizar)
    // =========================================================================

    public boolean registrarAveria(String descripcion, Maquinaria maq, Usuario usuReporta, Usuario usuTecnico, TipoAveria tipo) {
        // Validaciones
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripción es obligatoria.");
            return false;
        }
        if (maq == null || usuReporta == null || tipo == null) {
            JOptionPane.showMessageDialog(null, "Máquina, Reportador y Tipo son obligatorios.");
            return false;
        }

        Averia nueva = new Averia();
        nueva.setDescInicAveria(descripcion);
        nueva.setMaquinariaFK(maq.getCodigoMaquinaria());
        nueva.setUsuarioReportaFK(usuReporta.getCodigoUsuario());
        nueva.setTipoAveriaFK(tipo.getCodigoTipoAveria());
        nueva.setUsuarioTecnicoFK(usuTecnico != null ? usuTecnico.getCodigoUsuario() : null);

        try {
            averiaDao.insertar(nueva);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error guardando en BD: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarAveria(
            int idAveria, String descripcion, String procedimiento, 
            Maquinaria maq, Usuario usuReporta, Usuario usuTecnico, TipoAveria tipo,
            LocalDateTime fechaReporte, LocalDateTime fechaAsig, 
            LocalDateTime fechaAcep, LocalDateTime fechaFinal) {
        
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripción es obligatoria.");
            return false;
        }
        if (maq == null || usuReporta == null || tipo == null) {
            JOptionPane.showMessageDialog(null, "Máquina, Usuario que reporta y Tipo son obligatorios.");
            return false;
        }

        Averia a = new Averia();
        a.setCodigoAveria(idAveria);
        a.setDescInicAveria(descripcion);
        a.setProcRealizadoTecnico(procedimiento);
        
        a.setMaquinariaFK(maq.getCodigoMaquinaria());
        a.setUsuarioReportaFK(usuReporta.getCodigoUsuario());
        a.setTipoAveriaFK(tipo.getCodigoTipoAveria());
        a.setUsuarioTecnicoFK(usuTecnico != null ? usuTecnico.getCodigoUsuario() : null);

        a.setFechaInicioAver(fechaReporte); 
        a.setFechaAsigTecnico(fechaAsig);
        a.setFechaAcepTecnico(fechaAcep);
        a.setFechaFinalizTecnico(fechaFinal);

        try {
            averiaDao.actualizar(a); 
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error actualizando en BD: " + e.getMessage());
            return false;
        }
    }
}