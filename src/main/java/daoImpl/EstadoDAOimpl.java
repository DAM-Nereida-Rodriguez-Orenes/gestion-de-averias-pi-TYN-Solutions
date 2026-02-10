/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpl;
import dao.EstadoDAO;
import java.util.List;
import javax.sql.DataSource;
import modelo.Estado;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class EstadoDAOimpl implements EstadoDAO{
    private DataSource dataSource;
    
    public EstadoDAOimpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void insertar(Estado e) {
        //recuerda que aquí los id no son autoincrementales
        //800 + X (80 lbl + variable jInput X)
        //comprobar si existe (método aparte)
        
    }

    @Override
    public void modificar(Estado e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(Estado e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Estado> listarEstado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existeID(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
