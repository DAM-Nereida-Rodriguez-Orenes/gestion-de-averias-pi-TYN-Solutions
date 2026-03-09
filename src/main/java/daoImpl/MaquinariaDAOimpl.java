/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.MaquinariaDAO;
import java.sql.Connection;
import java.sql.Date; //este, no el de util
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
import modelo.Estado;
import modelo.Maquinaria;
import modelo.TipoMaquinaria;
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
        String sql = "INSERT INTO maquinaria (nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1) nombre
            ps.setString(1, m.getNombre());

            // 2) FK estado
            ps.setInt(2, m.getEstado().getCodigoEstado());

            // 3) fechaAlta (DATE)
            //El modelo es java.time.LocalDate:
            ps.setDate(3, Date.valueOf(m.getFechaAlta()));

            // 4) fechaBaja (DATE) puede ser NULL
            if (m.getFechaBaja() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(m.getFechaBaja()));
            }

            // 5) FK tipo_maquinaria
            ps.setInt(5, m.getTipoMaquinaria().getCodigoTipoMaquinaria());
            
            ps.executeUpdate();

            // Recuperar el ID autogenerado --> útil para refrescar vistas de listas
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setCodigoMaquinaria(rs.getInt(1));
                }
            }

        } catch (SQLException ex) {
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

            ps.setString(1, m.getNombre());

            ps.setInt(2, m.getEstado().getCodigoEstado());

            ps.setDate(3, Date.valueOf(m.getFechaAlta()));

            if (m.getFechaBaja() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(m.getFechaBaja()));
            };

            ps.setInt(5, m.getTipoMaquinaria().getCodigoTipoMaquinaria());

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
        List<Maquinaria> lista = new ArrayList<>();
        final String sql = "SELECT * FROM maquinaria";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMaquinaria(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listando maquinaria", e);
        }

        return lista;
    }

   
    @Override
    public Optional<Maquinaria> buscarMaquinariaPorId(Integer id){
        Optional<Maquinaria> maq = Optional.empty();//inicializado a vacío, NO a null
        final String sql = """
        SELECT codigoMaquinaria, nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK
        FROM maquinaria
        WHERE codigoMaquinaria = ?
        """;

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                return Optional.of(mapearMaquinaria(rs));
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return maq;
        }
    }
    @Override
    public List<Maquinaria> buscarMaquinariaPorTexto(String text){ 
        List<Maquinaria> lista = new ArrayList<>();//inicializado a vacío, NO a null
        final String sql = """
        SELECT codigoMaquinaria, nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK
        FROM maquinaria
        WHERE nombre LIKE ?
        """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, "%" + text + "%");

                try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                   lista.add(mapearMaquinaria(rs));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return lista;
    }
    public List<Maquinaria> buscarMaquinariaPorFecha(LocalDate fechaAlta, LocalDate fechaBaja){
        List<Maquinaria> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT codigoMaquinaria, nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK
        FROM maquinaria
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (fechaAlta != null) {
            sql.append(" AND fechaAlta = ?");
            params.add(Date.valueOf(fechaAlta));
        }

        if (fechaBaja != null) {
            sql.append(" AND fechaBaja = ?");
            params.add(Date.valueOf(fechaBaja));
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMaquinaria(rs));
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return lista;
    }
    @Override
    public List<Maquinaria> buscarMaquinariaPorEstado(Integer codigoEstadoFK) {
        List<Maquinaria> lista = new ArrayList<>();

        String sql = """
            SELECT codigoMaquinaria, nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK
            FROM maquinaria
            """;

        if (codigoEstadoFK != null) {
            sql += " WHERE codigoEstadoFK = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (codigoEstadoFK != null) {
                ps.setInt(1, codigoEstadoFK);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMaquinaria(rs));
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return lista;
    }
    @Override
    public List<Maquinaria> buscarMaquinariaPorTipo(Integer tipoMaquinariaFK) {
        List<Maquinaria> lista = new ArrayList<>();

        String sql = """
            SELECT codigoMaquinaria, nombre, codigoEstadoFK, fechaAlta, fechaBaja, tipoMaquinariaFK
            FROM maquinaria
            """;

        if (tipoMaquinariaFK != null) {
            sql += " WHERE tipoMaquinariaFK = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (tipoMaquinariaFK != null) {
                ps.setInt(1, tipoMaquinariaFK);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMaquinaria(rs));
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return lista;
    }
    
    //Métodos auxiliares
    private Maquinaria mapearMaquinaria(ResultSet rs) throws SQLException {
        Maquinaria m = new Maquinaria();

        m.setCodigoMaquinaria(rs.getInt("codigoMaquinaria"));
        m.setNombre(rs.getString("nombre"));

        Date alta = rs.getDate("fechaAlta");
        if (alta != null) {
            m.setFechaAlta(alta.toLocalDate());
        }

        Date baja = rs.getDate("fechaBaja");
        if (baja != null) {
            m.setFechaBaja(baja.toLocalDate());
        }

        Estado e = new Estado();
        e.setCodigoEstado(rs.getInt("codigoEstadoFK"));
        m.setEstado(e);

        TipoMaquinaria t = new TipoMaquinaria();
        t.setCodigoTipoMaquinaria(rs.getInt("tipoMaquinariaFK"));
        m.setTipoMaquinaria(t);

        return m;
    }
}