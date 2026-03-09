/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.util.Objects;
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
    private TipoMaquinaria tipoMaquinaria;
    private Estado estado;

    // Constructor vacio
    public Maquinaria() {
    }

    // Constructor completo
    public Maquinaria(int codigoMaquinaria, String nombre, int codigoEstadoFK,
            LocalDate fechaAlta, LocalDate fechaBaja, TipoMaquinaria tipoMaquinaria, Estado estado) {

        this.codigoMaquinaria = codigoMaquinaria;
        this.nombre = nombre;
        this.codigoEstadoFK = codigoEstadoFK;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
        this.tipoMaquinaria = tipoMaquinaria;
        this.estado = estado;
    }

    // Constructor sin ID
    public Maquinaria(String nombre, int codigoEstadoFK,
            LocalDate fechaAlta, LocalDate fechaBaja, TipoMaquinaria tipoMaquinaria, Estado estado) {
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
        this.tipoMaquinaria = tipoMaquinaria;
        this.estado = estado;
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

    public TipoMaquinaria getTipoMaquinaria() {
        return tipoMaquinaria;
    }

    public void setTipoMaquinaria(TipoMaquinaria tipoMaquinaria) {
        this.tipoMaquinaria = tipoMaquinaria;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Maquinaria other = (Maquinaria) obj;
        return this.codigoMaquinaria == other.codigoMaquinaria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoMaquinaria);
    }
}
