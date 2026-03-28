package controlador;

import config.DataSourceFactory;
import dao.AveriaDao;
import dao.MaquinariaDAO;
import dao.TipoAveriaDao;
import dao.UsuarioDao;
import daoImpl.AveriaDaoImpl;
import daoImpl.MaquinariaDAOimpl;
import daoImpl.TipoAveriaDaoImpl;
import daoImpl.UsuarioDaoImpl;
import modelo.Averia;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador principal para la gestión de Averías. Actúa como puente entre la
 * Vista (Interfaces Java Swing) y el DAO (Base de Datos).
 * * @author yosnavmol
 */
public class AveriaControlador {

    // DAOs necesarios para trabajar con averias, maquinaria, tipos y usuarios
    private AveriaDao averiaDao;
    private MaquinariaDAO maquinariaDao;
    private TipoAveriaDao tipoAveriaDao;
    private UsuarioDao usuarioDao;

    // Objetos de trabajo
    private Usuario usuario;
    private Averia averia;

    // Controlador de login usado para recuperar el usuario con sesion iniciada
    private LoginControlador loginControlador;

    /**
     * Obtiene la avería actualmente gestionada por el controlador.
     * @return Averia actual
     */
    public Averia getAveria() {
        return averia;
    }

    /**
     * Establece la avería actualmente gestionada por el controlador.
     * @param averia Averia a establecer
     */
    public void setAveria(Averia averia) {
        this.averia = averia;
    }

    /**
     * Obtiene el usuario actualmente gestionado por el controlador.
     * @return Usuario actual
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario actualmente gestionado por el controlador.
     * @param usuario Usuario a establecer
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Constructor del controlador.
     * Inicializa los DAO y el controlador de login.
     */
    public AveriaControlador() {
        try {
            javax.sql.DataSource ds = DataSourceFactory.getDataSource();
            this.averiaDao = new AveriaDaoImpl(ds);
            this.maquinariaDao = new MaquinariaDAOimpl(ds);
            this.tipoAveriaDao = new TipoAveriaDaoImpl(ds);
            this.usuarioDao = new UsuarioDaoImpl(ds);
            this.loginControlador = new LoginControlador();
        } catch (Exception e) {
            System.err.println("Error critico al inicializar los DAOs en AveriaControlador.");
        }
    }

    // =========================================================================
    // 1. METODOS DE LECTURA Y TABLA PRINCIPAL
    // =========================================================================

    /**
     * Obtiene una avería a partir de su id.
     * @param idAveria Identificador de la avería
     * @return Averia encontrada o null si no existe
     */
    public Averia obtenerAveriaPorId(int idAveria) {
        try {
            List<Averia> listaResultados = averiaDao.buscarPorFiltros(idAveria, null, null, null, null, null, null, null);
            if (listaResultados != null && !listaResultados.isEmpty()) {
                return listaResultados.get(0);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la averia por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista las averías que se van a mostrar en la vista principal.
     * Si se recibe un usuario, se usa su código para filtrar.
     * @param usuario Usuario para filtrar las averías (opcional)
     * @return Lista de averías mapeadas para la tabla
     */
    public List<Object[]> listarAveriasParaVista(Usuario usuario) {
        try {
            Integer codigoUsuario = null;

            if (usuario != null) {
                codigoUsuario = usuario.getCodigoUsuario();
            }

            List<Averia> listaAverias = averiaDao.buscarPorFiltros(
                    null, null, null, null, codigoUsuario, null, null, null
            );

            return mapearAveriasParaTabla(listaAverias);

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene averías filtradas según los datos introducidos en la vista.
     * @param idAveria Id de la avería
     * @param descripcion Descripción
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @param idUsuarioReporta Id usuario que reporta
     * @param idTecnico Id técnico asignado
     * @param idMaquinaria Id maquinaria
     * @param idTipoAveria Id tipo de avería
     * @return Lista de averías mapeadas para la tabla
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
     * Transforma la lista de averías en filas preparadas para mostrarse en un JTable.
     * Sustituye ids por nombres reales.
     * @param listaAverias Lista de averías
     * @return Lista de filas para la tabla
     */
    private List<Object[]> mapearAveriasParaTabla(List<Averia> listaAverias) {
        List<Object[]> filas = new ArrayList<>();

        if (listaAverias == null || listaAverias.isEmpty()) {
            return filas;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try {
            // Cargamos los catalogos necesarios
            List<Maquinaria> listaMaquinas = maquinariaDao.listarMaquinaria();
            List<TipoAveria> listaTipos = tipoAveriaDao.listar();
            List<Usuario> listaUsuarios = usuarioDao.listarUsuarios();

            // Mapas para localizar rapidamente nombres por id
            Map<Integer, String> mapaMaquinas = new HashMap<>();
            for (Maquinaria m : listaMaquinas) {
                mapaMaquinas.put(m.getCodigoMaquinaria(), m.getNombre());
            }

            Map<Integer, String> mapaTipos = new HashMap<>();
            for (TipoAveria t : listaTipos) {
                mapaTipos.put(t.getCodigoTipoAveria(), t.getDescripcionTipoAv());
            }

            Map<Integer, String> mapaUsuarios = new HashMap<>();
            for (Usuario u : listaUsuarios) {
                mapaUsuarios.put(u.getCodigoUsuario(), u.getNombre() + " " + u.getApellido());
            }

            // Recorremos cada averia y montamos la fila
            for (Averia a : listaAverias) {
                Object[] fila = new Object[12];

                fila[0] = a.getCodigoAveria();
                fila[1] = a.getDescInicAveria();

                fila[2] = mapaMaquinas.getOrDefault(a.getMaquinariaFK(), "ID: " + a.getMaquinariaFK());
                fila[3] = mapaTipos.getOrDefault(a.getTipoAveriaFK(), "ID: " + a.getTipoAveriaFK());

                // Fechas
                fila[4] = (a.getFechaInicioAver() != null) ? a.getFechaInicioAver().format(formatter) : "";
                fila[5] = (a.getFechaAsigTecnico() != null) ? a.getFechaAsigTecnico().format(formatter) : "-";
                fila[6] = (a.getFechaAcepTecnico() != null) ? a.getFechaAcepTecnico().format(formatter) : "-";
                fila[7] = (a.getFechaFinalizTecnico() != null) ? a.getFechaFinalizTecnico().format(formatter) : "-";

                // Estado de la averia segun sus fechas
                if (a.getFechaFinalizTecnico() != null) {
                    fila[8] = "Finalizada";
                } else if (a.getFechaAsigTecnico() != null) {
                    fila[8] = "En proceso";
                } else {
                    fila[8] = "Pendiente";
                }

                // Usuario que reporta
                fila[9] = mapaUsuarios.getOrDefault(a.getUsuarioReportaFK(), "ID: " + a.getUsuarioReportaFK());

                // Tecnico asignado
                if (a.getUsuarioTecnicoFK() != null && a.getUsuarioTecnicoFK() != 0) {
                    fila[10] = mapaUsuarios.getOrDefault(a.getUsuarioTecnicoFK(), "ID: " + a.getUsuarioTecnicoFK());
                } else {
                    fila[10] = "Sin asignar";
                }

                // Procedimiento realizado
                fila[11] = a.getProcRealizadoTecnico();

                filas.add(fila);
            }
        } catch (Exception e) {
            System.err.println("Error procesando los datos para la tabla.");
        }

        return filas;
    }

    // =========================================================================
    // 2. METODOS DE APOYO
    // =========================================================================

    /**
     * Devuelve todas las máquinas.
     * @return Lista de maquinaria
     */
    public List<Maquinaria> obtenerTodasLasMaquinas() {
        return maquinariaDao.listarMaquinaria();
    }

    /**
     * Devuelve todos los usuarios.
     * @return Lista de usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    /**
     * Devuelve todos los tipos de avería.
     * @return Lista de tipos de avería
     */
    public List<TipoAveria> obtenerTiposAveria() {
        return tipoAveriaDao.listar();
    }

    /**
     * Filtra en memoria una lista de máquinas por nombre.
     * @param listaOriginal Lista original de maquinaria
     * @param texto Texto a buscar
     * @return Lista filtrada de maquinaria
     */
    public List<Maquinaria> filtrarMaquinas(List<Maquinaria> listaOriginal, String texto) {
        if (listaOriginal == null) {
            return new ArrayList<>();
        }

        if (texto == null || texto.trim().isEmpty()) {
            return new ArrayList<>(listaOriginal);
        }

        String t = texto.toLowerCase();

        return listaOriginal.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    /**
     * Filtra en memoria una lista de usuarios por nombre y apellido.
     * @param listaOriginal Lista original de usuarios
     * @param texto Texto a buscar
     * @return Lista filtrada de usuarios
     */
    public List<Usuario> filtrarUsuarios(List<Usuario> listaOriginal, String texto) {
        if (listaOriginal == null) {
            return new ArrayList<>();
        }

        if (texto == null || texto.trim().isEmpty()) {
            return new ArrayList<>(listaOriginal);
        }

        String t = texto.toLowerCase();

        return listaOriginal.stream()
                .filter(u -> (u.getNombre() + " " + u.getApellido()).toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene solo los usuarios que tienen rol de técnico.
     * @return Lista de usuarios con rol de técnico
     */
    public List<Usuario> obtenerSoloTecnicos() {
        try {
            modelo.Rol rolTecnico = new modelo.Rol();
            rolTecnico.setDescripcionRol("Mecanico");

            return usuarioDao.buscarPorFiltrosUsuario(null, null, null, rolTecnico, null, null);

        } catch (Exception e) {
            System.err.println("Error al obtener la lista de tecnicos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Devuelve tecnicos ordenados segun la carga de trabajo.
     * @param tipo Tipo de ordenamiento
     * @return Lista de tecnicos ordenados
     */
    public List<Usuario> buscarTecnicosOrdenadorPorCarga(int tipo) {
        try {
            return usuarioDao.buscarTecnicosOrdenadorPorCarga(tipo);
        } catch (Exception e) {
            System.err.println("Error al obtener la lista de tecnicos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene los motivos o datos auxiliares de un tecnico segun el tipo de averia.
     * @param codigoTecnico Codigo del tecnico
     * @param codigoTipoAveria Codigo del tipo de averia
     * @return Object[] con los motivos del tecnico
     */
    public Object[] obtenerMotivosTecnico(int codigoTecnico, int codigoTipoAveria) {
        return usuarioDao.obtenerMotivosTecnico(codigoTecnico, codigoTipoAveria);
    }

    // =========================================================================
    // 3. METODOS TRANSACCIONALES
    // =========================================================================

    /**
     * Metodo para registrar una nueva averia.
     * El usuario que reporta se obtiene automaticamente desde la sesion.
     * @param descripcion Descripción de la avería
     * @param maq Maquinaria asociada
     * @param usuTecnico Técnico asignado (opcional)
     * @param tipo Tipo de avería
     * @return true si se registra correctamente, false en caso contrario
     */
    public boolean registrarAveria(String descripcion, Maquinaria maq, Usuario usuTecnico, TipoAveria tipo) {

        // Recuperamos el usuario logueado
        Usuario usuReporta = loginControlador.getUsuarioSesion();

        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripcion es obligatoria.");
            return false;
        }

        if (maq == null || tipo == null) {
            JOptionPane.showMessageDialog(null, "Maquina y Tipo son obligatorios.");
            return false;
        }

        if (usuReporta == null) {
            JOptionPane.showMessageDialog(null, "No se ha podido recuperar el usuario logueado.");
            return false;
        }

        // Creamos la nueva averia
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

    /**
     * Metodo para actualizar una averia ya existente.
     * @param idAveria Id de la avería a actualizar
     * @param descripcion Nueva descripción
     * @param procedimiento Nuevo procedimiento
     * @param maq Nueva maquinaria
     * @param usuReporta Nuevo usuario que reporta
     * @param usuTecnico Nuevo técnico asignado (opcional)
     * @param tipo Nuevo tipo de avería
     * @param fechaReporte Nueva fecha de reporte
     * @param fechaAsig Nueva fecha de asignación
     * @param fechaAcep Nueva fecha de aceptación
     * @param fechaFinal Nueva fecha de finalización
     * @return true si se actualiza correctamente, false en caso contrario
     */
    public boolean actualizarAveria(
            int idAveria, String descripcion, String procedimiento,
            Maquinaria maq, Usuario usuReporta, Usuario usuTecnico, TipoAveria tipo,
            LocalDateTime fechaReporte, LocalDateTime fechaAsig,
            LocalDateTime fechaAcep, LocalDateTime fechaFinal) {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripcion es obligatoria.");
            return false;
        }

        if (maq == null || usuReporta == null || tipo == null) {
            JOptionPane.showMessageDialog(null, "Maquina, Usuario que reporta y Tipo son obligatorios.");
            return false;
        }

        // Creamos el objeto averia con los nuevos datos
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

    /**
     * Devuelve el usuario que tiene la sesion iniciada.
     * @return Usuario con sesion activa
     */
    public Usuario getUsuarioSesion() {
        return loginControlador.getUsuarioSesion();
    }
}