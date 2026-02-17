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
public class GestionUsuarioControlador {

    private final UsuarioDao usuarioDao;

    public GestionUsuarioControlador(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public void eliminarUsuario(int codigoUsuario){
        usuarioDao.eliminarUsuario(codigoUsuario);
    }
}
