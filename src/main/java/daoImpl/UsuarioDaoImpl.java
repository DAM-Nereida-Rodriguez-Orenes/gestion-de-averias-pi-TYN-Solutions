/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;

import dao.UsuarioDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import modelo.Usuario;
import java.time.LocalDateTime;
import java.util.Random;
import modelo.Rol;

/**
 *
 * @author Thanya
 */
public class UsuarioDaoImpl implements UsuarioDao {

    //Atributos
    private final DataSource dataSource;

    //Constructor
    public UsuarioDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insertarUsuario(Usuario usuario) {

        final String sql = "INSERT INTO usuario (nombre, apellido, codigoRolFK, telefono, email, password, intentos, ultimoAcceso, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setInt(3, usuario.getRol().getCodigoRol());
            ps.setString(4, usuario.getTelefono());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getPassword());
            ps.setInt(7, usuario.getIntentos());

            if (usuario.getUltimoAcceso() == null) {
                ps.setNull(8, Types.TIMESTAMP);
            } else {
                Timestamp ultimoAcceso = Timestamp.valueOf(usuario.getUltimoAcceso());
                ps.setTimestamp(8, ultimoAcceso);
            }

            ps.setBoolean(9, usuario.isActivo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setCodigoUsuario(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error insertando usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {

        final String sql = "UPDATE usuario SET nombre = ?, apellido = ?, codigoRolFK = ?, telefono = ?, email = ?, password = ?, intentos = ?, ultimoAcceso = ?, activo = ? "
                + "WHERE codigoUsuario = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setInt(3, usuario.getRol().getCodigoRol());
            ps.setString(4, usuario.getTelefono());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getPassword());
            ps.setInt(7, usuario.getIntentos());

            if (usuario.getUltimoAcceso() == null) {
                ps.setNull(8, Types.TIMESTAMP);
            } else {
                Timestamp ultimoAcceso = Timestamp.valueOf(usuario.getUltimoAcceso());
                ps.setTimestamp(8, ultimoAcceso);
            }

            ps.setBoolean(9, usuario.isActivo());
            ps.setInt(10, usuario.getCodigoUsuario());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se pudo actualizar el usuario (no existe el codigoUsuario).");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarUsuario(int codigoUsuario) {

        final String sql = "UPDATE  usuario SET activo = 0 WHERE codigoUsuario = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            /*El primer ? que encuentres le pone el valor del codigoUsuario*/
            ps.setInt(1, codigoUsuario);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se pudo eliminar el usuario (no existe el codigoUsuario).");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> listarUsuarios() {

        List<Usuario> listaUsuarios = new ArrayList<>();

        final String sql = "SELECT codigoUsuario, nombre, apellido, rol.codigoRol, descripcionRol, telefono, email, password, intentos, ultimoAcceso, activo FROM usuario "
                + "JOIN rol on codigoRolFK = codigoRol";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setCodigoUsuario(rs.getInt("codigoUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));

                Rol rol = new Rol();
                rol.setCodigoRol(rs.getInt("codigoRol"));
                rol.setDescripcionRol(rs.getString("descripcionRol"));
                usuario.setRol(rol);

                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setIntentos(rs.getInt("intentos"));

                Timestamp ultimoAcceso = rs.getTimestamp("ultimoAcceso");
                if (ultimoAcceso == null) {
                    usuario.setUltimoAcceso(null);
                } else {
                    usuario.setUltimoAcceso(ultimoAcceso.toLocalDateTime());
                }

                usuario.setActivo(rs.getBoolean("activo"));

                listaUsuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        }

        return listaUsuarios;
    }

    @Override
    public List<Usuario> buscarPorFiltrosUsuario(Integer codigoUsuario, String nombre, String apellido, Rol rol, String email, Boolean activo) {

        List<Usuario> listaUsuarios = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT codigoUsuario, nombre, apellido, rol.codigoRol, descripcionRol, telefono, email, password, intentos, ultimoAcceso, activo ");
        sql.append("FROM usuario ");
        sql.append("JOIN rol on codigoRolFK = codigoRol ");
        sql.append("WHERE 1=1 ");

        List<Object> parametros = new ArrayList<>();

        if (codigoUsuario != null) {
            sql.append("AND codigoUsuario = ? ");
            parametros.add(codigoUsuario);
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append("AND nombre LIKE ? ");
            parametros.add(nombre.trim() + "%");
            //"%" + nombre.trim() + "%" <- de esta forma filtra los nombres que contengan x letra, no que empiecen con x letra 
        }

        if (apellido != null && !apellido.trim().isEmpty()) {
            sql.append("AND apellido LIKE ? ");
            parametros.add(apellido.trim() + "%");
            //"%" + apellido.trim() + "%"<- de esta forma filtra los apellidos que contengan x letra, no que empiecen con x letra 
        }

        if (rol != null) {
            sql.append("AND descripcionRol = ? ");
            parametros.add(rol.getDescripcionRol());
        }

        if (email != null) {
            sql.append("AND email = ? ");
            parametros.add(email);
        }

        if (activo != null) {
            sql.append("AND activo = ? ");
            parametros.add(activo);
        }

        sql.append("ORDER BY codigoUsuario ASC ");

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            /* Asignamos los parametros en el mismo orden en el que los hemos anadido */
            for (int i = 0; i < parametros.size(); i++) {
                Object valor = parametros.get(i);
                int posicion = i + 1;

                if (valor instanceof Integer) {
                    ps.setInt(posicion, (Integer) valor);
                } else if (valor instanceof Boolean) {
                    ps.setBoolean(posicion, (Boolean) valor);
                } else {
                    ps.setString(posicion, (String) valor);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setCodigoUsuario(rs.getInt("codigoUsuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));

                    Rol rolRecuperado = new Rol();
                    rolRecuperado.setCodigoRol(rs.getInt("codigoRol"));
                    rolRecuperado.setDescripcionRol(rs.getString("descripcionRol"));
                    usuario.setRol(rolRecuperado);

                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setIntentos(rs.getInt("intentos"));

                    Timestamp ultimoAcceso = rs.getTimestamp("ultimoAcceso");
                    if (ultimoAcceso == null) {
                        usuario.setUltimoAcceso(null);
                    } else {
                        usuario.setUltimoAcceso(ultimoAcceso.toLocalDateTime());
                    }

                    usuario.setActivo(rs.getBoolean("activo"));

                    listaUsuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando usuarios por filtros: " + e.getMessage(), e);
        }

        return listaUsuarios;
    }

    /**
     * Este metodo me sirve para el login.
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    public Usuario buscarPorCredenciales(String email, String password) {

        Usuario usuario = null;

        String sql = "SELECT codigoUsuario, nombre, apellido, codigoRolFK, telefono, email, password, intentos, ultimoAcceso, activo "
                + "FROM usuario "
                + "WHERE email = ? AND password = ? AND activo = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                usuario = new Usuario();

                usuario.setCodigoUsuario(resultSet.getInt("codigoUsuario"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));
                Rol rol = new Rol();
                rol.setCodigoRol(resultSet.getInt("codigoRolFK"));
                usuario.setRol(rol);
                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setIntentos(resultSet.getInt("intentos"));

                // Aqui va la conversion de ultimoAcceso
                Timestamp timestamp = resultSet.getTimestamp("ultimoAcceso");

                if (timestamp != null) {
                    usuario.setUltimoAcceso(timestamp.toLocalDateTime());
                }

                usuario.setActivo(resultSet.getBoolean("activo"));
            }

        } catch (SQLException e) {
            System.out.println("Error en buscarPorCredenciales: " + e.getMessage());
        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }

        return usuario;
    }

    /**
     * Actualiza la contraseña del usuario cuyo email coincide. Si se actualiza
     * 1 fila, devuelve true. Si no existe ese email, devuelve false.
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    public String actualizarPassword(String email, String nuevaPassword) {

        final String sql = "UPDATE usuario SET password = ? WHERE email = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaPassword);
            ps.setString(2, email);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                return nuevaPassword;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando contrasena: " + e.getMessage(), e);
        }
    }

    /**
     * Busca el texto escrito en nombre, apellido o email con LIKE, y ya te
     * devuelve el Rol con su descripcionRol (por el JOIN).
     */
    @Override
    public List<Usuario> buscarPorTexto(String texto) {

        List<Usuario> listaUsuarios = new ArrayList<>();

        final String sql
                = "SELECT u.codigoUsuario, u.nombre, u.apellido, u.telefono, u.email, u.password, "
                + "u.intentos, u.ultimoAcceso, u.activo, u.codigoRolFK, r.descripcionRol "
                + "FROM usuario u "
                + "INNER JOIN rol r ON u.codigoRolFK = r.codigoRol "
                + "WHERE u.nombre LIKE ? OR u.apellido LIKE ? OR u.email LIKE ? "
                + "ORDER BY u.codigoUsuario ASC";

        String patron = "%" + texto + "%";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setCodigoUsuario(rs.getInt("codigoUsuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setIntentos(rs.getInt("intentos"));

                    Timestamp ultimoAcceso = rs.getTimestamp("ultimoAcceso");
                    if (ultimoAcceso == null) {
                        usuario.setUltimoAcceso(null);
                    } else {
                        usuario.setUltimoAcceso(ultimoAcceso.toLocalDateTime());
                    }

                    usuario.setActivo(rs.getBoolean("activo"));

                    Rol rol = new Rol();
                    rol.setCodigoRol(rs.getInt("codigoRolFK"));
                    rol.setDescripcionRol(rs.getString("descripcionRol"));
                    usuario.setRol(rol);

                    listaUsuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando por texto: " + e.getMessage(), e);
        }

        return listaUsuarios;
    }

    public Usuario buscarPorEmail(String email) {

        Usuario usuario = null;

        String sql = "SELECT u.codigoUsuario, u.nombre, u.apellido, u.codigoRolFK, "
                + "r.descripcionRol, u.telefono, u.email, u.password, "
                + "u.intentos, u.ultimoAcceso, u.activo "
                + "FROM usuario u "
                + "INNER JOIN rol r ON u.codigoRolFK = r.codigoRol "
                + "WHERE u.email = ? AND u.activo = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                usuario = new Usuario();

                usuario.setCodigoUsuario(resultSet.getInt("codigoUsuario"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));

                Rol rol = new Rol();
                rol.setCodigoRol(resultSet.getInt("codigoRolFK"));
                rol.setDescripcionRol(resultSet.getString("descripcionRol"));
                usuario.setRol(rol);

                usuario.setTelefono(resultSet.getString("telefono"));
                usuario.setEmail(resultSet.getString("email"));
                usuario.setPassword(resultSet.getString("password"));
                usuario.setIntentos(resultSet.getInt("intentos"));

                Timestamp timestamp = resultSet.getTimestamp("ultimoAcceso");

                if (timestamp != null) {
                    usuario.setUltimoAcceso(timestamp.toLocalDateTime());
                }

                usuario.setActivo(resultSet.getBoolean("activo"));
            }

        } catch (SQLException e) {
            System.out.println("Error en buscarPorEmail: " + e.getMessage());
        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }

        return usuario;
    }

    @Override
    public List<Usuario> buscarTecnicosOrdenadorPorCarga(int codigoTipoAveria) {

        List<Usuario> listaUsuarios = new ArrayList<>();

        String sql = "SELECT u.codigoUsuario, u.nombre, u.apellido "
                + "FROM usuario u "
                + "LEFT JOIN ( "
                + "    SELECT a.usuarioTecnicoFK, COUNT(*) AS totalAveriasActivas "
                + "    FROM averia a "
                + "    WHERE a.fechaAcepTecnico IS NOT NULL "
                + "      AND a.fechaFinalizTecnico IS NULL "
                + "    GROUP BY a.usuarioTecnicoFK "
                + ") act ON act.usuarioTecnicoFK = u.codigoUsuario "
                + "LEFT JOIN ( "
                + "    SELECT a.usuarioTecnicoFK, a.tipoAveriaFK, "
                + "           COUNT(*) AS totalAveriasFinalizadas, "
                + "           AVG(TIMESTAMPDIFF(MINUTE, a.fechaAcepTecnico, a.fechaFinalizTecnico) / 60.0) AS tiempoMedioTecnico "
                + "    FROM averia a "
                + "    WHERE a.fechaAcepTecnico IS NOT NULL "
                + "      AND a.fechaFinalizTecnico IS NOT NULL "
                + "      AND a.tipoAveriaFK = ? "
                + "    GROUP BY a.usuarioTecnicoFK, a.tipoAveriaFK "
                + ") hist ON hist.usuarioTecnicoFK = u.codigoUsuario "
                + "INNER JOIN tipo_averia ta "
                + "    ON ta.codigoTipoAveria = ? "
                + "WHERE u.codigoRolFK = 703 "
                + "  AND u.activo = 1 "
                + "ORDER BY "
                + "    CASE "
                + "        WHEN COALESCE(hist.tiempoMedioTecnico, 0) > 0 THEN 0 "
                + "        ELSE 1 "
                + "    END ASC, "
                + "    COALESCE(act.totalAveriasActivas, 0) ASC, "
                + "    CASE "
                + "        WHEN COALESCE(hist.tiempoMedioTecnico, 0) > 0 THEN hist.tiempoMedioTecnico "
                + "        ELSE 999999 "
                + "    END ASC";

        try (Connection conexion = dataSource.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, codigoTipoAveria);
            ps.setInt(2, codigoTipoAveria);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setCodigoUsuario(rs.getInt("codigoUsuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));

                    listaUsuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo tecnicos ordenados por carga y experiencia", e);
        }

        return listaUsuarios;
    }

    @Override
    public Object[] obtenerMotivosTecnico(int codigoTecnico, int codigoTipoAveria) {

        String sql = "SELECT "
                + "    COALESCE(act.totalAveriasActivas, 0) AS totalAveriasActivas, "
                + "    COALESCE(hist.totalAveriasFinalizadas, 0) AS totalAveriasFinalizadas, "
                + "    COALESCE(hist.tiempoMedioTecnico, 0) AS tiempoMedioTecnico "
                + "FROM usuario u "
                + "LEFT JOIN ( "
                + "    SELECT a.usuarioTecnicoFK, COUNT(*) AS totalAveriasActivas "
                + "    FROM averia a "
                + "    WHERE a.fechaAcepTecnico IS NOT NULL "
                + "      AND a.fechaFinalizTecnico IS NULL "
                + "    GROUP BY a.usuarioTecnicoFK "
                + ") act ON act.usuarioTecnicoFK = u.codigoUsuario "
                + "LEFT JOIN ( "
                + "    SELECT a.usuarioTecnicoFK, "
                + "           COUNT(*) AS totalAveriasFinalizadas, "
                + "           AVG(TIMESTAMPDIFF(MINUTE, a.fechaAcepTecnico, a.fechaFinalizTecnico) / 60.0) AS tiempoMedioTecnico "
                + "    FROM averia a "
                + "    WHERE a.fechaAcepTecnico IS NOT NULL "
                + "      AND a.fechaFinalizTecnico IS NOT NULL "
                + "      AND a.tipoAveriaFK = ? "
                + "    GROUP BY a.usuarioTecnicoFK "
                + ") hist ON hist.usuarioTecnicoFK = u.codigoUsuario "
                + "WHERE u.codigoUsuario = ?";

        try (Connection conexion = dataSource.getConnection(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, codigoTipoAveria);
            ps.setInt(2, codigoTecnico);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Object[] datos = new Object[3];

                    datos[0] = rs.getInt("totalAveriasActivas");
                    datos[1] = rs.getInt("totalAveriasFinalizadas");
                    datos[2] = rs.getDouble("tiempoMedioTecnico");

                    return datos;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo motivos del tecnico", e);
        }

        return null;
    }

}
