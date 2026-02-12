/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import modelo.TipoAveria;

/**
 *
 * @author yosnavmol
 */
public interface TipoAveriaDao {
    
    void insertar(TipoAveria a);
    void actualizar(TipoAveria a);
    boolean existeId(int id);
    List<TipoAveria> listar();
    void eliminar(int id);
    
}
