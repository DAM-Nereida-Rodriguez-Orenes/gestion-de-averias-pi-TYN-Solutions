/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author Thanya
 */
public class PanelImgFondo extends JPanel {

    private final Image imagen;

    public PanelImgFondo(String rutaImagen) {
        URL url = getClass().getResource(rutaImagen);

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            imagen = icon.getImage();
        } else {
            imagen = null;
            System.out.println("No se encontro la imagen en: " + rutaImagen);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
