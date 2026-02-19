/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import daoImpl.MaquinariaDAOimpl;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import modelo.Maquinaria;
import config.DataSourceFactory;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionMaquinasControlador {
    private MaquinariaDAOimpl mDAOi = new MaquinariaDAOimpl(DataSourceFactory.getDataSource());

    public GestionMaquinasControlador() {}
    
    //crear nueva máquina
    public boolean crearMaquina(String nombre, String status, String tipo, Date fechaAlta){
        boolean flag = true;
        int statusInt;
        int tipoInt;
        LocalDate fechaAltaLDate;
        //validar status
        switch (status) {

            case "Operativa":
                statusInt = 801;
                break;

            case "Averiada":
                statusInt = 802;
                break;

            case "Mantenimiento":
                statusInt = 803;
                break;
            case "Fuera":
                statusInt = 804;
                break;
            default:
                flag = false;
                return flag;
        }
        //validar tipo
        switch (tipo){
            case "Arranque":
                tipoInt = 301;
                break;
            case "Agujeros":
                tipoInt = 302;
                break;
            case "Abrasivo":
                tipoInt = 303;
                break;
            case "Corte":
                tipoInt = 304;
                break;
            case "Sin viruta":
                tipoInt = 305;
                break;
            case "Unión":
                tipoInt = 306;
                break;
            case "Tratamiento":
                tipoInt = 307;
                break;
            case "Específicos":
                tipoInt = 308;
                break;
            default:
                flag = false;
                return flag;
        }
        
        //validar fechaAlta
        fechaAltaLDate = fechaAlta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(fechaAltaLDate.isAfter(LocalDate.now())){
            flag = false;
            return flag;
        }
        //crear objeto modelo
        Maquinaria m = new Maquinaria(nombre, statusInt, fechaAltaLDate, null, tipoInt);
      
        //llamar a la daoimpl;
        mDAOi.insertar(m);
        //comunicarse con la vista
        return flag;
    }
}
