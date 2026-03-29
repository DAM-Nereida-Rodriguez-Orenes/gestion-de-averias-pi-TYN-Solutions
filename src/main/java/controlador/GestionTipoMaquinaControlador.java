/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.TipoMaquinariaDAO;
import daoImpl.TipoMaquinariaDAOimpl;
import modelo.TipoMaquinaria;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionTipoMaquinaControlador {
    private TipoMaquinariaDAO tDAO = new TipoMaquinariaDAOimpl(DataSourceFactory.getDataSource());
    
     public List<TipoMaquinaria> listarTiposMaquinaria() {
        return tDAO.listarTipoMaquinaria();
    }

    public TipoMaquinaria obtenerTipoSeleccionado(int codigo, String descripcion) {
        TipoMaquinaria t = new TipoMaquinaria();
        t.setCodigoTipoMaquinaria(codigo);
        t.setDescripcionMaq(descripcion);
        return t;
    }
    
    public void eliminarTipoMaquinaria(TipoMaquinaria tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de maquinaria no puede ser null");
        }

        tDAO.eliminar(tipo);
    }
    //INSERTAR
    public boolean insertarTipoMaquinaria(String id, String desc){
        boolean flag = true;
        try{
            int ID = Integer.parseInt(id);
            TipoMaquinaria tm = new TipoMaquinaria(ID, desc);
            tDAO.insertar(tm);
        }catch(NumberFormatException e){
            System.out.println("Error de formato "+ e.getMessage());
            flag = false;
            return flag;
        }  catch (RuntimeException e) {
            System.out.println("Error al insertar tipo de maquinaria: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return flag;
    }
    
    //modificar tipo de maquinaria
    public boolean modificarTipoMaquinaria(String id, String descripcion) {
        try {
            int ID = Integer.parseInt(id);

            if (descripcion == null || descripcion.trim().isEmpty()) {
                return false;
            }

            TipoMaquinaria tm = new TipoMaquinaria(ID, descripcion.trim());
            tDAO.modificar(tm);

            return true;

        } catch (NumberFormatException e) {

            System.out.println("Error: el ID no es un número válido " + e.getMessage());
            return false;

        } catch (RuntimeException e) {

            System.out.println("Error modificando tipo de maquinaria: " + e.getMessage());
            return false;
        }
    }
    
    //buscar por ID
    public Optional<TipoMaquinaria> buscarTipoMaquinariaPorID(int id) {
        try {
            return tDAO.buscarPorID(id);
        } catch (RuntimeException e) {
            System.out.println("Error buscando tipo de maquinaria por ID: " + e.getMessage());
            return Optional.empty();
        }
    }
}
