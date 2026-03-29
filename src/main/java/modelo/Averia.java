/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

/**
 * Clase que representa una avería reportada en el sistema.
 * Contiene información sobre el estado, fechas, técnicos y maquinaria asociada.
 *
 * @author Thanya
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
    /**
     * Constructor completo. Inicializa una avería con todos sus atributos.
     * @param codigoAveria Código identificador de la avería
     * @param descInicAveria Descripción inicial de la avería
     * @param fechaInicioAver Fecha de inicio de la avería
     * @param fechaAsigTecnico Fecha de asignación al técnico
     * @param fechaAcepTecnico Fecha de aceptación por el técnico
     * @param fechaFinalizTecnico Fecha de finalización por el técnico
     * @param procRealizadoTecnico Proceso realizado por el técnico
     * @param usuarioReportaFK Usuario que reporta la avería
     * @param usuarioTecnicoFK Técnico asignado (puede ser null)
     * @param maquinariaFK Maquinaria asociada
     * @param tipoAveriaFK Tipo de avería
     */
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
    /**
     * Obtiene el código identificador de la avería.
     * @return Código de la avería
     */
    public int getCodigoAveria() {
        return codigoAveria;
    }

    /**
     * Establece el código identificador de la avería.
     * @param codigoAveria Código de la avería
     */
    public void setCodigoAveria(int codigoAveria) {
        this.codigoAveria = codigoAveria;
    }

    /**
     * Obtiene la descripción inicial de la avería.
     * @return Descripción inicial de la avería
     */
    public String getDescInicAveria() {
        return descInicAveria;
    }

    /**
     * Establece la descripción inicial de la avería.
     * @param descInicAveria Descripción inicial de la avería
     */
    public void setDescInicAveria(String descInicAveria) {
        this.descInicAveria = descInicAveria;
    }

    /**
     * Obtiene la fecha de inicio de la avería.
     * @return Fecha de inicio de la avería
     */
    public LocalDateTime getFechaInicioAver() {
        return fechaInicioAver;
    }

    /**
     * Establece la fecha de inicio de la avería.
     * @param fechaInicioAver Fecha de inicio de la avería
     */
    public void setFechaInicioAver(LocalDateTime fechaInicioAver) {
        this.fechaInicioAver = fechaInicioAver;
    }

    /**
     * Obtiene la fecha de asignación al técnico.
     * @return Fecha de asignación al técnico
     */
    public LocalDateTime getFechaAsigTecnico() {
        return fechaAsigTecnico;
    }

    /**
     * Establece la fecha de asignación al técnico.
     * @param fechaAsigTecnico Fecha de asignación al técnico
     */
    public void setFechaAsigTecnico(LocalDateTime fechaAsigTecnico) {
        this.fechaAsigTecnico = fechaAsigTecnico;
    }

    /**
     * Obtiene la fecha de aceptación por el técnico.
     * @return Fecha de aceptación por el técnico
     */
    public LocalDateTime getFechaAcepTecnico() {
        return fechaAcepTecnico;
    }

    /**
     * Establece la fecha de aceptación por el técnico.
     * @param fechaAcepTecnico Fecha de aceptación por el técnico
     */
    public void setFechaAcepTecnico(LocalDateTime fechaAcepTecnico) {
        this.fechaAcepTecnico = fechaAcepTecnico;
    }

    /**
     * Obtiene la fecha de finalización por el técnico.
     * @return Fecha de finalización por el técnico
     */
    public LocalDateTime getFechaFinalizTecnico() {
        return fechaFinalizTecnico;
    }

    /**
     * Establece la fecha de finalización por el técnico.
     * @param fechaFinalizTecnico Fecha de finalización por el técnico
     */
    public void setFechaFinalizTecnico(LocalDateTime fechaFinalizTecnico) {
        this.fechaFinalizTecnico = fechaFinalizTecnico;
    }

    /**
     * Obtiene el proceso realizado por el técnico.
     * @return Proceso realizado por el técnico
     */
    public String getProcRealizadoTecnico() {
        return procRealizadoTecnico;
    }

    /**
     * Establece el proceso realizado por el técnico.
     * @param procRealizadoTecnico Proceso realizado por el técnico
     */
    public void setProcRealizadoTecnico(String procRealizadoTecnico) {
        this.procRealizadoTecnico = procRealizadoTecnico;
    }

    /**
     * Obtiene el usuario que reporta la avería.
     * @return Código del usuario que reporta
     */
    public int getUsuarioReportaFK() {
        return usuarioReportaFK;
    }

    /**
     * Establece el usuario que reporta la avería.
     * @param usuarioReportaFK Código del usuario que reporta
     */
    public void setUsuarioReportaFK(int usuarioReportaFK) {
        this.usuarioReportaFK = usuarioReportaFK;
    }

    /**
     * Obtiene el técnico asignado a la avería.
     * @return Código del técnico asignado (puede ser null)
     */
    public Integer getUsuarioTecnicoFK() {
        return usuarioTecnicoFK;
    }

    /**
     * Establece el técnico asignado a la avería.
     * @param usuarioTecnicoFK Código del técnico asignado (puede ser null)
     */
    public void setUsuarioTecnicoFK(Integer usuarioTecnicoFK) {
        this.usuarioTecnicoFK = usuarioTecnicoFK;
    }

    /**
     * Obtiene la maquinaria asociada a la avería.
     * @return Código de la maquinaria
     */
    public int getMaquinariaFK() {
        return maquinariaFK;
    }

    /**
     * Establece la maquinaria asociada a la avería.
     * @param maquinariaFK Código de la maquinaria
     */
    public void setMaquinariaFK(int maquinariaFK) {
        this.maquinariaFK = maquinariaFK;
    }

    /**
     * Obtiene el tipo de avería.
     * @return Código del tipo de avería
     */
    public int getTipoAveriaFK() {
        return tipoAveriaFK;
    }

    /**
     * Establece el tipo de avería.
     * @param tipoAveriaFK Código del tipo de avería
     */
    public void setTipoAveriaFK(int tipoAveriaFK) {
        this.tipoAveriaFK = tipoAveriaFK;
    }
}
