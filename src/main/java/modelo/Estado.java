/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Netri
 */
public class Estado {

    // Atributos
    private int codigoEstado;
    private String descripcionEstado;

    // Constructor vacio
    public Estado() {
    }

    // Constructor completo
    public Estado(int codigoEstado, String descripcionEstado) {
        this.codigoEstado = codigoEstado;
        this.descripcionEstado = descripcionEstado;
    }

    // Getters y setters
    public int getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(int codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }
}
