package dao;

import modelo.Averia;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz DAO para definir las operaciones de acceso a datos de averias.
 * @author yosnavmol
 */
public interface AveriaDao {

    // Inserta una nueva averia en la base de datos
    void insertar(Averia a);

    // Actualiza una averia ya existente
    void actualizar(Averia a);

    // Busca averias aplicando distintos filtros
    List<Averia> buscarPorFiltros(
            Integer idAveria,
            String descripcion,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Integer idUsuarioReporta,
            Integer idTecnico,
            Integer idMaquinaria,
            Integer idTipoAveria
    );

    // Elimina una averia por su id
    boolean eliminar(int id);
}