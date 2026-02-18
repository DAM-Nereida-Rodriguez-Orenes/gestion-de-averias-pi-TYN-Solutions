/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Netri
 */
public class TipoAveria {
    
    // Atributos
    private int codigoTipoAveria;
    private String descripcionTipoAv;
    private float tiempoPromRepar;

    // Constructor vacio
    public TipoAveria() {
    }

    // Constructor completo
    public TipoAveria(int codigoTipoAveria, String descripcionTipoAv, float tiempoPromRepar) {
        this.codigoTipoAveria = codigoTipoAveria;
        this.descripcionTipoAv = descripcionTipoAv;
        this.tiempoPromRepar = tiempoPromRepar;
    }

    // Getters y setters
    public int getCodigoTipoAveria() {
        return codigoTipoAveria;
    }

    public void setCodigoTipoAveria(int codigoTipoAveria) {
        this.codigoTipoAveria = codigoTipoAveria;
    }

    public String getDescripcionTipoAv() {
        return descripcionTipoAv;
    }

    public void setDescripcionTipoAv(String descripcionTipoAv) {
        this.descripcionTipoAv = descripcionTipoAv;
    }

    public float getTiempoPromRepar() {
        return tiempoPromRepar;
    }

    public void setTiempoPromRepar(float tiempoPromRepar) {
        this.tiempoPromRepar = tiempoPromRepar;
    }
    
    @Override
    public String toString() {
        return this.descripcionTipoAv; 
    }
}
