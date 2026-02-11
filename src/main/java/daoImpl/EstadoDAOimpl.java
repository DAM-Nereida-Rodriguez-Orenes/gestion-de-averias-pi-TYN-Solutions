/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;
import dao.EstadoDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modelo.Estado;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class EstadoDAOimpl implements EstadoDAO{
    private DataSource dataSource;
    
    public EstadoDAOimpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * Convierte IDs "cortos" (1..99) a IDs reales (801..899) sumando 800.
     * Si ya viene un ID 800..899, lo deja tal cual.
     * Si viene otro valor, lanza excepción para evitar datos inválidos.
     */
    private int normalizarId800(int id) {
        if (id >= 1 && id <= 99) return 800 + id;     // 1..99 -> 801..899
        if (id >= 800 && id <= 899) return id;        // ya válido
        throw new IllegalArgumentException(
                "El código de Estado debe ser 1..99 (se convertirá a 800+X) o 800..899. Recibido: " + id
        );
    }

    @Override
    public void insertar(Estado e) {
        final int id = normalizarId800(e.getCodigoEstado());

        // Si quieres que el objeto quede coherente con la BD:
        e.setCodigoEstado(id);

        // Comprobar existencia antes de insertar
        if (existeID(id)) {
            throw new RuntimeException("No se puede insertar: ya existe un Estado con ID " + id);
        }

        final String sql = "INSERT INTO estado (codigoEstado, descripcionEstado) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getCodigoEstado());
            ps.setString(2, e.getDescripcionEstado());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error insertando estado", ex);
        }
    }

    @Override
    public void modificar(Estado e) {
        final int id = normalizarId800(e.getCodigoEstado());
        e.setCodigoEstado(id);

        final String sql = "UPDATE estado SET descripcionEstado = ? WHERE codigoEstado = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getDescripcionEstado());
            ps.setInt(2, e.getCodigoEstado());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error modificando estado", ex);
        }
    }

    @Override
    public void eliminar(Estado e) {
        final int id = normalizarId800(e.getCodigoEstado());
        e.setCodigoEstado(id);

        final String sql = "DELETE FROM estado WHERE codigoEstado = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getCodigoEstado());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando estado", ex);
        }
    }

    @Override
    public List<Estado> listarEstado() {
        final String sql = "SELECT codigoEstado, descripcionEstado FROM estado ORDER BY codigoEstado ASC";
        List<Estado> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estado e = new Estado();
                e.setCodigoEstado(rs.getInt("codigoEstado"));
                e.setDescripcionEstado(rs.getString("descripcionEstado"));
                lista.add(e);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error listando estados", ex);
        }

        return lista;
    }

    @Override
    public boolean existeID(int id) {
        final int normalizado = normalizarId800(id);
        final String sql = "SELECT 1 FROM estado WHERE codigoEstado = ? LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, normalizado);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error comprobando existencia de ID en estado", ex);
        }
    }
}
