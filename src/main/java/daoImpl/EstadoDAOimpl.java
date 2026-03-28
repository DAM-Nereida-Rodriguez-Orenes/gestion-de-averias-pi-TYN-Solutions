/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.EstadoDAO;
import modelo.Estado;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class EstadoDAOimpl implements EstadoDAO{
    private final DataSource dataSource;
    
    public EstadoDAOimpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void insertar(Estado e) {

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
        final String sql = "SELECT 1 FROM estado WHERE codigoEstado = ? LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error comprobando existencia de ID en estado", ex);
        }
    }
    @Override
    public Optional<Estado> buscarPorID(int id) {

        final String sql = """
            SELECT codigoEstado, descripcionEstado
            FROM estado
            WHERE codigoEstado = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Estado st = new Estado(
                            rs.getInt("codigoEstado"),
                            rs.getString("descripcionEstado")
                    );

                    return Optional.of(st);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando estado de maquinaria por ID", ex);
        }

        return Optional.empty();
    }
}
