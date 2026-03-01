/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import modelo.Rol;

/**
 *
 * @author Thanya
 */
public class Usuario {

    // Atributos
    private int codigoUsuario;
    private String nombre;
    private String apellido;
    private Rol rol;
    private String telefono;
    private String email;
    private String password;
    private int intentos;
    private LocalDateTime ultimoAcceso;
    private Boolean activo;

    // Constructor vacio
    public Usuario() {
    }

    // Constructor completo
    public Usuario(String nombre, String apellido, Rol codigoRolFK,
            String telefono, String email, String password, int intentos,
            LocalDateTime ultimoAcceso, boolean activo) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = codigoRolFK;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.intentos = intentos;
        this.ultimoAcceso = ultimoAcceso;
        this.activo = activo;
    }

    // Getters y setters
    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
