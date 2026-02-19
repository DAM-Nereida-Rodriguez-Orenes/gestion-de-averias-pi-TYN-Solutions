/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.UsuarioDao;
import daoImpl.RolDaoImpl;
import daoImpl.UsuarioDaoImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import modelo.Usuario;

/**
 *
 * @author Netri
 */
public class GestionUsuarioControlador {

    private UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl(DataSourceFactory.getDataSource());
    private RolDaoImpl rolDaoImpl = new RolDaoImpl(DataSourceFactory.getDataSource());
    private Usuario usuario;

    public List<Usuario> mostrarLista() {
        return usuarioDaoImpl.listarUsuarios();
    }

    public boolean crearUsuario(String nombre, String apellido, String codigoRol, String telefono, String email, String password) {
        // Variables
        int codigoRolInt = 0;
        int intentos = 0;
        boolean activo = true;

        //convertir el rol de texto int
        System.out.println("El rol es" + codigoRol);
        switch (codigoRol) {
            case "Administrador":
                codigoRolInt = 701;
                break;
            case "Operario":
                codigoRolInt = 702;
                break;
            case "Mecánico":
                codigoRolInt = 703;
                break;
            default:
                return false;
        }
         System.out.println("El rol es" + codigoRolInt);
         
        // validar que el rol exista 
        boolean rolExiste = rolDaoImpl.existeID(codigoRolInt);
        if (!rolExiste) {
             System.out.println("El rol no existe");
            return false;
        }
         System.out.println("El rol existe y es " + codigoRol);
         
        // llamamos al metodo validar datos telefono, email, password
        boolean datosValidos = validarDatos(telefono, email, password);
        if (!datosValidos) {
             System.out.println("Algun dato esta mal");
            return false;
        }
         System.out.println("Los datos estan bien");
         
        // crear el usuario y guardarlo
        try {
            Usuario usuario = new Usuario(nombre, apellido, codigoRolInt, telefono, email, password, intentos, LocalDateTime.now(), activo);
            usuarioDaoImpl.insertarUsuario(usuario);
            return true;

        } catch (RuntimeException e) {
            System.out.println("Error insertando usuario: " + e.getMessage());
            return false;
        }
    }

    private boolean validarDatos(String telefono, String email, String password) {
        //validar el telefono: llamamos a la funcion para que se encrague de validarlo
        if (!telefonoValido(telefono)) {
             System.out.println("el telefono esta mal");
            return false;
        }
        if (!emailValido(email)) {
             System.out.println("el email esta a¡mal");
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

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[^A-Za-z0-9]).{8,}$";

        return passwordLimpia.matches(regex);
    }

    public void eliminarUsuario(int codigoUsuario) {
        usuarioDaoImpl.eliminarUsuario(codigoUsuario);
    }
}
