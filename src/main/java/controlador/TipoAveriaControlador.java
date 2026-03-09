/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daoImpl.TipoAveriaDaoImpl;
import modelo.TipoAveria;
import config.DataSourceFactory;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author yosnavmol
 */
public class TipoAveriaControlador {
    private TipoAveriaDaoImpl tipoDao;

    public TipoAveriaControlador() {
        try {
            this.tipoDao = new TipoAveriaDaoImpl(DataSourceFactory.getDataSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    // GUARDAR (Llama al void)
    public boolean registrar(int id, String descripcion, float tiempo) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripción es obligatoria.");
            return false;
        }
        
        // Verificamos aquí para poder avisar al usuario, ya que el DAO es void
        if (tipoDao.existeId(id)) {
            JOptionPane.showMessageDialog(null, "Ya existe un Tipo de Avería con el ID " + id, "ID Duplicado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        TipoAveria t = new TipoAveria();
        t.setCodigoTipoAveria(id);
        t.setDescripcionTipoAv(descripcion);
        t.setTiempoPromRepar(tiempo);
        
        tipoDao.insertar(t); // Ejecutamos el método void
        return true; 
    }

    // ACTUALIZAR (Llama al void)
    public boolean actualizar(int id, String descripcion, float tiempo) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "La descripción es obligatoria.");
            return false;
        }

        TipoAveria t = new TipoAveria();
        t.setCodigoTipoAveria(id);
        t.setDescripcionTipoAv(descripcion);
        t.setTiempoPromRepar(tiempo);
        
        tipoDao.actualizar(t); // Ejecutamos el método void
        return true;
    }

    // ELIMINAR (Llama al boolean)
    public boolean eliminar(int id) {
        return tipoDao.eliminar(id);
    }
}
