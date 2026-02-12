/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.AveriaDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modelo.Averia;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yosnavmol
 */
public class AveriaDaoImpl implements AveriaDao {
    
    private static final Logger logger = Logger.getLogger(AveriaDaoImpl.class.getName());
    
    private final DataSource dataSource;

    public AveriaDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insertar(Averia a) {
        // 1. Definimos la SQL con placeholders (?) para todos los campos
        // Nota: No incluimos 'codigoAveria' porque es AUTO_INCREMENT en la base de datos.
        String sql = "INSERT INTO averia (" +
                     "desc_inic_averia, " +
                     "fecha_inicio_aver, " +
                     "fecha_asig_tecnico, " +
                     "fecha_acep_tecnico, " +
                     "fecha_finaliz_tecnico, " +
                     "proc_realizado_tecnico, " +
                     "usuario_reporta_fk, " +
                     "usuario_tecnico_fk, " +
                     "maquinaria_fk, " +
                     "tipo_averia_fk) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 2. Rellenamos los datos (Bind parameters)

            // Texto simple
            ps.setString(1, a.getDescInicAveria());

            // Fechas (LocalDateTime) -> java.sql.Timestamp
            // Usamos setObject para que sea compatible con null si la fecha no existe
            ps.setObject(2, a.getFechaInicioAver()); 
            ps.setObject(3, a.getFechaAsigTecnico());
            ps.setObject(4, a.getFechaAcepTecnico());
            ps.setObject(5, a.getFechaFinalizTecnico());

            // Texto (puede ser null)
            ps.setString(6, a.getProcRealizadoTecnico());

            // Enteros obligatorios (int primitivo)
            ps.setInt(7, a.getUsuarioReportaFK());

            // Entero Opcional (Integer wrapper) - ¡CASO IMPORTANTE!
            // Si usas setInt con un null, Java lanza error. 
            // Usamos setObject indicando que es de tipo INTEGER para que acepte nulls.
            ps.setObject(8, a.getUsuarioTecnicoFK(), java.sql.Types.INTEGER);

            // Enteros obligatorios
            ps.setInt(9, a.getMaquinariaFK());
            ps.setInt(10, a.getTipoAveriaFK());

            // 3. Ejecutamos
            ps.executeUpdate();
            System.out.println("Avería insertada correctamente.");

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al insertar la avería: " + a.getDescInicAveria(), ex);
        }
    }

    @Override
    public void actualizar(Averia a) {
        // 1. SQL: Actualizamos todo EXCEPTO el ID, que usamos para buscar la fila
        String sql = "UPDATE averia SET " +
                     "descInicAveria = ?, " +       // 1
                     "fechaInicioAver = ?, " +      // 2
                     "fechaAsigTecnico = ?, " +     // 3
                     "fechaAcepTecnico = ?, " +     // 4
                     "fechaFinalizTecnico = ?, " +  // 5
                     "procRealizadoTecnico = ?, " + // 6
                     "usuarioReportaFK = ?, " +     // 7
                     "usuarioTecnicoFK = ?, " +     // 8
                     "maquinariaFK = ?, " +         // 9
                     "tipoAveriaFK = ? " +          // 10
                     "WHERE codigoAveria = ?";      // 11 (La condición)

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // --- ASIGNACIÓN DE VALORES ---

            // 1. Texto
            ps.setString(1, a.getDescInicAveria());

            // 2, 3, 4, 5. Fechas (LocalDateTime)
            // Usamos setObject para que Java maneje si es fecha o null automáticamente
            ps.setObject(2, a.getFechaInicioAver());
            ps.setObject(3, a.getFechaAsigTecnico());
            ps.setObject(4, a.getFechaAcepTecnico());
            ps.setObject(5, a.getFechaFinalizTecnico());

            // 6. Texto opcional
            ps.setString(6, a.getProcRealizadoTecnico());

            // 7. FK Obligatoria (int)
            ps.setInt(7, a.getUsuarioReportaFK());

            // 8. FK Opcional (Integer) - ¡CUIDADO AQUÍ!
            // Usamos Types.INTEGER para que si es null, inserte NULL en la BD sin explotar
            ps.setObject(8, a.getUsuarioTecnicoFK(), java.sql.Types.INTEGER);

            // 9. FK Obligatoria (int)
            ps.setInt(9, a.getMaquinariaFK());

            // 10. FK Obligatoria (int)
            ps.setInt(10, a.getTipoAveriaFK());

            // 11. EL ID PARA EL WHERE (Es fundamental que sea el último)
            ps.setInt(11, a.getCodigoAveria());

            // --- EJECUCIÓN ---
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Avería actualizada correctamente.");
            } else {
                System.out.println("No se encontró ninguna avería con ID: " + a.getCodigoAveria());
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al actualizar la avería con ID: " + a.getCodigoAveria(), ex);
        }
    }
    
    @Override
    public List<Averia> buscarPorFiltros(Integer idAveria, 
                                        String descripcion, 
                                        LocalDateTime fechaInicio, 
                                        LocalDateTime fechaFin, 
                                        Integer idUsuarioReporta, 
                                        Integer idTecnico, 
                                        Integer idMaquinaria, 
                                        Integer idTipoAveria) {

       // 1. Construcción dinámica de la Query
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

       List<Averia> resultado = new ArrayList<>();

       try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())) {

           // 2. Asignar parámetros
           for (int i = 0; i < params.size(); i++) {
               ps.setObject(i + 1, params.get(i));
           }

           // 3. Ejecutar y Mapear (Todo aquí dentro)
           try (ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {
                   Averia a = new Averia();

                   // IDs y Strings simples
                   a.setCodigoAveria(rs.getInt("codigoAveria"));
                   a.setDescInicAveria(rs.getString("descInicAveria"));
                   a.setProcRealizadoTecnico(rs.getString("procRealizadoTecnico"));
                   a.setUsuarioReportaFK(rs.getInt("usuarioReportaFK"));
                   a.setMaquinariaFK(rs.getInt("maquinariaFK"));
                   a.setTipoAveriaFK(rs.getInt("tipoAveriaFK"));

                   // Manejo manual del Integer Nullable (Técnico)
                   int idTec = rs.getInt("usuarioTecnicoFK");
                   if (rs.wasNull()) {
                       a.setUsuarioTecnicoFK(null);
                   } else {
                       a.setUsuarioTecnicoFK(idTec);
                   }

                   // Manejo manual de Fechas (Timestamp -> LocalDateTime)
                   java.sql.Timestamp ts1 = rs.getTimestamp("fechaInicioAver");
                   if (ts1 != null) a.setFechaInicioAver(ts1.toLocalDateTime());

                   java.sql.Timestamp ts2 = rs.getTimestamp("fechaAsigTecnico");
                   if (ts2 != null) a.setFechaAsigTecnico(ts2.toLocalDateTime());

                   java.sql.Timestamp ts3 = rs.getTimestamp("fechaAcepTecnico");
                   if (ts3 != null) a.setFechaAcepTecnico(ts3.toLocalDateTime());

                   java.sql.Timestamp ts4 = rs.getTimestamp("fechaFinalizTecnico");
                   if (ts4 != null) a.setFechaFinalizTecnico(ts4.toLocalDateTime());

                   // Añadimos a la lista
                   resultado.add(a);
               }
           }
       } catch (SQLException ex) {
           logger.log(Level.SEVERE, "Error al consultar la base de datos con los filtros proporcionados.", ex);
       }

       return resultado;
   }

    @Override
    public List<Averia> listar() {
        return buscarPorFiltros(null, null, null, null, null, null, null, null);
    }

    @Override
    public void eliminar(int id) {
        // 1. La Query es simple: Borra de la tabla DONDE el id coincida
        String sql = "DELETE FROM averia WHERE codigoAveria = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 2. Asignamos el ID al interrogante
            ps.setInt(1, id);

            // 3. Ejecutamos
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Avería con ID " + id + " eliminada correctamente.");
            } else {
                System.out.println("No se pudo eliminar: No existe ninguna avería con ID " + id);
            }

        } catch (SQLException ex) {
            // OJO: Este error es muy común al borrar
            if (ex.getSQLState().startsWith("23")) { 
                logger.log(Level.WARNING, "No se puede eliminar la avería ID " + id + " porque tiene datos relacionados.", ex);
            } else {
                logger.log(Level.SEVERE, "Error crítico al eliminar la avería ID: " + id, ex);
            }
        }
    }
}
