/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.RolDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modelo.Rol;

/**
 *
 * @author Thanya
 */
public class RolDaoImpl implements RolDao {

    //Atributos
    private final DataSource dataSource;

    //Constructor
    public RolDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insertarRol(Rol rol) {

        // Primero comprobamos si ya existe el codigo
        if (existeID(rol.getCodigoRol())) {
            throw new RuntimeException("El codigo de rol ya existe.");
        }

        final String sql = "INSERT INTO rol (codigoRol, descripcionRol) VALUES (?,?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            /**
             * Aqui estamos indicandole que campos debe insertar en la query y
             * el orden del insert
             *
             * en este metodo no utilizamos getGeneratedKeys() este metodo es
             * cuando queremos traernos un datos generado por la base de datos
             * por ejemplo el ID Auto incremental
             */
            ps.setInt(1, rol.getCodigoRol());
            ps.setString(2, rol.getDescripcionRol());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error insertando rol" + e.getMessage());
        }
    }

    @Override
    public boolean existeID(int codigoRol) {
        final String sql = "SELECT 1 FROM rol WHERE codigoRol = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigoRol);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
                // Si hay resultado -> existe
                // Si no hay resultado -> no existe
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error comprobando si existe el rol: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarRol(Rol rol) {

        // Comprobamos que el rol existe antes de actualizar
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

    @Override
    public void eliminarRol(int codigoRol) {

        // Comprobamos si existe antes de eliminar
        if (!existeID(codigoRol)) {
            throw new RuntimeException("No existe un rol con ese codigo.");
        }

        final String sql = "DELETE FROM rol WHERE codigoRol = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigoRol);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se pudo eliminar el rol.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando rol: " + e.getMessage(), e);
        }
    }

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

}
