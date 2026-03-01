/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.UsuarioDao;
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

    //constructor
    public LoginControlador(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
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

    public void restablecerCOntraseña(String emailSolicitud) {

        //Primero debemso comprobar si es administrador para poder cambiar la contraseña 
        int codigoRol = usuario.getCodigoRolFK().getCodigoRol();

        if (codigoRol != 701) {
            System.out.println("No tienes permisso de adminitrador");
        } else {
            usuarioDao.actualizarContraseña(emailSolicitud);
        }
    }
}
