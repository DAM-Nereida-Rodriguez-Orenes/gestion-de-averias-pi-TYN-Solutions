/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa un tipo de maquinaria en el sistema.
 * Incluye la descripción del tipo de maquinaria.
 *
 * @author Thanya
 */
public class TipoMaquinaria {

    // Atributos
    private int codigoTipoMaquinaria;
    private String descripcionMaqu;

     // Constructor vacio
    /**
     * Constructor vacío. Crea un tipo de maquinaria sin inicializar sus atributos.
     */
    public TipoMaquinaria() {
    }

    /**
     * Constructor completo. Inicializa un tipo de maquinaria con todos sus atributos.
     * @param codigoTipoMaquinaria Código identificador del tipo de maquinaria
     * @param descripcionMaq Descripción del tipo de maquinaria
     */
    public TipoMaquinaria(int codigoTipoMaquinaria, String descripcionMaq) {
        this.codigoTipoMaquinaria = codigoTipoMaquinaria;
        this.descripcionMaqu = descripcionMaq;
    }

    // Getters y setters
    /**
     * Obtiene el código identificador del tipo de maquinaria.
     * @return Código del tipo de maquinaria
     */
    public int getCodigoTipoMaquinaria() {
        return codigoTipoMaquinaria;
    }

    /**
     * Establece el código identificador del tipo de maquinaria.
     * @param codigoTipoMaquinaria Código del tipo de maquinaria
     */
    public void setCodigoTipoMaquinaria(int codigoTipoMaquinaria) {
        this.codigoTipoMaquinaria = codigoTipoMaquinaria;
    }

    /**
     * Obtiene la descripción del tipo de maquinaria.
     * @return Descripción del tipo de maquinaria
     */
    public String getDescripcionMaq() {
        return descripcionMaqu;
    }

    /**
     * Establece la descripción del tipo de maquinaria.
     * @param descripcionMaq Descripción del tipo de maquinaria
     */
    public void setDescripcionMaq(String descripcionMaq) {
        this.descripcionMaqu = descripcionMaq;
    }
    
    /**
     * Devuelve la representación en texto del tipo de maquinaria (descripción).
     * @return Descripción del tipo de maquinaria
     */
    @Override
    public String toString() {
        return getDescripcionMaq();
    }
}
