/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Netri
 */
public class TipoMaquinaria {

    // Atributos
    private int codigoTipoMaquinaria;
    private String descripcionMaqu;

     // Constructor vacio
    public TipoMaquinaria() {
    }

    // Constructor completo
    public TipoMaquinaria(int codigoTipoMaquinaria, String descripcionMaq) {
        this.codigoTipoMaquinaria = codigoTipoMaquinaria;
        this.descripcionMaqu = descripcionMaq;
    }

    // Getters y setters
    public int getCodigoTipoMaquinaria() {
        return codigoTipoMaquinaria;
    }

    public void setCodigoTipoMaquinaria(int codigoTipoMaquinaria) {
        this.codigoTipoMaquinaria = codigoTipoMaquinaria;
    }

    public String getDescripcionMaq() {
        return descripcionMaqu;
    }

    public void setDescripcionMaq(String descripcionMaq) {
        this.descripcionMaqu = descripcionMaq;
    }
    
    @Override
    public String toString() {
        return getDescripcionMaq();
    }
}
