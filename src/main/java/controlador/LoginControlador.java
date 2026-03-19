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

    // Metodo redueprar contraseña
    private static final Preferences preferenciasRecuperacion = Preferences.userRoot().node("fixoraRecuperacion");
    private static final String CLAVE_SOLICITUDES = "solicitudesPendientes";

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

    //Otener el usuario logueado
    public Usuario getUsuarioSesion() {

        String email = getEmailUsuario();

        if (email == null || email.isEmpty()) {
            return null;
        }

        return usuarioDao.buscarPorEmail(email);
    }

    /**
     * RECUPERAR CONTRASEÑA.
     *
     */
    public void registrarSolicitudRecuperacion(String emailSolicitud) {
        String solicitudesActuales = preferenciasRecuperacion.get(CLAVE_SOLICITUDES, "");

        if (solicitudesActuales.isEmpty()) {
            preferenciasRecuperacion.put(CLAVE_SOLICITUDES, emailSolicitud);
        } else {
            String[] correos = solicitudesActuales.split(";");
            boolean yaExiste = false;

            for (int i = 0; i < correos.length; i++) {
                if (correos[i].equalsIgnoreCase(emailSolicitud)) {
                    yaExiste = true;
                    break;
                }
            }

            if (!yaExiste) {
                preferenciasRecuperacion.put(CLAVE_SOLICITUDES, solicitudesActuales + ";" + emailSolicitud);
            }
        }
    }

    public String[] obtenerSolicitudesRecuperacion() {
        String solicitudes = preferenciasRecuperacion.get(CLAVE_SOLICITUDES, "");

        if (solicitudes == null || solicitudes.trim().isEmpty()) {
            return new String[0];
        }

        return solicitudes.split(";");
    }

    public void eliminarSolicitudRecuperacion(String emailSolicitud) {
        String solicitudes = preferenciasRecuperacion.get(CLAVE_SOLICITUDES, "");

        if (solicitudes == null || solicitudes.trim().isEmpty()) {
            return;
        }

        String[] correos = solicitudes.split(";");
        String resultado = "";

        for (int i = 0; i < correos.length; i++) {
            if (!correos[i].equalsIgnoreCase(emailSolicitud)) {
                if (resultado.isEmpty()) {
                    resultado = correos[i];
                } else {
                    resultado = resultado + ";" + correos[i];
                }
            }
        }

        preferenciasRecuperacion.put(CLAVE_SOLICITUDES, resultado);
    }
}
