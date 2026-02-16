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
import vista.vLogin;

/**
 *
 * @author yosnavmol
 */
public class MainApp {

    public static void main(String[] args) {

        System.out.println("--- 1. Iniciando prueba de conexion con HikariCP ---");

        try {

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

            // 3) Crear DAO
            UsuarioDao usuarioDao = new UsuarioDaoImpl(dataSource);

            // 4) Crear controlador
            LoginControlador loginControlador = new LoginControlador(usuarioDao);

            // 5) Abrir vista login con el controlador
            vLogin vLogin = new vLogin(loginControlador);
            vLogin.setVisible(true);

        } catch (SQLException e) {
            System.err.println("Error de conexion:");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();

        } catch (RuntimeException e) {
            System.err.println("Error de configuracion (revisa application.properties):");
            e.printStackTrace();
        }
    }
}
