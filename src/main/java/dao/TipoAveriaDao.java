package dao;

import modelo.TipoAveria;

import java.util.List;

/**
 * Interfaz DAO para definir las operaciones de acceso a datos de tipos de averia.
 * @author yosnavmol
 */
public interface TipoAveriaDao {

    // Inserta un nuevo tipo de averia
    void insertar(TipoAveria a);

    // Actualiza un tipo de averia existente
    void actualizar(TipoAveria a);

    // Comprueba si existe un tipo de averia con ese id
    boolean existeId(int id);

    // Lista todos los tipos de averia
    List<TipoAveria> listar();

    // Elimina un tipo de averia por su id
    boolean eliminar(int id);

    // Busca el id de un tipo de averia por su descripcion
    int buscarTipoAveriaPorDescripcion(String descripcion);
}