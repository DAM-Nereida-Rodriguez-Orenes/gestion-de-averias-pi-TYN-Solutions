/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

/**
 *
 * @author yosnavmol
 */
public class DataSourceFactory {
    private static HikariDataSource dataSource;

    // Bloque estático: Se ejecuta una sola vez al cargar la clase
    static {
        try {
            // 1. Cargar las propiedades del archivo
            Properties props = new Properties();
            try (InputStream input = DataSourceFactory.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (input == null) {
                    throw new RuntimeException("No se encontró el archivo application.properties");
                }
                props.load(input);
            }

            // 2. Configurar HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.username"));
            config.setPassword(props.getProperty("jdbc.password"));
            config.setDriverClassName(props.getProperty("jdbc.driver"));
            
            // Configuraciones extra del pool (opcionales)
            config.setMaximumPoolSize(10); // Máximo 10 conexiones simultáneas
            config.setPoolName("HikariPool-Averias");

            // 3. Crear el DataSource
            dataSource = new HikariDataSource(config);
            
            System.out.println("✅ Pool de conexiones HikariCP inicializado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error crítico iniciando el Pool de Datos: " + e.getMessage());
        }
    }

    // Constructor privado para que nadie haga 'new DataSourceFactory()'
    private DataSourceFactory() {}

    /**
     * Método público para obtener el DataSource.
     * Los DAO usarán esto para pedir conexiones.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Método utilitario para obtener una conexión directamente.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
