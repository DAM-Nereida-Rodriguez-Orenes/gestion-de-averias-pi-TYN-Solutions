/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.util.Objects;
/**
 * Clase que representa una maquinaria en el sistema.
 * Contiene información sobre su estado, tipo, fechas y nombre.
 *
 * @author Thanya
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
    /**
     * Constructor vacío. Crea una maquinaria sin inicializar sus atributos.
     */
    public Maquinaria() {
    }

    // Constructor completo
    /**
     * Constructor completo. Inicializa una maquinaria con todos sus atributos.
     * @param codigoMaquinaria Código identificador de la maquinaria
     * @param nombre Nombre de la maquinaria
     * @param codigoEstadoFK Código del estado asociado
     * @param fechaAlta Fecha de alta
     * @param fechaBaja Fecha de baja
     * @param tipoMaquinaria Tipo de maquinaria
     * @param estado Estado de la maquinaria
     */
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
    /**
     * Constructor sin ID. Inicializa una maquinaria sin código identificador.
     * @param nombre Nombre de la maquinaria
     * @param codigoEstadoFK Código del estado asociado
     * @param fechaAlta Fecha de alta
     * @param fechaBaja Fecha de baja
     * @param tipoMaquinaria Tipo de maquinaria
     * @param estado Estado de la maquinaria
     */
    public Maquinaria(String nombre, int codigoEstadoFK,
            LocalDate fechaAlta, LocalDate fechaBaja, TipoMaquinaria tipoMaquinaria, Estado estado) {
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
        this.tipoMaquinaria = tipoMaquinaria;
        this.estado = estado;
    }
    // Getters y setters
    /**
     * Obtiene el código identificador de la maquinaria.
     * @return Código de la maquinaria
     */
    public int getCodigoMaquinaria() {
        return codigoMaquinaria;
    }

    /**
     * Establece el código identificador de la maquinaria.
     * @param codigoMaquinaria Código de la maquinaria
     */
    public void setCodigoMaquinaria(int codigoMaquinaria) {
        this.codigoMaquinaria = codigoMaquinaria;
    }

    /**
     * Obtiene el nombre de la maquinaria.
     * @return Nombre de la maquinaria
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la maquinaria.
     * @param nombre Nombre de la maquinaria
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha de alta de la maquinaria.
     * @return Fecha de alta
     */
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Establece la fecha de alta de la maquinaria.
     * @param fechaAlta Fecha de alta
     */
    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Obtiene la fecha de baja de la maquinaria.
     * @return Fecha de baja
     */
    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    /**
     * Establece la fecha de baja de la maquinaria.
     * @param fechaBaja Fecha de baja
     */
    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    /**
     * Obtiene el tipo de maquinaria.
     * @return Tipo de maquinaria
     */
    public TipoMaquinaria getTipoMaquinaria() {
        return tipoMaquinaria;
    }

    /**
     * Establece el tipo de maquinaria.
     * @param tipoMaquinaria Tipo de maquinaria
     */
    public void setTipoMaquinaria(TipoMaquinaria tipoMaquinaria) {
        this.tipoMaquinaria = tipoMaquinaria;
    }

    /**
     * Obtiene el estado de la maquinaria.
     * @return Estado de la maquinaria
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la maquinaria.
     * @param estado Estado de la maquinaria
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Compara si dos objetos Maquinaria son iguales por su código identificador.
     * @param obj Objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
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

    /**
     * Devuelve el hashCode basado en el código de la maquinaria.
     * @return hashCode de la maquinaria
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigoMaquinaria);
    }
    
    /**
     * Devuelve la representación en texto de la maquinaria (nombre).
     * @return Nombre de la maquinaria
     */
    @Override
    public String toString() {
        return this.nombre; 
    }
}
