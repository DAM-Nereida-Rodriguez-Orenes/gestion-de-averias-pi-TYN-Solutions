/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import utils.PanelImgFondo;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import modelo.Usuario;
import vista.admin.averia.GestionAveriaListar;
import vista.admin.maquinas.GestionMaquinas;
import vista.admin.usuario.GestionRol;
import vista.admin.usuario.GestionUsuario;
import vista.oper.usuario.GestionUsuarioPerfil;

/**
 *
 * @author Asus
 */
public class vHomeAdmin extends javax.swing.JFrame {

    /**
     * Creates new form vHome
     *
     * @param usuario
     * @param gestionUsuario
     */
    public vHomeAdmin() {
        initComponents();
        this.setLocationRelativeTo(null);
        mostrarImagenes();
        // Abrir la ventana maximizada
        // this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }

    public void mostrarImagenes() {
        //Ajustes del deisño del JFrame
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        // Tamaño inical de todas las ventanas
        this.setSize(1200, 800);
        // No se puede hacer más pequeña de 1200,800
        this.setMinimumSize(new Dimension(1200, 800));
        // Permite usar el botón de maximizar
        this.setResizable(true);
        // Centrar ventana en pantalla
        this.setLocationRelativeTo(null);

        URL urlLogo = getClass().getClassLoader().getResource("recursos/logos/fixora_logo_140x70.svg");
        System.out.println("urlLogo = " + urlLogo);

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 140, 70);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

        //iconos de los botones-Menus
        //Boton gestion usuario
        FlatSVGIcon iconoUsuario = new FlatSVGIcon("recursos/iconos/icon_users_exact.svg", 100, 100);
        btnGesUsuario.setIcon(iconoUsuario);
        btnGesUsuario.setText("Gestión de Usuario");
        btnGesUsuario.setHorizontalTextPosition(SwingConstants.CENTER);
        btnGesUsuario.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnGesUsuario.setIconTextGap(10);
        btnGesUsuario.setForeground(new Color(67, 113, 177));
        btnGesUsuario.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));

        //boton maquina
        FlatSVGIcon iconoMaquina = new FlatSVGIcon("recursos/iconos/engranajes_exact.svg", 100, 100);
        btnGesMaquina.setIcon(iconoMaquina);
        btnGesMaquina.setText("Gestión de Maquina");
        btnGesMaquina.setHorizontalTextPosition(SwingConstants.CENTER);
        btnGesMaquina.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnGesMaquina.setIconTextGap(10);
        btnGesMaquina.setForeground(new Color(67, 113, 177));
        btnGesMaquina.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
        //boton averia
        FlatSVGIcon iconAveria = new FlatSVGIcon("recursos/iconos/llave_exact.svg", 100, 100);
        btnGesAveria.setIcon(iconAveria);
        btnGesAveria.setText("Gestión de Averia");
        btnGesAveria.setHorizontalTextPosition(SwingConstants.CENTER);
        btnGesAveria.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnGesAveria.setIconTextGap(10);
        btnGesAveria.setForeground(new Color(67, 113, 177));
        btnGesAveria.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));

        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 32, 32);
        jLabel2.setIcon(iconUsuarioAdmin);
        GestionUsuarioControlador userContr = new GestionUsuarioControlador();
        jLabel2.setText("Hola, " + userContr.obtenerNombreUsuarioLogueado());
        jLabel2.setHorizontalTextPosition(SwingConstants.LEFT);
        jLabel2.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel2.setIconTextGap(8);

        //icono restablecer contraseña  
        FlatSVGIcon iconoPassword = new FlatSVGIcon("recursos/iconos/password.svg", 100, 100);
        btnrestablecerPassword.setIcon(iconoPassword);
        btnrestablecerPassword.setText("Restablecer contraseñas");
        btnrestablecerPassword.setHorizontalTextPosition(SwingConstants.CENTER);
        btnrestablecerPassword.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnrestablecerPassword.setIconTextGap(10);
        btnrestablecerPassword.setForeground(new Color(67, 113, 177));
        btnrestablecerPassword.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));

        //icono restablecer contraseña  
        FlatSVGIcon iconoinforme = new FlatSVGIcon("recursos/iconos/informe.svg", 100, 100);
        btnGenerarInforme.setIcon(iconoinforme);
        btnGenerarInforme.setText("Informes generados");
        btnGenerarInforme.setHorizontalTextPosition(SwingConstants.CENTER);
        btnGenerarInforme.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnGenerarInforme.setIconTextGap(10);
        btnGenerarInforme.setForeground(new Color(67, 113, 177));
        btnGenerarInforme.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFondo = new PanelImgFondo("/recursos/fondoFormularios.png");
        jpCabecera = new PanelImgFondo("/recursos/fondoFormularios2.png");
        jlLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jlIcnUsuario = new javax.swing.JLabel();
        panelTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnGesUsuario = new javax.swing.JButton();
        btnGesMaquina = new javax.swing.JButton();
        btnGesAveria = new javax.swing.JButton();
        btnrestablecerPassword = new javax.swing.JButton();
        btnGenerarInforme = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        miInicio = new javax.swing.JMenu();
        miCerrarSesion = new javax.swing.JMenuItem();
        miSalirApp = new javax.swing.JMenuItem();
        miPerfil = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inicio | Gestión de averías");

        panelFondo.setPreferredSize(new java.awt.Dimension(1200, 800));

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        jlLogo.setText("jLabel2");

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(67, 113, 177));
        jLabel2.setText("Hola, Admin");

        javax.swing.GroupLayout jpCabeceraLayout = new javax.swing.GroupLayout(jpCabecera);
        jpCabecera.setLayout(jpCabeceraLayout);
        jpCabeceraLayout.setHorizontalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlIcnUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(86, 86, 86))
        );
        jpCabeceraLayout.setVerticalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpCabeceraLayout.createSequentialGroup()
                        .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlLogo)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jlIcnUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelTitulo.setBackground(new java.awt.Color(153, 153, 153));
        panelTitulo.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("¡Bienvenido Administrador!");

        javax.swing.GroupLayout panelTituloLayout = new javax.swing.GroupLayout(panelTitulo);
        panelTitulo.setLayout(panelTituloLayout);
        panelTituloLayout.setHorizontalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelTituloLayout.setVerticalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        panelAcciones.setBackground(new java.awt.Color(153, 153, 153));
        panelAcciones.setOpaque(false);

        btnGesUsuario.setBackground(new java.awt.Color(234, 242, 251));
        btnGesUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(183, 206, 251)));
        btnGesUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGesUsuarioActionPerformed(evt);
            }
        });

        btnGesMaquina.setBackground(new java.awt.Color(234, 242, 251));
        btnGesMaquina.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(183, 206, 251)));
        btnGesMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGesMaquinaActionPerformed(evt);
            }
        });

        btnGesAveria.setBackground(new java.awt.Color(234, 242, 251));
        btnGesAveria.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(183, 206, 251)));
        btnGesAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGesAveriaActionPerformed(evt);
            }
        });

        btnrestablecerPassword.setBackground(new java.awt.Color(234, 242, 251));
        btnrestablecerPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(183, 206, 251)));
        btnrestablecerPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrestablecerPasswordActionPerformed(evt);
            }
        });

        btnGenerarInforme.setBackground(new java.awt.Color(234, 242, 251));
        btnGenerarInforme.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(183, 206, 251)));
        btnGenerarInforme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarInformeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap(197, Short.MAX_VALUE)
                .addComponent(btnGesUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(btnGesMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(btnGesAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(196, Short.MAX_VALUE))
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnrestablecerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGenerarInforme, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGesMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGesAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGesUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnrestablecerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerarInforme, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFondoLayout = new javax.swing.GroupLayout(panelFondo);
        panelFondo.setLayout(panelFondoLayout);
        panelFondoLayout.setHorizontalGroup(
            panelFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAcciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFondoLayout.setVerticalGroup(
            panelFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFondoLayout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(panelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        miInicio.setText("Inicio");

        miCerrarSesion.setText("Cerrar sesión");
        miCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCerrarSesionActionPerformed(evt);
            }
        });
        miInicio.add(miCerrarSesion);

        miSalirApp.setText("Cerrar Fixora");
        miSalirApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalirAppActionPerformed(evt);
            }
        });
        miInicio.add(miSalirApp);

        miPerfil.setText("Perfil");
        miPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPerfilActionPerformed(evt);
            }
        });
        miInicio.add(miPerfil);

        jMenuBar1.add(miInicio);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed
        LoginControlador loginControlador = new LoginControlador();
        vLogin login = new vLogin(loginControlador);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miCerrarSesionActionPerformed

    private void btnGesUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGesUsuarioActionPerformed
        GestionUsuarioControlador gestionUsuarioControlador = new GestionUsuarioControlador();
        GestionUsuario gestionUsuario = new GestionUsuario(gestionUsuarioControlador);
        gestionUsuario.setLocationRelativeTo(null);
        gestionUsuario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnGesUsuarioActionPerformed

    private void btnGesMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGesMaquinaActionPerformed
        GestionMaquinas gestionMaquina = new GestionMaquinas();
        gestionMaquina.setLocationRelativeTo(null);
        gestionMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnGesMaquinaActionPerformed

    private void btnGesAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGesAveriaActionPerformed
        GestionAveriaListar gestionAveria = new GestionAveriaListar();
        gestionAveria.setLocationRelativeTo(null);
        gestionAveria.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnGesAveriaActionPerformed

    private void miSalirAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirAppActionPerformed
        System.exit(0);
    }//GEN-LAST:event_miSalirAppActionPerformed

    private void btnrestablecerPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrestablecerPasswordActionPerformed
        RestablecerPassword restablecer = new RestablecerPassword(this, true);
        restablecer.setLocationRelativeTo(null);
        restablecer.setVisible(true);
    }//GEN-LAST:event_btnrestablecerPasswordActionPerformed

    private void btnGenerarInformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarInformeActionPerformed
        TablaInformes tablaInformes = new TablaInformes(this, true);
        tablaInformes.setLocationRelativeTo(null);
        tablaInformes.setVisible(true);
    }//GEN-LAST:event_btnGenerarInformeActionPerformed

    private void miPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPerfilActionPerformed
        GestionUsuarioPerfil gestionUsuarioPerfil = new GestionUsuarioPerfil();
        gestionUsuarioPerfil.setLocationRelativeTo(null);
        gestionUsuarioPerfil.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miPerfilActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerarInforme;
    private javax.swing.JButton btnGesAveria;
    private javax.swing.JButton btnGesMaquina;
    private javax.swing.JButton btnGesUsuario;
    private javax.swing.JButton btnrestablecerPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel jlIcnUsuario;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JPanel jpCabecera;
    private javax.swing.JMenuItem miCerrarSesion;
    private javax.swing.JMenu miInicio;
    private javax.swing.JMenuItem miPerfil;
    private javax.swing.JMenuItem miSalirApp;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFondo;
    private javax.swing.JPanel panelTitulo;
    // End of variables declaration//GEN-END:variables
}
