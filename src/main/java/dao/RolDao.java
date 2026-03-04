/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import modelo.Rol;

/**
 *
 * @author Thanya
 */
public interface  RolDao {
    
    void insertarRol(Rol rol);

    void actualizarRol(Rol rol);

    void eliminarRol(int codigoRol);

    List<Rol> listarRoles();

    boolean existeID(int codigoRol);
}
