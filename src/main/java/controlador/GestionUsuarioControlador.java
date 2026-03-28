/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.RolDao;
import dao.UsuarioDao;
import daoImpl.RolDaoImpl;
import daoImpl.UsuarioDaoImpl;
import modelo.Rol;
import modelo.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Controlador encargado de gestionar los usuarios.
 * Proporciona metodos para crear, actualizar, eliminar y buscar usuarios, asi como validar los datos y generar contrasenas seguras.
 * @author Thanya
 */
public class GestionUsuarioControlador {

    // DAOs para acceder a la base de datos
    private UsuarioDao usuarioDaoImpl = new UsuarioDaoImpl(DataSourceFactory.getDataSource());
    private RolDao rolDaoImpl = new RolDaoImpl(DataSourceFactory.getDataSource());

    // Objetos de trabajo
    private Usuario usuario;
    private Rol rol;

    //GETTERS Y SETTERS
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    //METODOS CRUD

    /**
     * Metodo para crear un nuevo usuario en la base de datos
     * Primero valida los datos y luego inserta el usuario
     */
    public boolean crearUsuario(String nombre, String apellido, String descripcionRol, String telefono, String email, String password) {

        int intentos = 0;
        boolean activo = true;

        // Validamos los datos antes de insertar
        boolean datosValidos = validarDatos(telefono, email, password);
        if (!datosValidos) {
            System.out.println("Algun dato esta mal");
            return false;
        }

        // Recuperamos el rol desde la base de datos
        Rol rol = rolDaoImpl.recuperarRolPorCodigo(descripcionRol);

        if (rol != null) {
            try {
                // Creamos el usuario con los datos recibidos
                Usuario usuario = new Usuario(nombre, apellido, rol, telefono, email, password, intentos, LocalDateTime.now(), activo);

                // Insertamos en la base de datos
                usuarioDaoImpl.insertarUsuario(usuario);
                return true;

            } catch (RuntimeException e) {
                System.out.println("Error insertando usuario: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Error Rol no encontrado");
            return false;
        }
    }

    /**
     * Metodo para actualizar los datos de un usuario existente
     */
    public boolean actualizarDatosUsuario(String nombre, String apellido, String descripcionRol, String telefono, String email, String password, Boolean activo) {

        // Validamos los datos
        boolean datosValidos = validarDatos(telefono, email, password);
        if (!datosValidos) {
            System.out.println("Algun dato esta mal");
            return false;
        }

        // Recuperamos el rol actualizado
        Rol rol = rolDaoImpl.recuperarRolPorCodigo(descripcionRol);

        if (rol != null) {
            try {
                // Creamos el usuario actualizado manteniendo el codigo
                Usuario usuarioActualizado = new Usuario(nombre, apellido, rol, telefono, email, password, this.usuario.getIntentos(), LocalDateTime.now(), activo);

                usuarioActualizado.setCodigoUsuario(usuario.getCodigoUsuario());

                // Actualizamos en base de datos
                usuarioDaoImpl.actualizarUsuario(usuarioActualizado);
                return true;

            } catch (RuntimeException e) {
                System.out.println("Error insertando usuario: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Error Rol no encontrado");
            return false;
        }
    }

    /**
     * Metodo para eliminar un usuario por su codigo
     */
    public void eliminarUsuario(int codigoUsuario) {
        usuarioDaoImpl.eliminarUsuario(codigoUsuario);
    }

    /**
     * Metodo para obtener todos los usuarios de la base de datos
     */
    public List<Usuario> recuperarUsuarios() {
        return usuarioDaoImpl.listarUsuarios();
    }

    /**
     * Metodo para buscar usuarios por filtros
     */
    public List<Usuario> buscarUsuario(Integer codigoUsuario, String nombre, String apellido, Rol codigoRolFK, String email, Boolean activo) {
        return usuarioDaoImpl.buscarPorFiltrosUsuario(codigoUsuario, nombre, apellido, codigoRolFK, email, activo);
    }

    /**
     * METODOS AUXILIARES
     * Se utilizan para validar los datos antes de crear o actualizar
     */
    private boolean validarDatos(String telefono, String email, String password) {

        if (!telefonoValido(telefono)) {
            System.out.println("el telefono esta mal");
            return false;
        }

        if (!emailValido(email)) {
            System.out.println("el email esta amal");
            return false;
        }

        if (!passwordValida(password)) {
            System.out.println("la password esta mal");
            return false;
        }

        return true;
    }

    /**
     * Metodo para validar telefono con expresion regular
     */
    private boolean telefonoValido(String telefono) {
        if (telefono == null) {
            return false;
        }

        String telefonoLimpio = telefono.replaceAll("[\\s\\-()]", "");
        String regex = "^\\+\\d{8,15}$";

        return Pattern.matches(regex, telefonoLimpio);
    }

    /**
     * Metodo para validar email
     */
    private boolean emailValido(String email) {
        if (email == null) {
            return false;
        }

        String emailLimpio = email.trim();
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return Pattern.matches(regex, emailLimpio);
    }

    /**
     * Metodo para validar password segura
     */
    private boolean passwordValida(String password) {

        if (password == null) {
            return false;
        }

        String passwordLimpia = password.trim();
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";

        return passwordLimpia.matches(regex);
    }

    /**
     * Metodo para recuperar todos los roles
     * Anade un rol por defecto al inicio
     */
    public List<Rol> recuperarListadoRoles() {

        List<Rol> listaRoles = rolDaoImpl.listarRoles();

        Rol rolInicial = new Rol();
        rolInicial.setCodigoRol(0);
        rolInicial.setDescripcionRol("Trabajador");

        listaRoles.add(0, rolInicial);

        return listaRoles;
    }

    /**
     * Metodo para buscar usuarios por texto
     */
    public List<Usuario> buscarPorTexto(String texto) {
        return usuarioDaoImpl.buscarPorTexto(texto);
    }

    /**
     * Metodo para generar contrasenas aleatorias seguras
     */
    public String generarPasswordAleatoria() {

        String minusculas = "abcdefghijklmnopqrstuvwxyz";
        String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numeros = "0123456789";
        String especiales = ".@#$%&*!?_+-";
        String todosCaracteres = minusculas + mayusculas + numeros + especiales;

        Random random = new Random();
        StringBuilder passwordGenerada = new StringBuilder();

        // Anadimos minimo un caracter de cada tipo
        passwordGenerada.append(minusculas.charAt(random.nextInt(minusculas.length())));
        passwordGenerada.append(mayusculas.charAt(random.nextInt(mayusculas.length())));
        passwordGenerada.append(numeros.charAt(random.nextInt(numeros.length())));
        passwordGenerada.append(especiales.charAt(random.nextInt(especiales.length())));

        // Completamos hasta longitud minima
        while (passwordGenerada.length() < 8) {
            passwordGenerada.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }

        return mezclarCaracteres(passwordGenerada.toString());
    }

    /**
     * Metodo para mezclar los caracteres de una cadena
     */
    private String mezclarCaracteres(String texto) {

        Random random = new Random();
        char[] arrayCaracteres = texto.toCharArray();

        for (int i = 0; i < arrayCaracteres.length; i++) {

            int indiceAleatorio = random.nextInt(arrayCaracteres.length);

            char caracterAuxiliar = arrayCaracteres[i];
            arrayCaracteres[i] = arrayCaracteres[indiceAleatorio];
            arrayCaracteres[indiceAleatorio] = caracterAuxiliar;
        }

        return new String(arrayCaracteres);
    }

    /**
     * Metodo para obtener el nombre del usuario logueado
     */
    public String obtenerNombreUsuarioLogueado() {

        LoginControlador loginControlador = new LoginControlador();
        Usuario usuarioSesion = loginControlador.getUsuarioSesion();

        if (usuarioSesion != null) {
            return usuarioSesion.getNombre();
        }

        return "NO hay usuario";
    }

    /**
     * Metodo para actualizar la password de un usuario
     */
    public boolean passwordActualizada(String email, String nuevaPassword) {

        String passwordActual = usuarioDaoImpl.actualizarPassword(email, nuevaPassword);

        return passwordActual != null && !passwordActual.isEmpty();
    }
}