/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.TipoMaquinariaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modelo.TipoMaquinaria;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class TipoMaquinariaDAOimpl implements TipoMaquinariaDAO{
    private DataSource dataSource;

    public TipoMaquinariaDAOimpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    

     /**
     * Convierte IDs "cortos" (1..99) a IDs reales (301..399) sumando 300.
     * Si ya viene un ID 300..399, lo deja tal cual.
     * Si viene otro valor, lanza excepción para evitar datos inválidos.
     */
    private int normalizarId300(int id) {
        if (id >= 1 && id <= 99) return 300 + id;     // 1..99 -> 301..399
        if (id >= 300 && id <= 399) return id;        // ya válido
        throw new IllegalArgumentException(
                "El código de TipoMaquinaria debe ser 1..99 (se convertirá a 300+X) o 300..399. Recibido: " + id
        );
    }

    @Override
    public void insertar(TipoMaquinaria t) {
        final int id = normalizarId300(t.getCodigoTipoMaquinaria());
        t.setCodigoTipoMaquinaria(id);

        // Comprobar existencia antes de insertar
        if (existeID(id)) {
            throw new RuntimeException("No se puede insertar: ya existe un TipoMaquinaria con ID " + id);
        }

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
        final int id = normalizarId300(t.getCodigoTipoMaquinaria());
        t.setCodigoTipoMaquinaria(id);

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
        final int id = normalizarId300(t.getCodigoTipoMaquinaria());
        t.setCodigoTipoMaquinaria(id);

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
        final int normalizado = normalizarId300(id);
        final String sql = "SELECT 1 FROM tipo_maquinaria WHERE codigoTipoMaquinaria = ? LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, normalizado);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error comprobando existencia de ID en tipo_maquinaria", ex);
        }
    }
    
}
