/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.TipoMaquinariaDAO;
import modelo.TipoMaquinaria;

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
public class TipoMaquinariaDAOimpl implements TipoMaquinariaDAO{
    private final DataSource dataSource;

    public TipoMaquinariaDAOimpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insertar(TipoMaquinaria t) {

        final String sql = "INSERT INTO tipo_maquinaria (codigoTipoMaquinaria, descripcionMaq) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getCodigoTipoMaquinaria());
            ps.setString(2, t.getDescripcionMaq());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error insertando tipo de maquinaria", ex);
        }
    }

    @Override
    public void eliminar(TipoMaquinaria t) {

        final String sql = "DELETE FROM tipo_maquinaria WHERE codigoTipoMaquinaria = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getCodigoTipoMaquinaria());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando tipo de maquinaria", ex);
        }
    }

    @Override
    public void modificar(TipoMaquinaria t) {
        final String sql = "UPDATE tipo_maquinaria SET descripcionMaq = ? WHERE codigoTipoMaquinaria = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getDescripcionMaq());
            ps.setInt(2, t.getCodigoTipoMaquinaria());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error modificando tipo de maquinaria", ex);
        }
    }

    @Override
    public List<TipoMaquinaria> listarTipoMaquinaria() {
        final String sql = "SELECT codigoTipoMaquinaria, descripcionMaq FROM tipo_maquinaria ORDER BY codigoTipoMaquinaria ASC";
        List<TipoMaquinaria> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoMaquinaria t = new TipoMaquinaria();
                t.setCodigoTipoMaquinaria(rs.getInt("codigoTipoMaquinaria"));
                t.setDescripcionMaq(rs.getString("descripcionMaq"));
                lista.add(t);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error listando tipos de maquinaria", ex);
        }

        return lista;
    }

    @Override
    public boolean existeID(int id) {
        final String sql = "SELECT 1 FROM tipo_maquinaria WHERE codigoTipoMaquinaria = ? LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error comprobando existencia de ID en tipo_maquinaria", ex);
        }
    }
    @Override
    public Optional<TipoMaquinaria> buscarPorID(int id) {

        final String sql = """
            SELECT codigoTipoMaquinaria, descripcionMaq
            FROM tipo_maquinaria
            WHERE codigoTipoMaquinaria = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    TipoMaquinaria tm = new TipoMaquinaria(
                            rs.getInt("codigoTipoMaquinaria"),
                            rs.getString("descripcionMaq")
                    );

                    return Optional.of(tm);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando tipo de maquinaria por ID", ex);
        }

        return Optional.empty();
    }
}
