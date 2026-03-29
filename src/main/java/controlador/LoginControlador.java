package controlador;

import config.DataSourceFactory;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import modelo.Usuario;

import java.util.prefs.Preferences;

/**
 * Controlador encargado de gestionar toda la logica relacionada con el login y la sesion de usuario
 * Aqui se realizan operaciones como comprobar las credenciales, guardar la sesion, cerrar la sesion, y gestionar las solicitudes de recuperacion de password
 * @author Thanya
 */
public class LoginControlador {

    // DAO para acceder a los datos del usuario
    private final UsuarioDao usuarioDao;

    // Usuario con el que se trabaja en memoria
    private Usuario usuario;

    // Preferencias para guardar la sesion iniciada
    private static final Preferences preferencias = Preferences.userRoot().node("fixoraSesion");

    // Claves usadas para guardar datos de sesion
    private static final String CLAVE_EMAIL_USUARIO = "emailUsuario";
    private static final String CLAVE_SESION_ACTIVA = "sesionActiva";
    private static final String CLAVE_ROL_USUARIO = "rolUsuario";

    // Preferencias para guardar solicitudes de recuperacion de password
    private static final Preferences preferenciasRecuperacion = Preferences.userRoot().node("fixoraRecuperacion");
    private static final String CLAVE_SOLICITUDES = "solicitudesPendientes";

    /**
     * Constructor del controlador.
     * Inicializa el DAO de usuario.
     */
    public LoginControlador() {
        this.usuarioDao = new UsuarioDaoImpl(DataSourceFactory.getDataSource());
    }

    // GETTERS Y SETTERS
    public UsuarioDao getUsuarioDao() {
        return usuarioDao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Metodo para comprobar las credenciales del login.
     * Si son correctas devuelve el usuario.
     */
    public Usuario accederAplicacion(String email, String password) {
        Usuario usuario = usuarioDao.buscarPorCredenciales(email, password);

        if (usuario != null) {
            return usuario;
        }
        return null;
    }

    /**
     * Guarda en preferencias los datos de la sesion iniciada.
     */
    public void guardarSesion(Usuario usuario) {
        preferencias.put(CLAVE_EMAIL_USUARIO, usuario.getEmail());
        preferencias.putInt(CLAVE_ROL_USUARIO, usuario.getRol().getCodigoRol());
        preferencias.putBoolean(CLAVE_SESION_ACTIVA, true);
    }

    /**
     * Comprueba si actualmente hay una sesion activa.
     */
    public boolean haySesionActiva() {
        return preferencias.getBoolean(CLAVE_SESION_ACTIVA, false);
    }

    /**
     * Devuelve el email guardado de la sesion.
     */
    public String getEmailUsuario() {
        return preferencias.get(CLAVE_EMAIL_USUARIO, "");
    }

    /**
     * Devuelve el rol guardado de la sesion.
     */
    public static Integer getRolUsuario() {
        return preferencias.getInt(CLAVE_ROL_USUARIO, -1);
    }

    /**
     * Cierra la sesion eliminando los datos guardados.
     */
    public void cerrarSesion() {
        preferencias.remove(CLAVE_EMAIL_USUARIO);
        preferencias.putBoolean(CLAVE_SESION_ACTIVA, false);
        preferencias.remove(CLAVE_ROL_USUARIO);
    }

    /**
     * Devuelve el usuario logueado a partir del email guardado.
     */
    public Usuario getUsuarioSesion() {

        String email = getEmailUsuario();

        if (email == null || email.isEmpty()) {
            return null;
        }

        return usuarioDao.buscarPorEmail(email);
    }

    /**
     * Guarda una solicitud de recuperacion de password.
     * No la repite si ya estaba registrada.
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

    /**
     * Devuelve todas las solicitudes de recuperacion guardadas.
     */
    public String[] obtenerSolicitudesRecuperacion() {
        String solicitudes = preferenciasRecuperacion.get(CLAVE_SOLICITUDES, "");

        if (solicitudes == null || solicitudes.trim().isEmpty()) {
            return new String[0];
        }

        return solicitudes.split(";");
    }

    /**
     * Elimina una solicitud de recuperacion concreta.
     */
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