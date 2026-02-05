/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

/**
 *
 * @author Netri
 */
public class Averia {

    // Atributos
    private int codigoAveria;
    private String descInicAveria;
    private LocalDateTime fechaInicioAver;
    private LocalDateTime fechaAsigTecnico;
    private LocalDateTime fechaAcepTecnico;
    private LocalDateTime fechaFinalizTecnico;
    private String procRealizadoTecnico;
    private int usuarioReportaFK;
    private Integer usuarioTecnicoFK; // Se utiliza Integer en lugar de int porque este campo puede ser NULL en la base de datos.
    private int maquinariaFK;
    private int tipoAveriaFK;

    /**
     * El tipo int no admite valores nulos, mientras que Integer permite representar correctamente
     * la ausencia de un tecnico asignado cuando la averia aun no ha sido atendida.
     */
    
    // Constructor vacio
    public Averia() {
    }

    // Constructor completo
    public Averia(int codigoAveria, String descInicAveria, LocalDateTime fechaInicioAver,
            LocalDateTime fechaAsigTecnico, LocalDateTime fechaAcepTecnico,
            LocalDateTime fechaFinalizTecnico, String procRealizadoTecnico,
            int usuarioReportaFK, Integer usuarioTecnicoFK,
            int maquinariaFK, int tipoAveriaFK) {

        this.codigoAveria = codigoAveria;
        this.descInicAveria = descInicAveria;
        this.fechaInicioAver = fechaInicioAver;
        this.fechaAsigTecnico = fechaAsigTecnico;
        this.fechaAcepTecnico = fechaAcepTecnico;
        this.fechaFinalizTecnico = fechaFinalizTecnico;
        this.procRealizadoTecnico = procRealizadoTecnico;
        this.usuarioReportaFK = usuarioReportaFK;
        this.usuarioTecnicoFK = usuarioTecnicoFK;
        this.maquinariaFK = maquinariaFK;
        this.tipoAveriaFK = tipoAveriaFK;
    }

    // Getters y setters
    public int getCodigoAveria() {
        return codigoAveria;
    }

    public void setCodigoAveria(int codigoAveria) {
        this.codigoAveria = codigoAveria;
    }

    public String getDescInicAveria() {
        return descInicAveria;
    }

    public void setDescInicAveria(String descInicAveria) {
        this.descInicAveria = descInicAveria;
    }

    public LocalDateTime getFechaInicioAver() {
        return fechaInicioAver;
    }

    public void setFechaInicioAver(LocalDateTime fechaInicioAver) {
        this.fechaInicioAver = fechaInicioAver;
    }

    public LocalDateTime getFechaAsigTecnico() {
        return fechaAsigTecnico;
    }

    public void setFechaAsigTecnico(LocalDateTime fechaAsigTecnico) {
        this.fechaAsigTecnico = fechaAsigTecnico;
    }

    public LocalDateTime getFechaAcepTecnico() {
        return fechaAcepTecnico;
    }

    public void setFechaAcepTecnico(LocalDateTime fechaAcepTecnico) {
        this.fechaAcepTecnico = fechaAcepTecnico;
    }

    public LocalDateTime getFechaFinalizTecnico() {
        return fechaFinalizTecnico;
    }

    public void setFechaFinalizTecnico(LocalDateTime fechaFinalizTecnico) {
        this.fechaFinalizTecnico = fechaFinalizTecnico;
    }

    public String getProcRealizadoTecnico() {
        return procRealizadoTecnico;
    }

    public void setProcRealizadoTecnico(String procRealizadoTecnico) {
        this.procRealizadoTecnico = procRealizadoTecnico;
    }

    public int getUsuarioReportaFK() {
        return usuarioReportaFK;
    }

    public void setUsuarioReportaFK(int usuarioReportaFK) {
        this.usuarioReportaFK = usuarioReportaFK;
    }

    public Integer getUsuarioTecnicoFK() {
        return usuarioTecnicoFK;
    }

    public void setUsuarioTecnicoFK(Integer usuarioTecnicoFK) {
        this.usuarioTecnicoFK = usuarioTecnicoFK;
    }

    public int getMaquinariaFK() {
        return maquinariaFK;
    }

    public void setMaquinariaFK(int maquinariaFK) {
        this.maquinariaFK = maquinariaFK;
    }

    public int getTipoAveriaFK() {
        return tipoAveriaFK;
    }

    public void setTipoAveriaFK(int tipoAveriaFK) {
        this.tipoAveriaFK = tipoAveriaFK;
    }
}
