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
            ps.setInt(3, usuario.getCodigoRolFK().getCodigoRol());
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
            ps.setInt(3, usuario.getCodigoRolFK().getCodigoRol());
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

        final String sql = "SELECT codigoUsuario, nombre, apellido, codigoRolFK, telefono, email, password, intentos, ultimoAcceso, activo FROM usuario";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setCodigoUsuario(rs.getInt("codigoUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                Rol rol = new Rol();
                rol.setCodigoRol(rs.getInt("codigoRolFK"));
                usuario.setCodigoRolFK(rol);
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
    public List<Usuario> buscarPorFiltros(Integer codigoUsuario, String nombre, String apellido, Rol codigoRolFK, String email) {

        List<Usuario> listaUsuarios = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT codigoUsuario, nombre, apellido, codigoRolFK, telefono, email, password, intentos, ultimoAcceso, activo ");
        sql.append("FROM usuario ");
        sql.append("WHERE 1=1 ");

        List<Object> parametros = new ArrayList<>();

        if (codigoUsuario != null) {
            sql.append("AND codigoUsuario = ? ");
            parametros.add(codigoUsuario);
        }

        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append("AND nombre LIKE ? ");
            parametros.add("%" + nombre.trim() + "%");
        }

        if (apellido != null && !apellido.trim().isEmpty()) {
            sql.append("AND apellido LIKE ? ");
            parametros.add("%" + apellido.trim() + "%");
        }

        if (codigoRolFK != null) {
            sql.append("AND codigoRolFK = ? ");
            parametros.add(codigoRolFK);
        }

        if (email != null) {
            sql.append("AND email = ? ");
            parametros.add(email);
        }

        sql.append("ORDER BY codigoUsuario ASC");

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            /* Asignamos los parametros en el mismo orden en el que los hemos anadido */
            for (int i = 0; i < parametros.size(); i++) {
                Object valor = parametros.get(i);
                int posicion = i + 1;

                if (valor instanceof Integer) {
                    ps.setInt(posicion, (Integer) valor);
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
                    Rol rol = new Rol();
                    rol.setCodigoRol(rs.getInt("codigoRolFK"));
                    usuario.setCodigoRolFK(rol);
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
                usuario.setCodigoRolFK(rol);
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
    public String actualizarPassword(String email) {

        final String sql = "UPDATE usuario SET password = ? WHERE email = ?";

        String nuevaPassword = generarContrasena(8);

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

    private String generarContrasena(int longitud) {

        String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minusculas = "abcdefghijklmnopqrstuvwxyz";
        String numeros = "0123456789";
        String especiales = "!@#$%&*?";

        // juntamos todo para rellenar el resto
        String todos = mayusculas + minusculas + numeros + especiales;

        Random random = new Random();

        char[] password = new char[longitud];

        // aseguramos reglas minimas
        password[0] = mayusculas.charAt(random.nextInt(mayusculas.length()));
        password[1] = minusculas.charAt(random.nextInt(minusculas.length()));
        password[2] = numeros.charAt(random.nextInt(numeros.length()));
        password[3] = especiales.charAt(random.nextInt(especiales.length()));

        // rellenamos el resto con mezcla
        for (int i = 4; i < longitud; i++) {
            password[i] = todos.charAt(random.nextInt(todos.length()));
        }

        // mezclamos para que no siempre sea: mayus, minus, num, esp...
        for (int i = password.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char aux = password[i];
            password[i] = password[j];
            password[j] = aux;
        }

        return new String(password);
    }

}
