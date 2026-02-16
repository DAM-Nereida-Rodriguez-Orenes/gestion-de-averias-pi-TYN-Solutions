/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import modelo.Maquinaria;

/**
 * Las funciones básicas son: crear, modificar, eliminar y listar. Esta lista, además, debe poderse filtrar, así que
 * vamos a añadir un método de búsqueda por ID (buscar una máquina concreta), por tipo de máquina y por su estado. Como queremos que el
 * filtro pueda incluir varios campos (por ejemplo, máquinas cortadoras averiadas), el método de filtrado será único
 * 
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public interface MaquinariaDAO {
    void insertar(Maquinaria m);
    void modificar(Maquinaria m);
    void eliminar(int mID);
    List<Maquinaria> listarMaquinaria();
    List<Maquinaria> buscarPorFiltrosMaquinaria( Integer codigoEstadoFK,
        Integer tipoMaquinariaFK,
        LocalDate fechaAltaDesde,
        LocalDate fechaAltaHasta,
        Boolean soloActivas);
}
