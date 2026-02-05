/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author Netri
 */
public class Maquinaria {

    // Atributos
    private int codigoMaquinaria;
    private String nombre;
    private int codigoEstadoFK;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private int tipoMaquinariaFK;

    // Constructor vacio
    public Maquinaria() {
    }

    // Constructor completo
    public Maquinaria(int codigoMaquinaria, String nombre, int codigoEstadoFK,
            LocalDate fechaAlta, LocalDate fechaBaja, int tipoMaquinariaFK) {

        this.codigoMaquinaria = codigoMaquinaria;
        this.nombre = nombre;
        this.codigoEstadoFK = codigoEstadoFK;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
        this.tipoMaquinariaFK = tipoMaquinariaFK;
    }

    // Getters y setters
    public int getCodigoMaquinaria() {
        return codigoMaquinaria;
    }

    public void setCodigoMaquinaria(int codigoMaquinaria) {
        this.codigoMaquinaria = codigoMaquinaria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigoEstadoFK() {
        return codigoEstadoFK;
    }

    public void setCodigoEstadoFK(int codigoEstadoFK) {
        this.codigoEstadoFK = codigoEstadoFK;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public int getTipoMaquinariaFK() {
        return tipoMaquinariaFK;
    }

    public void setTipoMaquinariaFK(int tipoMaquinariaFK) {
        this.tipoMaquinariaFK = tipoMaquinariaFK;
    }
}
