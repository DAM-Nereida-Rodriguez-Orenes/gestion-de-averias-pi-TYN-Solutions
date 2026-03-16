/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import java.util.prefs.Preferences;
import modelo.Usuario;

/**
 *
 * @author Netri
 */
public class LoginControlador {

    //si quiero mostrar uin mensaje lo puedo hacer mediante una variable 
    //que la puedo llamar desde la vista que me interese 
    private final UsuarioDao usuarioDao;
    private Usuario usuario;
    
    //Guardar la sesion del login
    private static final Preferences preferencias = Preferences.userRoot().node("fixoraSesion");

    //Guardamos el ID del usuario 
    private static final String CLAVE_EMAIL_USUARIO = "emailUsuario";
    private static final String CLAVE_SESION_ACTIVA = "sesionActiva";
    private static final String CLAVE_ROL_USUARIO = "rolUsuario";

    //constructor
    public LoginControlador() {
        this.usuarioDao = new UsuarioDaoImpl(DataSourceFactory.getDataSource());
    }

    //getter y setters
    public UsuarioDao getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario accederAplicacion(String email, String password) {
        Usuario usuario = usuarioDao.buscarPorCredenciales(email, password);

        if (usuario != null) {
            return usuario;
        }
        return null;
    }

    public void restablecerPassword(String emailSolicitud) {

        //Primero debemos comprobar si es administrador para poder cambiar la contraseña 
        int codigoRol = usuario.getRol().getCodigoRol();

        if (codigoRol != 701) {
            System.out.println("No tienes permisos de adminitrador");
            return;
        }
        String passwordActualizada = usuarioDao.actualizarPassword(emailSolicitud, null);
        if (passwordActualizada != null && !passwordActualizada.isEmpty()) {
            System.out.println("Contrasena actualizada");
        } else {
            System.out.println("No existe un usuario con ese email");
        }
    }

    //METODOS PARA LA PERSISTENCIA DEL USUARIO LOGEADO 

    public void guardarSesion(Usuario usuario) {
        preferencias.put(CLAVE_EMAIL_USUARIO, usuario.getEmail());
        preferencias.putInt(CLAVE_ROL_USUARIO, usuario.getRol().getCodigoRol());
        preferencias.putBoolean(CLAVE_SESION_ACTIVA, true);
    }

    public boolean haySesionActiva() {
        return preferencias.getBoolean(CLAVE_SESION_ACTIVA, false);
    }

    public String getEmailUsuario() {
        return preferencias.get(CLAVE_EMAIL_USUARIO, "");
    }

    public static Integer getRolUsuario() {
        return preferencias.getInt(CLAVE_ROL_USUARIO, -1);
    }

    //este metodo es para salir al login 
    public void cerrarSesion() {
        preferencias.remove(CLAVE_EMAIL_USUARIO);
        preferencias.putBoolean(CLAVE_SESION_ACTIVA, false);
        preferencias.remove(CLAVE_ROL_USUARIO);
    }
}
