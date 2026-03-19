/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.TipoAveriaDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.TipoAveria;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Las tablas maestras no deben modificarse, o al menos no a menudo, pero, como
 * pretendemos que esta aplicación sea escalable, debemos pensar en qué pasaría
 * si en un tiempo el taller cliente necesita añadir algún tipo más al catálogo.
 * Por ello, vamos a crear las funciones de añadir, modificar y eliminar, aunque
 * controlaremos mediante la interfaz el acceso a estas para que sea limitado.
 *
 * @author yosnavmol
 */
public class TipoAveriaDaoImpl implements TipoAveriaDao {

    private static final Logger logger = Logger.getLogger(TipoAveriaDaoImpl.class.getName());

    private final DataSource dataSource;

    public TipoAveriaDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean existeId(int id) {
        // Consulta optimizada: No trae datos pesados, solo un '1' si encuentra la fila.
        String sql = "SELECT 1 FROM tipo_averia WHERE codigoTipoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                // Si rs.next() es true, significa que encontró al menos una fila.
                return rs.next();
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al buscar el tipo de avería con ID: " + id, ex);
            return false; // Ante la duda o error, asumimos false (o lanzamos excepción)
        }
    }

    @Override
    public void insertar(TipoAveria t) {
        // PASO 1: Verificación previa
        if (existeId(t.getCodigoTipoAveria())) {
            System.err.println("ERROR: Ya existe un Tipo de Avería con el ID " + t.getCodigoTipoAveria());
            return; // Salimos del método para no intentar insertar
        }

        // PASO 2: La Query (Ahora incluimos el ID explícitamente)
        // Fíjate que ahora hay 3 interrogantes (?, ?, ?)
        String sql = "INSERT INTO tipo_averia (codigoTipoAveria, descripcionTipoAv, tiempoPromRepar) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Asignamos los 3 valores
            ps.setInt(1, t.getCodigoTipoAveria());    // ID Manual
            ps.setString(2, t.getDescripcionTipoAv());
            ps.setFloat(3, t.getTiempoPromRepar());

            ps.executeUpdate();
            System.out.println("Insertado correctamente: " + t.getDescripcionTipoAv());

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al insertar el tipo de avería: " + t.getDescripcionTipoAv(), ex);
        }
    }

    @Override
    public void actualizar(TipoAveria t) {
        // Verificación de seguridad
        if (!existeId(t.getCodigoTipoAveria())) {
            System.err.println("ERROR: No se puede actualizar. No existe el ID " + t.getCodigoTipoAveria());
            return;
        }

        String sql = "UPDATE tipo_averia SET descripcionTipoAv = ?, tiempoPromRepar = ? WHERE codigoTipoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getDescripcionTipoAv());
            ps.setFloat(2, t.getTiempoPromRepar());
            ps.setInt(3, t.getCodigoTipoAveria()); // El ID va al final para el WHERE

            ps.executeUpdate();
            System.out.println("Actualizado correctamente.");

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al actualizar el tipo de avería con ID: " + t.getCodigoTipoAveria(), ex);
        }
    }

    @Override
    public List<TipoAveria> listar() {
        List<TipoAveria> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_averia";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoAveria t = new TipoAveria();

                // Mapeo exacto según tus columnas de la imagen
                t.setCodigoTipoAveria(rs.getInt("codigoTipoAveria"));
                t.setDescripcionTipoAv(rs.getString("descripcionTipoAv"));

                // OJO: En la imagen se ve que es tipo 'float', así que usamos getFloat
                t.setTiempoPromRepar(rs.getFloat("tiempoPromRepar"));

                lista.add(t);
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al listar el catálogo de tipos de avería.", ex);
        }

        return lista;
    }

    @Override
    public boolean eliminar(int id) {
        if (!existeId(id)) {
            System.err.println("No se puede eliminar: El ID " + id + " no existe.");
            return false; // <-- Falla porque no existe
        }

        String sql = "DELETE FROM tipo_averia WHERE codigoTipoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Tipo de avería eliminado correctamente (ID: " + id + ")");
            return true; // <-- ÉXITO

        } catch (SQLException ex) {
            if (ex.getSQLState().startsWith("23")) {
                logger.log(Level.WARNING, "Intento fallido de eliminar el tipo ID " + id + " (tiene registros vinculados).", ex);
            } else {
                logger.log(Level.SEVERE, "Error crítico al eliminar el tipo de avería ID: " + id, ex);
            }
            return false; // <-- Falla por tener datos asociados u otro error
        }
    }

    @Override
    public int buscarTipoAveriaPorDescripcion(String descripcion) {

        int codigoTipoAveria = -1;

        String sql = "SELECT codigoTipoAveria "
                + "FROM tipo_averia "
                + "WHERE LOWER(descripcionTipoAv) LIKE LOWER(?)";

        try (Connection conexion = dataSource.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + descripcion + "%");

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    codigoTipoAveria = rs.getInt("codigoTipoAveria");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo tipo de averia por descripcion", e);
        }

        return codigoTipoAveria;
    }

}
