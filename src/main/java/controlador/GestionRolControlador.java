package controlador;

import config.DataSourceFactory;
import dao.RolDao;
import daoImpl.RolDaoImpl;
import modelo.Rol;

import java.util.List;

/**
 * Controlador encargado de gestionar los roles de usuario.
 * Proporciona metodos para crear, actualizar, eliminar y buscar roles, asi como validar los datos de entrada.
 * @author Thanya
 */
public class GestionRolControlador {

    // Objeto rol y DAO de acceso a datos
    private Rol rol = new Rol();
    private RolDao rolDaoImpl = new RolDaoImpl(DataSourceFactory.getDataSource());

    /**
     * Obtiene el rol gestionado actualmente por el controlador.
     * @return Rol actual
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol gestionado actualmente por el controlador.
     * @param rol Rol a establecer
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Crea un nuevo rol en la base de datos.
     * @param codigoRol Código del rol
     * @param descripcionRol Descripción del rol
     * @return null si todo sale bien o un mensaje de error si falla
     */
    public String crearRol(String codigoRol, String descripcionRol) {
        try {
            // Convertimos el codigo a entero
            int codigoRolEntero = Integer.parseInt(codigoRol);

            // Creamos el objeto Rol
            Rol rol = new Rol();
            rol.setCodigoRol(codigoRolEntero);
            rol.setDescripcionRol(descripcionRol);

            // Insertamos el rol
            rolDaoImpl.insertarRol(rol);
            return null;

        } catch (NumberFormatException e) {
            return "El codigo de rol no tiene un formato valido.";

        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Actualiza un rol existente.
     * @param codigoRol Código del rol
     * @param descripcionRol Nueva descripción del rol
     * @return null si todo sale bien o un mensaje de error si falla
     */
    public String actualizarRol(String codigoRol, String descripcionRol) {
        try {
            int codigoRolEntero = Integer.parseInt(codigoRol);

            Rol rol = new Rol();
            rol.setCodigoRol(codigoRolEntero);
            rol.setDescripcionRol(descripcionRol);

            rolDaoImpl.actualizarRol(rol);
            return null;

        } catch (NumberFormatException e) {
            return "El codigo de rol no tiene un formato valido.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Elimina un rol de la base de datos.
     * @return null si todo sale bien o un mensaje de error si falla
     */
    public List<Rol> recuperarRoles() {
        return rolDaoImpl.listarRoles();
    }

    /**
     * Metodo para eliminar un rol por su codigo.
     */
    public void eliminarRol(int codigoRol) {
        rolDaoImpl.eliminarRol(codigoRol);
    }

    /**
     * Metodo para buscar un rol por su descripcion.
     */
    public Rol buscarRol(String descripcionRol) {
        return rolDaoImpl.recuperarRolPorCodigo(descripcionRol);
    }
}