/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.TipoAveriaDao;
import modelo.TipoAveria;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Constructor que recibe un DataSource para gestionar las conexiones a la base de datos.
     *
     * @param dataSource El DataSource para obtener conexiones.
     */
    public TipoAveriaDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Verifica si existe un tipo de averia con el ID especificado.
     *
     * @param id El ID del tipo de averia a verificar.
     * @return true si existe, false en caso contrario.
     */
    @Override
    public boolean existeId(int id) {
        String sql = "SELECT 1 FROM tipo_averia WHERE codigoTipoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al buscar el tipo de averia con ID: " + id, ex);
            return false;
        }
    }

    /**
     * Inserta un nuevo tipo de averia en la base de datos.
     * Verifica que no exista un tipo de averia con el mismo ID antes de insertar.
     *
     * @param t El objeto TipoAveria a insertar.
     */
    @Override
    public void insertar(TipoAveria t) {
        if (existeId(t.getCodigoTipoAveria())) {
            System.err.println("ERROR: Ya existe un Tipo de Averia con el ID " + t.getCodigoTipoAveria());
            return;
        }

        String sql = "INSERT INTO tipo_averia (codigoTipoAveria, descripcionTipoAv, tiempoPromRepar) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getCodigoTipoAveria());
            ps.setString(2, t.getDescripcionTipoAv());
            ps.setFloat(3, t.getTiempoPromRepar());

            ps.executeUpdate();
            System.out.println("Insertado correctamente: " + t.getDescripcionTipoAv());

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al insertar el tipo de averia: " + t.getDescripcionTipoAv(), ex);
        }
    }

    /**
     * Actualiza un tipo de averia existente en la base de datos.
     * Verifica que exista un tipo de averia con el ID especificado antes de actualizar.
     *
     * @param t El objeto TipoAveria con los datos actualizados.
     */
    @Override
    public void actualizar(TipoAveria t) {
        if (!existeId(t.getCodigoTipoAveria())) {
            System.err.println("ERROR: No se puede actualizar. No existe el ID " + t.getCodigoTipoAveria());
            return;
        }

        String sql = "UPDATE tipo_averia SET descripcionTipoAv = ?, tiempoPromRepar = ? WHERE codigoTipoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getDescripcionTipoAv());
            ps.setFloat(2, t.getTiempoPromRepar());
            ps.setInt(3, t.getCodigoTipoAveria());

            ps.executeUpdate();
            System.out.println("Actualizado correctamente.");

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al actualizar el tipo de averia con ID: " + t.getCodigoTipoAveria(), ex);
        }
    }

    /**
     * Lista todos los tipos de averia disponibles en la base de datos.
     *
     * @return Una lista de objetos TipoAveria.
     */
    @Override
    public List<TipoAveria> listar() {
        List<TipoAveria> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_averia";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoAveria t = new TipoAveria();

                t.setCodigoTipoAveria(rs.getInt("codigoTipoAveria"));
                t.setDescripcionTipoAv(rs.getString("descripcionTipoAv"));
                t.setTiempoPromRepar(rs.getFloat("tiempoPromRepar"));

                lista.add(t);
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al listar el catalogo de tipos de averia.", ex);
        }

        return lista;
    }

    /**
     * Elimina un tipo de averia por su ID.
     * Verifica que exista un tipo de averia con el ID especificado antes de eliminar.
     * Controla las excepciones SQL para manejar casos de integridad referencial.
     *
     * @param id El ID del tipo de averia a eliminar.
     * @return true si se eliminó correctamente, false en caso de error o si no existe el ID.
     */
    @Override
    public boolean eliminar(int id) {
        if (!existeId(id)) {
            System.err.println("No se puede eliminar: El ID " + id + " no existe.");
            return false;
        }

        String sql = "DELETE FROM tipo_averia WHERE codigoTipoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Tipo de averia eliminado correctamente (ID: " + id + ")");
            return true;

        } catch (SQLException ex) {
            if (ex.getSQLState().startsWith("23")) {
                logger.log(Level.WARNING, "Intento fallido de eliminar el tipo ID " + id + " (tiene registros vinculados).", ex);
            } else {
                logger.log(Level.SEVERE, "Error critico al eliminar el tipo de averia ID: " + id, ex);
            }
            return false;
        }
    }

    /**
     * Busca el ID de un tipo de averia por su descripción.
     * Realiza una búsqueda insensible a mayúsculas y permite coincidencias parciales.
     *
     * @param descripcion La descripción del tipo de averia a buscar.
     * @return El ID del tipo de averia si se encuentra, o -1 si no se encuentra.
     */
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
