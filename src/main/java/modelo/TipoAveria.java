/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa un tipo de avería en el sistema.
 * Incluye descripción y tiempo promedio de reparación.
 *
 * @author Thanya
 */
public class TipoAveria {
    
    // Atributos
    private int codigoTipoAveria;
    private String descripcionTipoAv;
    private float tiempoPromRepar;

    /**
     * Constructor vacío. Crea un tipo de avería sin inicializar sus atributos.
     */
    public TipoAveria() {
    }

    /**
     * Constructor completo. Inicializa un tipo de avería con todos sus atributos.
     * @param codigoTipoAveria Código identificador del tipo de avería
     * @param descripcionTipoAv Descripción del tipo de avería
     * @param tiempoPromRepar Tiempo promedio de reparación
     */
    public TipoAveria(int codigoTipoAveria, String descripcionTipoAv, float tiempoPromRepar) {
        this.codigoTipoAveria = codigoTipoAveria;
        this.descripcionTipoAv = descripcionTipoAv;
        this.tiempoPromRepar = tiempoPromRepar;
    }

    /**
     * Obtiene el código identificador del tipo de avería.
     * @return Código del tipo de avería
     */
    public int getCodigoTipoAveria() {
        return codigoTipoAveria;
    }

    /**
     * Establece el código identificador del tipo de avería.
     * @param codigoTipoAveria Código del tipo de avería
     */
    public void setCodigoTipoAveria(int codigoTipoAveria) {
        this.codigoTipoAveria = codigoTipoAveria;
    }

    /**
     * Obtiene la descripción del tipo de avería.
     * @return Descripción del tipo de avería
     */
    public String getDescripcionTipoAv() {
        return descripcionTipoAv;
    }

    /**
     * Establece la descripción del tipo de avería.
     * @param descripcionTipoAv Descripción del tipo de avería
     */
    public void setDescripcionTipoAv(String descripcionTipoAv) {
        this.descripcionTipoAv = descripcionTipoAv;
    }

    /**
     * Obtiene el tiempo promedio de reparación.
     * @return Tiempo promedio de reparación
     */
    public float getTiempoPromRepar() {
        return tiempoPromRepar;
    }

    /**
     * Establece el tiempo promedio de reparación.
     * @param tiempoPromRepar Tiempo promedio de reparación
     */
    public void setTiempoPromRepar(float tiempoPromRepar) {
        this.tiempoPromRepar = tiempoPromRepar;
    }
    
    /**
     * Devuelve la representación en texto del tipo de avería (descripción).
     * @return Descripción del tipo de avería
     */
    @Override
    public String toString() {
        return this.descripcionTipoAv; 
    }
}
