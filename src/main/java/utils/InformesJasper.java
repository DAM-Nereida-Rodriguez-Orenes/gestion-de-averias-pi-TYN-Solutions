/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import config.DataSourceFactory;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Netri
 */
public class InformesJasper {

    public String generarInformeAveria(int idAveria) throws Exception {
        if (idAveria <= 0) {
            throw new IllegalArgumentException("El id de la averia no es valido.");
        }

        // 1. Ruta del archivo .jasper dentro de resources
        //este archivo NO es el PDF final, sino la plantilla del informe, el diseno.
        InputStream archivoJasper = getClass().getResourceAsStream("/informesGenerados/informe_averia.jasper");

        if (archivoJasper == null) {
            throw new Exception("No se encontro el archivo Jasper del informe.");
        }

        // 2. Parametros que recibira el informe
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("idAveria", idAveria);

        // 3. Crear carpeta de salida si no existe
        File carpetaInformes = new File(System.getProperty("user.dir") + "/informesPDFGenerados"); //así siempre lo crea en la raíz del proyecto, no depende de dónde ejecutes
        if (!carpetaInformes.exists()) {
            carpetaInformes.mkdirs();
        }

        // 4. Nombre unico para el PDF generado
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHora = LocalDateTime.now().format(formateador);

        String nombreArchivo = "informe_averia_" + idAveria + "_" + fechaHora + ".pdf";
        String rutaCompleta = new File(carpetaInformes, nombreArchivo).getAbsolutePath();

        // 5. Rellenar el informe y exportarlo a PDF
        try (Connection conexion = DataSourceFactory.getConnection()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(archivoJasper, parametros, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaCompleta);
        }

        return rutaCompleta;
    }

    public String generarInformeMaquinasPorEstado(int idEstado) throws Exception {
        if (idEstado <= 0) {
            throw new IllegalArgumentException("El id del estado no es valido.");
        }

        // Cargamos la plantilla .jasper desde resources
        InputStream archivoJasper = getClass().getResourceAsStream("/informeJasper/informe_maquinas_estado.jasper");

        if (archivoJasper == null) {
            throw new Exception("No se encontro el archivo Jasper del informe de maquinas por estado.");
        }

        // Parametros que recibira el informe
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("idEstado", idEstado);

        // Crear carpeta de salida si no existe
        File carpetaInformes = new File(System.getProperty("user.dir") + "/informesPDFGenerados");
        if (!carpetaInformes.exists()) {
            carpetaInformes.mkdirs();
        }

        // Nombre unico para el PDF generado
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHora = LocalDateTime.now().format(formateador);

        String nombreArchivo = "informe_maquinas_estado_" + idEstado + "_" + fechaHora + ".pdf";
        String rutaCompleta = new File(carpetaInformes, nombreArchivo).getAbsolutePath();

        // Rellenar el informe y exportarlo a PDF
        try (Connection conexion = DataSourceFactory.getConnection()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(archivoJasper, parametros, conexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaCompleta);
        }

        return rutaCompleta;
    }

}
