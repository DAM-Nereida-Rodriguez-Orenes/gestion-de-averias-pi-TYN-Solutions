/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app;

import config.DataSourceFactory;
import controlador.LoginControlador;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.swing.UIManager;
import vista.vLogin;
import com.formdev.flatlaf.FlatLightLaf;
import controlador.GestionUsuarioControlador;
import java.awt.Color;
import vista.vHomeAdmin;
import vista.vHomeOper;

/**
 *
 * @author yosnavmol
 */
public class MainApp {

    public static void main(String[] args) {

        System.out.println("--- 1. Iniciando prueba de conexion con HikariCP ---");

        try {
            //0) color de los place holder para toda la interfaz 
            UIManager.put("TextField.placeholderForeground", new Color(67,113,177));
            
            // 1) Obtener el DataSource (Hikari)
            DataSource dataSource = DataSourceFactory.getDataSource();

            // 2) Probar conexion (opcional, para ti)
            Connection con = dataSource.getConnection();

            if (con != null) {
                System.out.println("Conexion exitosa");
                System.out.println("Base de datos: " + con.getCatalog());
                System.out.println("Driver usado: " + con.getMetaData().getDriverName());
            }

            con.close();

            // 4) Crear controlador
            LoginControlador loginControlador = new LoginControlador();

            //libreria Flat para el diseño de la interfaz 
            UIManager.setLookAndFeel(new FlatLightLaf());

            // 5) Abrir vista login con el controlador y Comprobamos si ya hay una sesion activa 
            
            if (loginControlador.haySesionActiva() && loginControlador.getRolUsuario() != -1) {
                Integer rolUsuario = loginControlador.getRolUsuario();

                if (rolUsuario == 701) {
                    vHomeAdmin menuAdmin = new vHomeAdmin();
                    menuAdmin.setLocationRelativeTo(null);
                    menuAdmin.setVisible(true);
                } else {
                    vHomeOper menuOper = new vHomeOper();
                    menuOper.setLocationRelativeTo(null);
                    menuOper.setVisible(true);
                }
            } else {               
                vLogin login = new vLogin(loginControlador);
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            }         

        } catch (SQLException e) {
            System.err.println("Error de conexion:");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();

        } catch (RuntimeException e) {
            System.err.println("Error de configuracion (revisa application.properties):");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//main
}
