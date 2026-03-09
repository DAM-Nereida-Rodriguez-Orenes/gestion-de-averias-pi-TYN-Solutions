/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.RolDao;
import daoImpl.RolDaoImpl;
import java.util.List;
import modelo.Rol;

/**
 *
 * @author Thanya
 */
public class GestionRolControlador {

    private Rol rol = new Rol();
    private RolDao rolDaoImpl = new RolDaoImpl(DataSourceFactory.getDataSource());

    /**
     * Metodo que crea un nuevo rol en la base de datos. Recibe el codigo del
     * rol y su descripcion desde la vista, crea el objeto Rol y llama al DAO
     * para insertarlo.
     *
     * @param codigoRol codigo del rol
     * @param descripcionRol descripcion del rol
     * @return true si el rol se inserta correctamente, false si ocurre un error
     */
    public String crearRol(String codigoRol, String descripcionRol) {
        try {
            // Convertimos el codigo a entero
            int codigoRolEntero = Integer.parseInt(codigoRol);

            // Creamos el objeto Rol
            Rol rol = new Rol();
            rol.setCodigoRol(codigoRolEntero);
            rol.setDescripcionRol(descripcionRol);

            // Llamamos al DAO para insertar el rol
            rolDaoImpl.insertarRol(rol);
            return null;

        } catch (NumberFormatException e) {
            return "El codigo de rol no tiene un formato valido.";

        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Listar roles
     *
     * @return
     */
    public List<Rol> recuperarRoles() {
        return rolDaoImpl.listarRoles();
    }

    /**
     * Eliminar roles
     *
     * @param codigoRol
     */
    public void eliminarRol(int codigoRol) {
        rolDaoImpl.eliminarRol(codigoRol);
    }

}
