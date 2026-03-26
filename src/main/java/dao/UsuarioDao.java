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
public interface UsuarioDao {

    void insertarUsuario(Usuario usuario);

    void actualizarUsuario(Usuario usuario);

    void eliminarUsuario(int codigoUsuario);

    List<Usuario> listarUsuarios();

    List<Usuario> buscarPorFiltrosUsuario(Integer codigoUsuario, String nombre, String apellido, Rol rol, String email, Boolean activo);

    Usuario buscarPorCredenciales(String email, String password);

    String actualizarPassword(String email, String nuevaPassword);

    List<Usuario> buscarPorTexto(String texto);

    Usuario buscarPorEmail(String email);

    List<Usuario> buscarTecnicosOrdenadorPorCarga(int codigoTipoAveria);

    public Object[] obtenerMotivosTecnico(int codigoTecnico, int codigoTipoAveria);
}
