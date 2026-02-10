/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;
import java.util.List;
import modelo.Estado;

/**
 * Las tablas maestras no deben modificarse, o al menos no a menudo, pero, como pretendemos que esta aplicación sea escalable,
 * debemos pensar en qué pasaría si en un tiempo el taller cliente necesita añadir algún tipo más al catálogo.
 * Por ello, vamos a crear las funciones de añadir, modificar y eliminar, aunque controlaremos mediante la interfaz el acceso a estas
 * para que sea limitado.
 * 
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public interface EstadoDAO {
    void insertar(Estado e);
    void modificar(Estado e);
    void eliminar(Estado e);
    List<Estado> listarEstado();
}
