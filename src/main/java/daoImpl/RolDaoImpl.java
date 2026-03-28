package daoImpl;

import dao.RolDao;
import modelo.Rol;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion DAO para gestionar los roles.
 *
 * @author Thanya
 */
public class RolDaoImpl implements RolDao {

    // Fuente de conexion a la base de datos
    private final DataSource dataSource;

    /**
     * Constructor que recibe la fuente de datos para establecer la conexion a la base de datos.
     *
     * @param dataSource fuente de datos para la conexion
     */
    public RolDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta un nuevo rol en la base de datos.
     * Antes de insertar, comprueba si el codigo del rol ya existe para evitar duplicados.
     *
     * @param rol objeto Rol a insertar
     * @throws RuntimeException si el codigo del rol ya existe o si ocurre un error al insertar
     */
    @Override
    public void insertarRol(Rol rol) {

        // Comprobamos antes si el codigo ya existe
        if (existeID(rol.getCodigoRol())) {
            throw new RuntimeException("El codigo de rol ya existe.");
        }

        final String sql = "INSERT INTO rol (codigoRol, descripcionRol) VALUES (?,?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, rol.getCodigoRol());
            ps.setString(2, rol.getDescripcionRol());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error insertando rol" + e.getMessage());
        }
    }

    /**
     * Comprueba si existe un rol con el codigo proporcionado.
     *
     * @param codigoRol codigo del rol a comprobar
     * @return true si existe un rol con ese codigo, false en caso contrario
     * @throws RuntimeException si ocurre un error al consultar la base de datos
     */
    @Override
    public boolean existeID(int codigoRol) {
        final String sql = "SELECT 1 FROM rol WHERE codigoRol = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigoRol);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando si existe el rol: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la descripcion de un rol existente.
     * Antes de actualizar, comprueba si el rol existe para evitar errores.
     *
     * @param rol objeto Rol con el codigo del rol a actualizar y la nueva descripcion
     * @throws RuntimeException si el rol no existe o si ocurre un error al actualizar
     */
    @Override
    public void actualizarRol(Rol rol) {

        // Solo se actualiza si el rol existe
        if (!existeID(rol.getCodigoRol())) {
            throw new RuntimeException("No existe un rol con ese codigo.");
        }

        final String sql = "UPDATE rol SET descripcionRol = ? WHERE codigoRol = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rol.getDescripcionRol());
            ps.setInt(2, rol.getCodigoRol());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se pudo actualizar el rol.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando rol: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un rol por su codigo.
     * Antes de eliminar, comprueba si el rol existe y si no tiene usuarios asociados para evitar errores.
     *
     * @param codigoRol codigo del rol a eliminar
     * @throws RuntimeException si el rol no existe, si tiene usuarios asociados o si ocurre un error al eliminar
     */
    @Override
    public void eliminarRol(int codigoRol) {

        if (!existeID(codigoRol)) {
            throw new RuntimeException("No existe un rol con ese codigo.");
        }

        final String sqlComprobacion = "SELECT COUNT(*) FROM rol WHERE codigoRol = ?";
        final String sqlEliminar = "DELETE FROM rol WHERE codigoRol = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement psComprobacion = conn.prepareStatement(sqlComprobacion)) {

            psComprobacion.setInt(1, codigoRol);
            ResultSet rs = psComprobacion.executeQuery();

            if (rs.next()) {
                int totalUsuarios = rs.getInt(1);

                // Si hay usuarios asociados no se elimina
                if (totalUsuarios > 0) {
                    throw new RuntimeException("No se puede eliminar el rol porque esta asociado a usuarios.");
                }
            }

            PreparedStatement psEliminar = conn.prepareStatement(sqlEliminar);
            psEliminar.setInt(1, codigoRol);

            int filasAfectadas = psEliminar.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se pudo eliminar el rol.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando rol: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos los roles disponibles en la base de datos.
     *
     * @return lista de objetos Rol con todos los roles registrados
     * @throws RuntimeException si ocurre un error al consultar la base de datos
     */
    @Override
    public List<Rol> listarRoles() {
        List<Rol> listaRoles = new ArrayList<>();
        final String sql = "SELECT codigoRol, descripcionRol FROM rol";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();

                rol.setCodigoRol(rs.getInt("codigoRol"));
                rol.setDescripcionRol(rs.getString("descripcionRol"));

                listaRoles.add(rol);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listando roles: " + e.getMessage(), e);
        }

        return listaRoles;
    }

    /**
     * Recupera un rol a partir de su descripcion.
     *
     * @param descripcionRol descripcion del rol a recuperar
     * @return objeto Rol con los datos del rol encontrado o null si no se encuentra
     * @throws RuntimeException si ocurre un error al consultar la base de datos
     */
    @Override
    public Rol recuperarRolPorCodigo(String descripcionRol) {
        final String sql = "SELECT * FROM rol WHERE descripcionRol = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, descripcionRol);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol();

                    rol.setCodigoRol(rs.getInt("codigoRol"));
                    rol.setDescripcionRol(rs.getString("descripcionRol"));

                    return rol;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando si existe el rol: " + e.getMessage(), e);
        }
    }
}