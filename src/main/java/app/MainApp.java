/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app;

import config.DataSourceFactory;
import java.sql.Connection;
import java.sql.SQLException;
import vista.vLogin;

/**
 *
 * @author yosnavmol
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("--- 1. Iniciando prueba de conexión con HikariCP ---");

        // Llamamos a tu clase DataSourceFactory
        try (Connection con = DataSourceFactory.getConnection()) {
            
            if (con != null) {
                System.out.println("✅ ¡CONEXIÓN EXITOSA!");
                System.out.println(" - Base de datos: " + con.getCatalog());
                System.out.println(" - Driver usado: " + con.getMetaData().getDriverName());
            }

        } catch (SQLException e) {
            System.err.println("❌ ERROR DE CONEXIÓN:");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
             System.err.println("❌ ERROR DE CONFIGURACIÓN (Revisa application.properties):");
             e.printStackTrace();
        }
        vLogin vLogin = new vLogin();
        vLogin.setVisible(true);
    }
    
}
