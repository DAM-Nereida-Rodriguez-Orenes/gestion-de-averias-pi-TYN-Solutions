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

    private final UsuarioDao usuarioDao;

    public LoginControlador(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario accederAplicacion(String email, String password) {

        Usuario usuario = usuarioDao.buscarPorCredenciales(email, password);

        if (usuario != null) {
            return usuario;
        }

        return null;
    }

}
