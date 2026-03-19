/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.time.LocalDateTime;
import java.util.List;
import modelo.Averia;
import modelo.Usuario;

/**
 *
 * @author yosnavmol
 */
public interface AveriaDao {
    
    void insertar(Averia a);
    void actualizar(Averia a);
    List<Averia> buscarPorFiltros(Integer idAveria, 
                                        String descripcion, 
                                        LocalDateTime fechaInicio, 
                                        LocalDateTime fechaFin, 
                                        Integer idUsuarioReporta, 
                                        Integer idTecnico, 
                                        Integer idMaquinaria, 
                                        Integer idTipoAveria);
    List<Averia> listar();
    boolean eliminar(int id);       
}
