package daoImpl;

import dao.AveriaDao;
import modelo.Averia;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementacion del DAO para la entidad Averia.
 * Esta clase se encarga de realizar las operaciones CRUD sobre la tabla "averia" en la base de datos.
 * @author yosnavmol
 */
public class AveriaDaoImpl implements AveriaDao {

    private static final Logger logger = Logger.getLogger(AveriaDaoImpl.class.getName());
    private final DataSource dataSource;

    /**
     * Constructor que recibe un DataSource para gestionar las conexiones a la base de datos.
     */
    public AveriaDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inserta una nueva averia en la base de datos.
     * La fecha de inicio se establece al momento de la insercion.
     * Si se asigna un tecnico, tambien se guarda la fecha de asignacion.
     */
    @Override
    public void insertar(Averia a) {
        LocalDateTime ahora = LocalDateTime.now();

        // La fecha de inicio siempre es el momento actual
        a.setFechaInicioAver(ahora);

        // Si hay tecnico asignado tambien se guarda la fecha de asignacion
        if (a.getUsuarioTecnicoFK() != null && a.getUsuarioTecnicoFK() > 0) {
            a.setFechaAsigTecnico(ahora);
        } else {
            a.setUsuarioTecnicoFK(null);
            a.setFechaAsigTecnico(null);
        }

        String sql = "INSERT INTO averia ("
                + "descInicAveria, "
                + "fechaInicioAver, "
                + "fechaAsigTecnico, "
                + "usuarioReportaFK, "
                + "usuarioTecnicoFK, "
                + "maquinariaFK, "
                + "tipoAveriaFK) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getDescInicAveria());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(a.getFechaInicioAver()));

            if (a.getFechaAsigTecnico() != null) {
                ps.setTimestamp(3, java.sql.Timestamp.valueOf(a.getFechaAsigTecnico()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }

            ps.setInt(4, a.getUsuarioReportaFK());

            if (a.getUsuarioTecnicoFK() != null) {
                ps.setInt(5, a.getUsuarioTecnicoFK());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setInt(6, a.getMaquinariaFK());
            ps.setInt(7, a.getTipoAveriaFK());

            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(AveriaDaoImpl.class.getName())
                    .log(Level.SEVERE, "Error al insertar averia", ex);
        }
    }

    /**
     * Actualiza una averia existente en la base de datos.
     * Se actualizan todos los campos excepto el ID, que es el identificador unico.
     */
    @Override
    public void actualizar(Averia a) {
        String sql = "UPDATE averia SET "
                + "descInicAveria = ?, "
                + "fechaInicioAver = ?, "
                + "fechaAsigTecnico = ?, "
                + "fechaAcepTecnico = ?, "
                + "fechaFinalizTecnico = ?, "
                + "procRealizadoTecnico = ?, "
                + "usuarioReportaFK = ?, "
                + "usuarioTecnicoFK = ?, "
                + "maquinariaFK = ?, "
                + "tipoAveriaFK = ? "
                + "WHERE codigoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getDescInicAveria());
            ps.setObject(2, a.getFechaInicioAver());
            ps.setObject(3, a.getFechaAsigTecnico());
            ps.setObject(4, a.getFechaAcepTecnico());
            ps.setObject(5, a.getFechaFinalizTecnico());
            ps.setString(6, a.getProcRealizadoTecnico());
            ps.setInt(7, a.getUsuarioReportaFK());
            ps.setObject(8, a.getUsuarioTecnicoFK(), java.sql.Types.INTEGER);
            ps.setInt(9, a.getMaquinariaFK());
            ps.setInt(10, a.getTipoAveriaFK());
            ps.setInt(11, a.getCodigoAveria());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Averia actualizada correctamente.");
            } else {
                System.out.println("No se encontro ninguna averia con ID: " + a.getCodigoAveria());
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al actualizar la averia con ID: " + a.getCodigoAveria(), ex);
        }
    }

    /**
     * Busca averias aplicando distintos filtros. Todos los filtros son opcionales.
     * Si se proporciona un filtro, se aplica a la consulta. Si no, se ignora.
     * El resultado se ordena por prioridad: primero pendientes, luego en proceso y al final finalizadas.
     */
    @Override
    public List<Averia> buscarPorFiltros(Integer idAveria,
                                         String descripcion,
                                         LocalDateTime fechaInicio,
                                         LocalDateTime fechaFin,
                                         Integer idUsuarioReporta,
                                         Integer idTecnico,
                                         Integer idMaquinaria,
                                         Integer idTipoAveria) {

        StringBuilder sql = new StringBuilder("SELECT * FROM averia WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (idAveria != null) {
            sql.append(" AND codigoAveria = ?");
            params.add(idAveria);
        }
        if (descripcion != null && !descripcion.isEmpty()) {
            sql.append(" AND descInicAveria LIKE ?");
            params.add("%" + descripcion + "%");
        }
        if (fechaInicio != null) {
            sql.append(" AND fechaInicioAver = ?");
            params.add(fechaInicio);
        }
        if (fechaFin != null) {
            sql.append(" AND fechaFinalizTecnico = ?");
            params.add(fechaFin);
        }
        if (idUsuarioReporta != null) {
            sql.append(" AND usuarioReportaFK = ?");
            params.add(idUsuarioReporta);
        }
        if (idTecnico != null) {
            sql.append(" AND usuarioTecnicoFK = ?");
            params.add(idTecnico);
        }
        if (idMaquinaria != null) {
            sql.append(" AND maquinariaFK = ?");
            params.add(idMaquinaria);
        }
        if (idTipoAveria != null) {
            sql.append(" AND tipoAveriaFK = ?");
            params.add(idTipoAveria);
        }

        // Orden de prioridad para mostrar primero pendientes, luego en proceso y al final finalizadas
        sql.append(" ORDER BY "
                + "CASE "
                + "WHEN fechaFinalizTecnico IS NOT NULL THEN 3 "
                + "WHEN fechaAsigTecnico IS NOT NULL THEN 2 "
                + "ELSE 1 "
                + "END ASC, "
                + "fechaInicioAver DESC");

        List<Averia> resultado = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Averia a = new Averia();

                    a.setCodigoAveria(rs.getInt("codigoAveria"));
                    a.setDescInicAveria(rs.getString("descInicAveria"));
                    a.setProcRealizadoTecnico(rs.getString("procRealizadoTecnico"));
                    a.setUsuarioReportaFK(rs.getInt("usuarioReportaFK"));
                    a.setMaquinariaFK(rs.getInt("maquinariaFK"));
                    a.setTipoAveriaFK(rs.getInt("tipoAveriaFK"));

                    int idTec = rs.getInt("usuarioTecnicoFK");
                    if (rs.wasNull()) {
                        a.setUsuarioTecnicoFK(null);
                    } else {
                        a.setUsuarioTecnicoFK(idTec);
                    }

                    java.sql.Timestamp ts1 = rs.getTimestamp("fechaInicioAver");
                    if (ts1 != null) {
                        a.setFechaInicioAver(ts1.toLocalDateTime());
                    }

                    java.sql.Timestamp ts2 = rs.getTimestamp("fechaAsigTecnico");
                    if (ts2 != null) {
                        a.setFechaAsigTecnico(ts2.toLocalDateTime());
                    }

                    java.sql.Timestamp ts3 = rs.getTimestamp("fechaAcepTecnico");
                    if (ts3 != null) {
                        a.setFechaAcepTecnico(ts3.toLocalDateTime());
                    }

                    java.sql.Timestamp ts4 = rs.getTimestamp("fechaFinalizTecnico");
                    if (ts4 != null) {
                        a.setFechaFinalizTecnico(ts4.toLocalDateTime());
                    }

                    resultado.add(a);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al consultar la base de datos con los filtros proporcionados.", ex);
        }

        return resultado;
    }

    /**
     * Elimina una averia por su ID. Si la averia tiene datos relacionados que impiden su eliminacion, se captura la excepcion y se muestra un mensaje de error.
     * @param id ID de la averia a eliminar
     * @return true si se elimino correctamente, false si no se pudo eliminar
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM averia WHERE codigoAveria = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Averia con ID " + id + " eliminada correctamente.");
                return true;
            } else {
                System.out.println("No se pudo eliminar: No existe ninguna averia con ID " + id);
                return false;
            }

        } catch (SQLException ex) {
            if (ex.getSQLState().startsWith("23")) {
                logger.log(Level.WARNING, "No se puede eliminar la averia ID " + id + " porque tiene datos relacionados.", ex);
            } else {
                logger.log(Level.SEVERE, "Error critico al eliminar la averia ID: " + id, ex);
            }
            return false;
        }
    }
}