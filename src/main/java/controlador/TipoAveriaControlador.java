package controlador;

import config.DataSourceFactory;
import dao.TipoAveriaDao;
import daoImpl.TipoAveriaDaoImpl;
import modelo.TipoAveria;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de gestionar los tipos de averia.
 * Este controlador se encarga de realizar las operaciones CRUD sobre los tipos de averia,
 * asi como de preparar los datos para mostrarlos en la vista.
 * @author yosnavmol
 */
public class TipoAveriaControlador {

    // DAO para acceder a la tabla de tipos de averia
    private TipoAveriaDao tipoDao;

    /**
     * Constructor del controlador.
     */
    public TipoAveriaControlador() {
        try {
            this.tipoDao = new TipoAveriaDaoImpl(DataSourceFactory.getDataSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve los tipos de averia preparados para mostrarse en una tabla.
     */
    public List<Object[]> listarParaTabla() {
        List<Object[]> filas = new ArrayList<>();
        try {
            List<TipoAveria> lista = tipoDao.listar();

            for (TipoAveria t : lista) {
                filas.add(new Object[]{
                        t.getCodigoTipoAveria(),
                        t.getDescripcionTipoAv(),
                        t.getTiempoPromRepar()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filas;
    }

    /**
     * Devuelve la lista completa de tipos de averia.
     */
    public List<TipoAveria> listarTiposAveria() {
        return tipoDao.listar();
    }

    /**
     * Metodo para registrar un nuevo tipo de averia.
     */
    public boolean registrar(int id, String descripcion, float tiempo) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripcion es obligatoria.");
            return false;
        }

        // Comprobamos si el ID ya existe
        if (tipoDao.existeId(id)) {
            JOptionPane.showMessageDialog(null, "Ya existe un Tipo de Averia con el ID " + id, "ID Duplicado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        TipoAveria t = new TipoAveria();
        t.setCodigoTipoAveria(id);
        t.setDescripcionTipoAv(descripcion);
        t.setTiempoPromRepar(tiempo);

        tipoDao.insertar(t);
        return true;
    }

    /**
     * Metodo para actualizar un tipo de averia existente.
     */
    public boolean actualizar(int id, String descripcion, float tiempo) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripcion es obligatoria.");
            return false;
        }

        TipoAveria t = new TipoAveria();
        t.setCodigoTipoAveria(id);
        t.setDescripcionTipoAv(descripcion);
        t.setTiempoPromRepar(tiempo);

        tipoDao.actualizar(t);
        return true;
    }

    /**
     * Metodo para eliminar un tipo de averia por su id.
     */
    public boolean eliminar(int id) {
        return tipoDao.eliminar(id);
    }

    /**
     * Metodo para obtener el id de un tipo de averia a partir de su descripcion.
     */
    public int obtenerIdTipoAveria(String descripcion) {
        return tipoDao.buscarTipoAveriaPorDescripcion(descripcion);
    }
}