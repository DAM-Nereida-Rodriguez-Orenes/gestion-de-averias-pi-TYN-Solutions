/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import config.DataSourceFactory;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import java.util.List;
import modelo.Usuario;

/**
 *
 * @author Netri
 */
public class GestionUsuarioControlador {

    private UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl(DataSourceFactory.getDataSource());

    public GestionUsuarioControlador() {
    }

    public List<Usuario> mostrarLista() {
        return usuarioDaoImpl.listarUsuarios();
    }
    
    public boolean crearUsuario(){
        
        return true;
    }

    public void eliminarUsuario(int codigoUsuario) {
        usuarioDaoImpl.eliminarUsuario(codigoUsuario);
    }
}
