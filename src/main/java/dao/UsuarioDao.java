package dao;

import modelo.Rol;
import modelo.Usuario;

import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos relacionadas con la entidad Usuario.
 * Proporciona métodos para insertar, actualizar, eliminar, buscar y listar usuarios,
 * así como operaciones específicas para técnicos y gestión de motivos de avería.
 *
 * @author Thanya
 */
public interface UsuarioDao {

    /** Inserta un nuevo usuario en la base de datos
     * @param usuario el objeto Usuario a insertar
     */
    void insertarUsuario(Usuario usuario);

    /** Actualiza un usuario ya existente
     * @param usuario el objeto Usuario con los datos actualizados
     */
    void actualizarUsuario(Usuario usuario);

    /** Elimina un usuario por su codigo
     * @param codigoUsuario el codigo del usuario a eliminar
     */
    void eliminarUsuario(int codigoUsuario);

    /** Lista todos los usuarios de la base de datos
     * @return una lista de objetos Usuario
     */
    List<Usuario> listarUsuarios();

    /** Busca usuarios aplicando distintos filtros
     * @param codigoUsuario el codigo del usuario a buscar (opcional)
     * @param nombre el nombre del usuario a buscar (opcional)
     * @param apellido el apellido del usuario a buscar (opcional)
     * @param rol el rol del usuario a buscar (opcional)
     * @param email el email del usuario a buscar (opcional)
     * @param activo el estado de actividad del usuario a buscar (opcional)
     * @return una lista de objetos Usuario que cumplen con los filtros aplicados
     */
    List<Usuario> buscarPorFiltrosUsuario(Integer codigoUsuario, String nombre, String apellido, Rol rol, String email, Boolean activo);

    /** Busca un usuario por sus credenciales de email y password
     * @param email el email del usuario a buscar
     * @param password la password del usuario a buscar
     * @return el objeto Usuario que coincide con las credenciales, o null si no se encuentra
     */
    Usuario buscarPorCredenciales(String email, String password);

    /** Actualiza la password de un usuario identificado por su email
     * @param email el email del usuario cuya password se va a actualizar
     * @param nuevaPassword la nueva password a establecer
     * @return null si la actualización fue exitosa, o un mensaje de error si falla
     */
    String actualizarPassword(String email, String nuevaPassword);

    /** Busca usuarios cuyo nombre o apellido contenga el texto dado
     * @param texto el texto a buscar en el nombre o apellido de los usuarios
     * @return una lista de objetos Usuario que coinciden con la búsqueda
     */
    List<Usuario> buscarPorTexto(String texto);

    /** Busca un usuario por su email
     * @param email el email del usuario a buscar
     * @return el objeto Usuario que coincide con el email, o null si no se encuentra
     */
    Usuario buscarPorEmail(String email);

    /** Busca tecnicos ordenados por su carga de trabajo para un tipo de averia dado
     * @param codigoTipoAveria el codigo del tipo de averia para el cual se buscan tecnicos
     * @return una lista de objetos Usuario que son tecnicos ordenados por su carga de trabajo para el tipo de averia especificado
     */
    List<Usuario> buscarTecnicosOrdenadorPorCarga(int codigoTipoAveria);

    /** Obtiene los motivos de averia asociados a un tecnico y un tipo de averia
     * @param codigoTecnico el codigo del tecnico para el cual se obtienen los motivos
     * @param codigoTipoAveria el codigo del tipo de averia para el cual se obtienen los motivos
     * @return un array de objetos que representan los motivos de averia asociados al tecnico y tipo de averia especificados
     */
    Object[] obtenerMotivosTecnico(int codigoTecnico, int codigoTipoAveria);
}