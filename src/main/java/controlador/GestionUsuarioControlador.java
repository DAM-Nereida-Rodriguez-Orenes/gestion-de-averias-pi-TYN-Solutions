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
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import modelo.Rol;
import modelo.Usuario;

/**
 *
 * @author Netri
 */
public class GestionUsuarioControlador {

    private UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl(DataSourceFactory.getDataSource());
    private RolDao rolDaoImpl = new RolDaoImpl(DataSourceFactory.getDataSource());
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
    public boolean crearUsuario(String nombre, String apellido, String descripcionRol, String telefono, String email, String password) {
        // Variables
        int intentos = 0;
        boolean activo = true;

        // llamamos al metodo validar datos telefono, email, password
        boolean datosValidos = validarDatos(telefono, email, password);
        if (!datosValidos) {
            System.out.println("Algun dato esta mal");
            return false;
        }
        System.out.println("Los datos estan bien");

        //estamos recuperandoe el rol de la base de datos
        Rol rol = rolDaoImpl.recuperarRolPorCodigo(descripcionRol);
        if (rol != null) {
            try {
                Usuario usuario = new Usuario(nombre, apellido, rol, telefono, email, password, intentos, LocalDateTime.now(), activo);
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

    public boolean actualizarDatosUsuario(String nombre, String apellido, String descripcionRol, String telefono, String email, String password) {

        Rol rol = rolDaoImpl.recuperarRolPorCodigo(descripcionRol);
        if (rol != null) {
            try {
                //Ahora si le cambiamos sus datos por los que me vienen por parametro 
                Usuario usuarioActualizado = new Usuario(nombre, apellido, rol, telefono, email, password, this.usuario.getIntentos(), LocalDateTime.now(), this.usuario.isActivo());
                usuarioActualizado.setCodigoUsuario(usuario.getCodigoUsuario());
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

    public void eliminarUsuario(int codigoUsuario) {
        usuarioDaoImpl.eliminarUsuario(codigoUsuario);
    }

    /**
     * este metod lo utilizo para rellenar los datos de la tabla sacandolos
     * desde la base de datos
     *
     * @return
     */
    public List<Usuario> recuperarUsuarios() {
        return usuarioDaoImpl.listarUsuarios();
    }

    public List<Usuario> buscarUsuario(Integer codigoUsuario, String nombre, String apellido, Rol codigoRolFK, String email, Boolean activo) {
        return usuarioDaoImpl.buscarPorFiltros(codigoUsuario, nombre, apellido, codigoRolFK, email, activo);
    }

    /**
     * METODOS AUXILIARES.
     */
    //Estos metodos se utilizan en el metodo crearUsuario()
    private boolean validarDatos(String telefono, String email, String password) {
        //validar el telefono: llamamos a la funcion para que se encrague de validarlo
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

    private boolean telefonoValido(String telefono) {

        if (telefono == null) {
            return false;
        }
        // eliminar espacios, guiones y parentesis
        String telefonoLimpio = telefono.replaceAll("[\\s\\-()]", "");
        // expresion regular: + seguido de 8 a 15 digitos
        //TENEMOS QUE PONERLE EL +34 O LO QUE SEA
        String regex = "^\\+\\d{8,15}$";
        return Pattern.matches(regex, telefonoLimpio);
    }

    private boolean emailValido(String email) {

        if (email == null) {
            return false;
        }

        // Eliminar espacios al inicio y al final
        String emailLimpio = email.trim();

        // Expresion regular basica para email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return Pattern.matches(regex, emailLimpio);
    }

    private boolean passwordValida(String password) {

        if (password == null) {
            return false;
        }

        String passwordLimpia = password.trim();

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        return passwordLimpia.matches(regex);
    }

    public List<Rol> recuperarListadoRoles() {
        return rolDaoImpl.listarRoles();
    }

    public List<Usuario> filtrarUsuarioPorEstado(String estado) {

        if (estado != null && !estado.isEmpty()) {

            if (estado.equals("Activo")) {
                return usuarioDaoImpl.buscarPorFiltros(null, null, null, null, null, Boolean.TRUE);
            } else if (estado.equals("Inactivo")) {
                return usuarioDaoImpl.buscarPorFiltros(null, null, null, null, null, Boolean.FALSE);
            }

        } else {
            System.out.println("El estado esta vacio o es null");
        }
        return null;
    }

    public List<Usuario> buscarPorTexto(String texto) {
        return usuarioDaoImpl.buscarPorTexto(texto);
    }

}
