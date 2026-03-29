/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

/**
 * Clase que representa a un usuario del sistema.
 * Contiene información personal, credenciales, rol, estado y datos de acceso.
 * Permite la gestión de usuarios en la aplicación.
 *
 * @author Thanya
 */
public class Usuario {

    // Atributos
    private int codigoUsuario;
    private String nombre;
    private String apellido;
    private Rol rol;
    private String telefono;
    private String email;
    private String password;
    private int intentos;
    private LocalDateTime ultimoAcceso;
    private Boolean activo;

    /**
     * Constructor vacío. Crea un usuario sin inicializar sus atributos.
     */
    public Usuario() {
    }

    /**
     * Constructor completo. Inicializa un usuario con todos sus atributos.
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param codigoRolFK Rol del usuario
     * @param telefono Teléfono del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param intentos Número de intentos de acceso
     * @param ultimoAcceso Fecha y hora del último acceso
     * @param activo Estado de actividad del usuario
     */
    public Usuario(String nombre, String apellido, Rol codigoRolFK,
            String telefono, String email, String password, int intentos,
            LocalDateTime ultimoAcceso, boolean activo) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = codigoRolFK;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.intentos = intentos;
        this.ultimoAcceso = ultimoAcceso;
        this.activo = activo;
    }

    // Getters y setters
    /**
     * Obtiene el código identificador del usuario.
     * @return Código del usuario
     */
    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    /**
     * Establece el código identificador del usuario.
     * @param codigoUsuario Código del usuario
     */
    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return Nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre Nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     * @return Apellido del usuario
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     * @param apellido Apellido del usuario
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el rol del usuario.
     * @return Rol del usuario
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     * @param rol Rol del usuario
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el teléfono del usuario.
     * @return Teléfono del usuario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del usuario.
     * @param telefono Teléfono del usuario
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el email del usuario.
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario.
     * @param email Email del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password Contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el número de intentos de acceso fallidos.
     * @return Número de intentos
     */
    public int getIntentos() {
        return intentos;
    }

    /**
     * Establece el número de intentos de acceso fallidos.
     * @param intentos Número de intentos
     */
    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    /**
     * Obtiene la fecha y hora del último acceso.
     * @return Fecha y hora del último acceso
     */
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * Establece la fecha y hora del último acceso.
     * @param ultimoAcceso Fecha y hora del último acceso
     */
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * Indica si el usuario está activo.
     * @return true si está activo, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad del usuario.
     * @param activo true si está activo, false en caso contrario
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    /**
     * Devuelve la representación en texto del usuario (nombre y apellido).
     * @return Cadena con el nombre y apellido del usuario
     */
    @Override
    public String toString() {
        return this.nombre + " " + this.apellido; // Esto es lo que se verá en la lista
    }
}
