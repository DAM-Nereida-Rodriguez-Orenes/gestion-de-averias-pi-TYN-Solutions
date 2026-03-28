/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa un rol de usuario en el sistema.
 * Un rol define los permisos y el nivel de acceso de un usuario.
 *
 * @author Thanya
 */
public class Rol {

    // Atributos
    private int codigoRol;
    private String descripcionRol;

    /**
     * Constructor vacío. Crea un rol sin inicializar sus atributos.
     */
    public Rol() {
    }

    /**
     * Constructor completo. Inicializa un rol con todos sus atributos.
     * @param codigoRol Código identificador del rol
     * @param descripcionRol Descripción del rol
     */
    public Rol(int codigoRol, String descripcionRol) {
        this.codigoRol = codigoRol;
        this.descripcionRol = descripcionRol;
    }

    // Getters y setters
    /**
     * Obtiene el código identificador del rol.
     * @return Código del rol
     */
    public int getCodigoRol() {
        return codigoRol;
    }

    /**
     * Establece el código identificador del rol.
     * @param codigoRol Código del rol
     */
    public void setCodigoRol(int codigoRol) {
        this.codigoRol = codigoRol;
    }

    /**
     * Obtiene la descripción del rol.
     * @return Descripción del rol
     */
    public String getDescripcionRol() {
        return descripcionRol;
    }

    /**
     * Establece la descripción del rol.
     * @param descripcionRol Descripción del rol
     */
    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }
}
