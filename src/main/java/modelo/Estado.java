/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa un estado en el sistema.
 * Un estado puede estar asociado a una avería, máquina, etc.
 *
 * @author Thanya
 */
public class Estado {

    // Atributos
    private int codigoEstado;
    private String descripcionEstado;

    /**
     * Constructor vacío. Crea un estado sin inicializar sus atributos.
     */
    public Estado() {
    }

    /**
     * Constructor completo. Inicializa un estado con todos sus atributos.
     * @param codigoEstado Código identificador del estado
     * @param descripcionEstado Descripción del estado
     */
    public Estado(int codigoEstado, String descripcionEstado) {
        this.codigoEstado = codigoEstado;
        this.descripcionEstado = descripcionEstado;
    }

    /**
     * Obtiene el código identificador del estado.
     * @return Código del estado
     */
    public int getCodigoEstado() {
        return codigoEstado;
    }

    /**
     * Establece el código identificador del estado.
     * @param codigoEstado Código del estado
     */
    public void setCodigoEstado(int codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    /**
     * Obtiene la descripción del estado.
     * @return Descripción del estado
     */
    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    /**
     * Establece la descripción del estado.
     * @param descripcionEstado Descripción del estado
     */
    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    /**
     * Devuelve la descripción del estado como representación en texto.
     * @return Descripción del estado
     */
    @Override
    public String toString() {
        return getDescripcionEstado();
    }
}
