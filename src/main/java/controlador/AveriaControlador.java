/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter; // para formatear fechas
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
 *
 * @author yosnavmol
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
            e.printStackTrace();
        }
    }
    
    // MÉTODOS PARA LA VENTANA PRINCIPAL (TABLA)
    public List<Object[]> listarAveriasParaVista() {
        List<Object[]> filas = new ArrayList<>();
        
        // Formateador para que las fechas se vean amigables (Ej: 2023-10-25 14:30)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            List<Averia> listaAverias = averiaDao.listar();
            List<Maquinaria> listaMaquinas = maquinariaDao.listarMaquinaria();
            List<TipoAveria> listaTipos = tipoAveriaDao.listar();
            List<Usuario> listaUsuarios = usuarioDao.listarUsuarios();

            // --- MAPAS (Igual que antes) ---
            Map<Integer, String> mapaMaquinas = new HashMap<>();
            for (Maquinaria m : listaMaquinas) mapaMaquinas.put(m.getCodigoMaquinaria(), m.getNombre());

            Map<Integer, String> mapaTipos = new HashMap<>();
            for (TipoAveria t : listaTipos) mapaTipos.put(t.getCodigoTipoAveria(), t.getDescripcionTipoAv());

            Map<Integer, String> mapaUsuarios = new HashMap<>();
            for (Usuario u : listaUsuarios) mapaUsuarios.put(u.getCodigoUsuario(), u.getNombre() + " " + u.getApellido());

            // --- CONSTRUCCIÓN DE FILAS ---
            for (Averia a : listaAverias) {
                // Aumentamos el tamaño del array a 12 para que quepan las fechas nuevas
                Object[] fila = new Object[12]; 

                fila[0] = a.getCodigoAveria();
                fila[1] = a.getDescInicAveria();
                
                // Máquina
                String nomMaq = mapaMaquinas.get(a.getMaquinariaFK());
                fila[2] = (nomMaq != null) ? nomMaq : "ID: " + a.getMaquinariaFK(); 

                // Tipo
                String descTipo = mapaTipos.get(a.getTipoAveriaFK());
                fila[3] = (descTipo != null) ? descTipo : "ID: " + a.getTipoAveriaFK();

                // --- FECHAS (Formateadas) ---
                fila[4] = (a.getFechaInicioAver() != null) ? a.getFechaInicioAver().format(formatter) : "";
                
                // NUEVAS FECHAS
                fila[5] = (a.getFechaAsigTecnico() != null) ? a.getFechaAsigTecnico().format(formatter) : "-";
                fila[6] = (a.getFechaAcepTecnico() != null) ? a.getFechaAcepTecnico().format(formatter) : "-";
                fila[7] = (a.getFechaFinalizTecnico() != null) ? a.getFechaFinalizTecnico().format(formatter) : "-";

                // Estado
                if (a.getFechaFinalizTecnico() != null) fila[8] = "Finalizada";
                else if (a.getFechaAsigTecnico() != null) fila[8] = "En proceso";
                else fila[8] = "Pendiente";
                
                // Reportado Por
                String nomReporta = mapaUsuarios.get(a.getUsuarioReportaFK());
                fila[9] = (nomReporta != null) ? nomReporta : "ID: " + a.getUsuarioReportaFK();

                // Técnico
                if (a.getUsuarioTecnicoFK() != null && a.getUsuarioTecnicoFK() != 0) {
                    String nomTecnico = mapaUsuarios.get(a.getUsuarioTecnicoFK());
                    fila[10] = (nomTecnico != null) ? nomTecnico : "ID: " + a.getUsuarioTecnicoFK();
                } else {
                    fila[10] = "Sin asignar";
                }
                
                // Procedimiento (Última columna)
                fila[11] = a.getProcRealizadoTecnico();

                filas.add(fila);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filas;
    }
    
    // NUEVOS MÉTODOS PARA LA VENTANA "NUEVA AVERÍA"
    // Obtiene la lista completa de máquinas para el selector.
    public List<Maquinaria> obtenerTodasLasMaquinas() {
        // Asegúrate de que tu DAO tiene este método (puede llamarse listar() o listarMaquinaria())
        return maquinariaDao.listarMaquinaria(); 
    }

    // Obtiene la lista completa de usuarios (para reportador y técnico).
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    // Obtiene los tipos de avería para el ComboBox.
    public List<TipoAveria> obtenerTiposAveria() {
        return tipoAveriaDao.listar();
    }

    // Filtra una lista de máquinas por nombre.
    public List<Maquinaria> filtrarMaquinas(List<Maquinaria> listaOriginal, String texto) {
        List<Maquinaria> filtro = new ArrayList<>();
        if (listaOriginal == null) return filtro;
        
        for (Maquinaria m : listaOriginal) {
            if (m.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtro.add(m);
            }
        }
        return filtro;
    }

    // Filtra una lista de usuarios por nombre o apellido.
    public List<Usuario> filtrarUsuarios(List<Usuario> listaOriginal, String texto) {
        List<Usuario> filtro = new ArrayList<>();
        if (listaOriginal == null) return filtro;

        for (Usuario u : listaOriginal) {
            String nombreCompleto = u.getNombre() + " " + u.getApellido();
            if (nombreCompleto.toLowerCase().contains(texto.toLowerCase())) {
                filtro.add(u);
            }
        }
        return filtro;
    }

    /*
     * Valida y registra una nueva avería en la base de datos.
     * Retorna true si tuvo éxito.
     */
    public boolean registrarAveria(String descripcion, Maquinaria maq, Usuario usuReporta, Usuario usuTecnico, TipoAveria tipo) {
        
        // 1. Validaciones
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripción es obligatoria.");
            return false;
        }
        if (maq == null) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una máquina.");
            return false;
        }
        if (usuReporta == null) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar quién reporta la avería.");
            return false;
        }
        if (tipo == null) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un tipo de avería.");
            return false;
        }

        // 2. Crear objeto Averia
        Averia nueva = new Averia();
        nueva.setDescInicAveria(descripcion);
        nueva.setMaquinariaFK(maq.getCodigoMaquinaria());
        nueva.setUsuarioReportaFK(usuReporta.getCodigoUsuario());
        nueva.setTipoAveriaFK(tipo.getCodigoTipoAveria());

        // Técnico es opcional
        if (usuTecnico != null) {
            nueva.setUsuarioTecnicoFK(usuTecnico.getCodigoUsuario());
        } else {
            nueva.setUsuarioTecnicoFK(null);
        }

        // 3. Guardar (El DAO pone las fechas automáticas)
        try {
            averiaDao.insertar(nueva);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error guardando en BD: " + e.getMessage());
            return false;
        }
    }
}
