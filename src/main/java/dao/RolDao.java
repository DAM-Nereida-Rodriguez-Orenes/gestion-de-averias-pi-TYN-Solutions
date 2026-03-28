package dao;

import modelo.Rol;

import java.util.List;

/**
 * Interfaz DAO para definir las operaciones de acceso a datos de roles.
 * @author Thanya
 */
public interface RolDao {

    // Inserta un nuevo rol
    void insertarRol(Rol rol);

    // Actualiza un rol existente
    void actualizarRol(Rol rol);

    // Elimina un rol por su codigo
    void eliminarRol(int codigoRol);

    // Comprueba si existe un rol con ese codigo
    boolean existeID(int codigoRol);

    // Lista todos los roles
    List<Rol> listarRoles();

    // Recupera un rol a partir de su descripcion
    Rol recuperarRolPorCodigo(String descripcionRol);
}