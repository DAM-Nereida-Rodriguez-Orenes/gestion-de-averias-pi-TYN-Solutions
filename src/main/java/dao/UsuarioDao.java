/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import modelo.Rol;
import modelo.Usuario;

/**
 *
 * @author Thanya
 */
public interface  UsuarioDao {

    void insertarUsuario(Usuario usuario);

    void actualizarUsuario(Usuario usuario);

    void eliminarUsuario(int codigoUsuario);

    List<Usuario> listarUsuarios();

    List<Usuario> buscarPorFiltros(Integer codigoUsuario, String nombre, String apellido, Rol codigoRolFK, String email);
    
    Usuario buscarPorCredenciales(String email, String password);
    
    boolean actualizarContraseña(String password);
}
