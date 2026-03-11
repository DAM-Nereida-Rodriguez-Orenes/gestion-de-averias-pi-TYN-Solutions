/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import daoImpl.EstadoDAOimpl;
import java.util.List;
import java.util.Optional;
import modelo.Estado;
import modelo.TipoMaquinaria;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionEstadoMaquinaControlador {
    private final EstadoDAOimpl eDAO = new EstadoDAOimpl(DataSourceFactory.getDataSource());
    
   // LISTAR
    public List<Estado> listarEstados() {
        return eDAO.listarEstado();
    }

    // CREAR OBJETO SELECCIONADO DESDE LA TABLA
    public Estado obtenerEstadoSeleccionado(int codigo, String descripcion) {
        Estado e = new Estado();
        e.setCodigoEstado(codigo);
        e.setDescripcionEstado(descripcion);
        return e;
    }

    // ELIMINAR
    public void eliminarEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }

        eDAO.eliminar(estado);
    }

    // INSERTAR
    public boolean insertarEstado(String id, String desc) {
        boolean flag = true;

        try {
            int ID = Integer.parseInt(id);

            if (desc == null || desc.trim().isEmpty()) {
                return false;
            }

            Estado e = new Estado(ID, desc.trim());
            eDAO.insertar(e);

        } catch (NumberFormatException ex) {
            System.out.println("Error de formato: " + ex.getMessage());
            flag = false;
            return flag;

        } catch (RuntimeException ex) {
            System.out.println("Error al insertar estado: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }

        return flag;
    }

    // MODIFICAR
    public boolean modificarEstado(String id, String descripcion) {
        try {
            int ID = Integer.parseInt(id);

            if (descripcion == null || descripcion.trim().isEmpty()) {
                return false;
            }

            Estado e = new Estado(ID, descripcion.trim());
            eDAO.modificar(e);

            return true;

        } catch (NumberFormatException ex) {
            System.out.println("Error: el ID no es un número válido " + ex.getMessage());
            return false;

        } catch (RuntimeException ex) {
            System.out.println("Error modificando estado: " + ex.getMessage());
            return false;
        }
    }

    // BUSCAR POR ID
    public Optional<Estado> buscarEstadoPorID(int id) {
        try {
            return eDAO.buscarPorID(id);
        } catch (RuntimeException ex) {
            System.out.println("Error buscando estado por ID: " + ex.getMessage());
            return Optional.empty();
        }
    }

    // COMPROBAR SI EXISTE ID
    public boolean existeEstadoPorID(int id) {
        try {
            return eDAO.existeID(id);
        } catch (RuntimeException ex) {
            System.out.println("Error comprobando existencia del ID del estado: " + ex.getMessage());
            return false;
        }
    }
}
