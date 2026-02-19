/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.MaquinariaDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import modelo.Maquinaria;
/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class MaquinariaDAOimpl implements MaquinariaDAO{
    private DataSource dataSource;

    public MaquinariaDAOimpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insertar(Maquinaria m) {
        /*
        INSERT INTO maquinaria (nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK)
        VALUES ("mp", 801, "2026-01-01", null, 305)
        */
        String sql = "INSERT INTO maquinaria (nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK)VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(true);
            
            // 1) nombre
            ps.setString(1, m.getNombre());

            // 2) FK estado
            ps.setInt(2, m.getCodigoEstadoFK());

            // 3) fechaAlta (DATE)
            //modelo es java.time.LocalDate:
            ps.setDate(3, Date.valueOf(m.getFechaAlta()));


            // 4) fechaBaja (DATE) puede ser NULL
            if (m.getFechaBaja() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(m.getFechaBaja()));
            }

            // 5) FK tipo_maquinaria
            ps.setInt(5, m.getTipoMaquinariaFK());
            
            System.out.println("INSERT maquinaria: " + m.getNombre() + ", estado=" + m.getCodigoEstadoFK()
            + ", alta=" + m.getFechaAlta() + ", baja=" + m.getFechaBaja()
            + ", tipo=" + m.getTipoMaquinariaFK());

            ps.executeUpdate();

            // (Opcional pero muy útil) recuperar el ID autogenerado --> útil para refrescar vistas de listas
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setCodigoMaquinaria(rs.getInt(1));
                    System.out.println("Código maquinaria: " + m.getCodigoMaquinaria());
                }
            }
            

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error insertando maquinaria", ex);
        }
    }

    @Override
    public void modificar(Maquinaria m) {
        final String sql = """
            UPDATE maquinaria
            SET nombre = ?,
                codigoEstadoFK = ?,
                fechaAlta = ?,
                fechaBaja = ?,
                tipoMaquinariaFK = ?
            WHERE codigoMaquinaria = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            //1. nombre
            ps.setString(1, m.getNombre());

            // 2. estado FK
            ps.setInt(2, m.getCodigoEstadoFK());

            // 3️. Fecha Alta (Date)
            ps.setDate(3, Date.valueOf(m.getFechaAlta()));

            // 4. fecha Baja (puede ser null)
            if (m.getFechaBaja() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(m.getFechaBaja()));
            }

            // 5️. tipo maquinaria (FK)
            ps.setInt(5, m.getTipoMaquinariaFK());

            // 6️. where
            ps.setInt(6, m.getCodigoMaquinaria());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando maquinaria", e);
        }
    }

    @Override
    public void eliminar(int codigoMaquinaria) {
        final String sql = "DELETE FROM maquinaria WHERE codigoMaquinaria = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // PK máquina a eliminar
            ps.setInt(1, codigoMaquinaria);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando maquinaria", e);
        }
    }

    @Override
    public List<Maquinaria> listarMaquinaria() {
        return buscarPorFiltrosMaquinaria(null, null, null, null, null);
    }

    @Override
    public List<Maquinaria> buscarPorFiltrosMaquinaria( Integer codigoEstadoFK,
        Integer tipoMaquinariaFK,
        LocalDate fechaAltaDesde,
        LocalDate fechaAltaHasta,
        Boolean soloActivas) {
        
        StringBuilder sql = new StringBuilder("SELECT * FROM maquinaria WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (codigoEstadoFK != null) {
            sql.append(" AND codigoEstadoFK = ?");
            params.add(codigoEstadoFK);
        }

        if (tipoMaquinariaFK != null) {
            sql.append(" AND tipoMaquinariaFK = ?");
            params.add(tipoMaquinariaFK);
        }

        if (fechaAltaDesde != null) {
            sql.append(" AND fechaAlta >= ?");
            params.add(java.sql.Date.valueOf(fechaAltaDesde));
        }

        if (fechaAltaHasta != null) {
            sql.append(" AND fechaAlta <= ?");
            params.add(java.sql.Date.valueOf(fechaAltaHasta));
        }

        if (soloActivas != null && soloActivas) {
            sql.append(" AND fechaBaja IS NULL");
        }

        sql.append(" ORDER BY codigoMaquinaria ASC");

        List<Maquinaria> resultado = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Maquinaria m = new Maquinaria();
                    m.setCodigoMaquinaria(rs.getInt("codigoMaquinaria"));
                    m.setNombre(rs.getString("nombre"));
                    m.setCodigoEstadoFK(rs.getInt("codigoEstadoFK"));

                    java.sql.Date fa = rs.getDate("fechaAlta");
                    m.setFechaAlta(fa != null ? fa.toLocalDate() : null);

                    java.sql.Date fb = rs.getDate("fechaBaja");
                    m.setFechaBaja(fb != null ? fb.toLocalDate() : null);

                    m.setTipoMaquinariaFK(rs.getInt("tipoMaquinariaFK"));

                    resultado.add(m);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error filtrando maquinaria", e);
        }

        return resultado;
    }
    
}
