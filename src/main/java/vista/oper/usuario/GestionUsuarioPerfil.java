/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.oper.usuario;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import modelo.Usuario;
import utils.PanelImgFondo;
import vista.oper.averias.GestionAveriaOper;
import vista.vHomeOper;
import vista.vLogin;

/**
 *
 * @author Netri
 */
public class GestionUsuarioPerfil extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GestionUsuarioPerfil.class.getName());
    private final GestionUsuarioControlador gestionUsuarioControlador;
    //Ver contraseña
    private char echoCharOriginal;

    /**
     * Creates new form GestionUsuarioPerfil
     */
    public GestionUsuarioPerfil() {
        initComponents();

        gestionUsuarioControlador = new GestionUsuarioControlador();

        //Ver contraseña 
        echoCharOriginal = pwdPassword.getEchoChar();

        mostrarImagenes();
        mostrarDatos();
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

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 70, 34);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 32, 32);
        txtSaludo.setIcon(iconUsuarioAdmin);
        GestionUsuarioControlador userContr = new GestionUsuarioControlador();
        txtSaludo.setText("Hola, " + userContr.obtenerNombreUsuarioLogueado());
        txtSaludo.setHorizontalTextPosition(SwingConstants.LEFT);
        txtSaludo.setVerticalTextPosition(SwingConstants.CENTER);
        txtSaludo.setIconTextGap(8);

        // Icono inicial del boton ver password
        FlatSVGIcon iconoOjoCerrado = new FlatSVGIcon("recursos/iconos/ojoCerrado.svg", 32, 32);
        tgbtnVerPassword.setIcon(iconoOjoCerrado);
        tgbtnVerPassword.setContentAreaFilled(false);

        //ICONOS LtextField
        //Campo nombre y apeliido 
        FlatSVGIcon iconoUsuario = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 16, 16);
        txtNombre.putClientProperty("JTextField.leadingIcon", iconoUsuario);
        txtNombre.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtNombre.putClientProperty("JTextField.placeholderText", "Nombre: ");
        txtApellidos.putClientProperty("JTextField.leadingIcon", iconoUsuario);
        txtApellidos.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtApellidos.putClientProperty("JTextField.placeholderText", "Apellidos: ");
        //Telefono
        FlatSVGIcon iconoTelefono = new FlatSVGIcon("recursos/iconos/icTelefono.svg", 16, 16);
        txtTelefono.putClientProperty("JTextField.leadingIcon", iconoTelefono);
        txtTelefono.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTelefono.putClientProperty("JTextField.placeholderText", "Teléfono: ");
        // cbb Prefijos telefonos
        cbbPrefijosTelefonos.setSelectedItem("+34");
        cbbPrefijosTelefonos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean estaSeleccionado,
                    boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, estaSeleccionado, tieneFoco);

                if (indice == -1) {
                    etiqueta.setIcon(iconoTelefono);
                } else {
                    etiqueta.setIcon(null);
                }

                return etiqueta;
            }
        });
        //Correo
        FlatSVGIcon iconoEmail = new FlatSVGIcon("recursos/iconos/icnCorreo.svg", 16, 16);
        txtEmail.putClientProperty("JTextField.leadingIcon", iconoEmail);
        txtEmail.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtEmail.putClientProperty("JTextField.placeholderText", "Email: ");
        //contraseña
        FlatSVGIcon iconoPassword = new FlatSVGIcon("recursos/iconos/icnPassword.svg", 16, 16);
        pwdPassword.putClientProperty("JTextField.leadingIcon", iconoPassword);
        pwdPassword.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        pwdPassword.putClientProperty("JTextField.placeholderText", "Contraseña:  ");
        //cbb Tipo de trabajador txtRol
        FlatSVGIcon iconoTipoTrabajador = new FlatSVGIcon("recursos/iconos/icnTpUsuario.svg", 16, 16);
        txtRol.putClientProperty("JTextField.leadingIcon", iconoTipoTrabajador);
        txtRol.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtRol.putClientProperty("JTextField.placeholderText", "Tipo de trabajador: ");

    }

    private void mostrarDatos() {
        LoginControlador loginControlador = new LoginControlador();
        Usuario usuarioSesion = loginControlador.getUsuarioSesion();

        if (usuarioSesion == null) {
            JOptionPane.showMessageDialog(this,
                    "No se ha podido cargar el usuario logueado",
                    "Error de sesion",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        txtNombre.setText(usuarioSesion.getNombre());
        txtNombre.setEditable(false);
        txtNombre.setEnabled(false);

        txtApellidos.setText(usuarioSesion.getApellido());
        txtApellidos.setEditable(false);
        txtApellidos.setEnabled(false);

        txtEmail.setText(usuarioSesion.getEmail());
        txtEmail.setEditable(false);
        txtEmail.setEnabled(false);

        txtRol.setText(usuarioSesion.getRol().getDescripcionRol());
        txtRol.setEditable(false);
        txtRol.setEnabled(false);

        String telefono = usuarioSesion.getTelefono();
        String prefijoEncontrado = "";
        String numero = telefono;

        if (telefono != null) {
            for (int i = 0; i < cbbPrefijosTelefonos.getItemCount(); i++) {
                String prefijo = cbbPrefijosTelefonos.getItemAt(i);

                if (telefono.startsWith(prefijo)) {
                    prefijoEncontrado = prefijo;
                    numero = telefono.substring(prefijo.length());
                    break;
                }
            }
        }

        cbbPrefijosTelefonos.setSelectedItem(prefijoEncontrado);
        txtTelefono.setText(numero != null ? numero : "");
        pwdPassword.setText(usuarioSesion.getPassword());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jpCabecera = new PanelImgFondo("/recursos/fondoFormularios2.png");
        jlLogo = new javax.swing.JLabel();
        txtSaludo = new javax.swing.JLabel();
        panelTitulo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelFormulario = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        pwdPassword = new javax.swing.JPasswordField();
        btnGenerarPassword = new javax.swing.JButton();
        cbbPrefijosTelefonos = new javax.swing.JComboBox<>();
        txtTelefono = new javax.swing.JTextField();
        tgbtnVerPassword = new javax.swing.JToggleButton();
        txtRol = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnActualizarDatosUsuario = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        miMenuPrincipal = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        miCerrarSesion = new javax.swing.JMenuItem();
        miSalirApp = new javax.swing.JMenuItem();
        miPerfil1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        miPerfil = new javax.swing.JMenuItem();
        miGestionAverias = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jpCabecera.setPreferredSize(new java.awt.Dimension(400, 50));

        jlLogo.setText("jLabel2");

        txtSaludo.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        txtSaludo.setForeground(new java.awt.Color(67, 113, 177));
        txtSaludo.setText("Hola, Admin");

        javax.swing.GroupLayout jpCabeceraLayout = new javax.swing.GroupLayout(jpCabecera);
        jpCabecera.setLayout(jpCabeceraLayout);
        jpCabeceraLayout.setHorizontalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSaludo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jpCabeceraLayout.setVerticalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlLogo)
                    .addComponent(txtSaludo))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelTitulo.setBackground(new java.awt.Color(204, 204, 204));
        panelTitulo.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Información personal");

        javax.swing.GroupLayout panelTituloLayout = new javax.swing.GroupLayout(panelTitulo);
        panelTitulo.setLayout(panelTituloLayout);
        panelTituloLayout.setHorizontalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelTituloLayout.setVerticalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelFormulario.setBackground(new java.awt.Color(204, 204, 204));
        panelFormulario.setOpaque(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtNombre.setEditable(false);
        txtNombre.setBackground(new java.awt.Color(237, 243, 251));
        txtNombre.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(67, 113, 177));

        txtApellidos.setEditable(false);
        txtApellidos.setBackground(new java.awt.Color(237, 243, 251));
        txtApellidos.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtApellidos.setForeground(new java.awt.Color(67, 113, 177));

        txtEmail.setEditable(false);
        txtEmail.setBackground(new java.awt.Color(237, 243, 251));
        txtEmail.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 14)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(67, 113, 177));

        pwdPassword.setBackground(new java.awt.Color(237, 243, 251));
        pwdPassword.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        pwdPassword.setForeground(new java.awt.Color(67, 113, 177));

        btnGenerarPassword.setBackground(new java.awt.Color(237, 243, 251));
        btnGenerarPassword.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        btnGenerarPassword.setForeground(new java.awt.Color(67, 113, 177));
        btnGenerarPassword.setText("Generar Contraseña");
        btnGenerarPassword.setBorderPainted(false);
        btnGenerarPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPasswordActionPerformed(evt);
            }
        });

        cbbPrefijosTelefonos.setBackground(new java.awt.Color(237, 243, 251));
        cbbPrefijosTelefonos.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        cbbPrefijosTelefonos.setForeground(new java.awt.Color(67, 113, 177));
        cbbPrefijosTelefonos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+43", "+32", "+359", "+385", "+357", "+420", "+45", "+372", "+358", "+33", "+49", "+30", "+36", "+353", "+39", "+371", "+370", "+352", "+356", "+31", "+48", "+351", "+40", "+421", "+386", "+34", "+46", "+55", "+81", "+61" }));

        txtTelefono.setBackground(new java.awt.Color(237, 243, 251));
        txtTelefono.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(67, 113, 177));

        tgbtnVerPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgbtnVerPasswordActionPerformed(evt);
            }
        });

        txtRol.setBackground(new java.awt.Color(237, 243, 251));
        txtRol.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtRol.setForeground(new java.awt.Color(67, 113, 177));
        txtRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRolActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmail)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbPrefijosTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tgbtnVerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnGenerarPassword))))
                    .addComponent(txtRol))
                .addGap(114, 114, 114))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(txtRol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnGenerarPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tgbtnVerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbPrefijosTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setOpaque(false);

        btnActualizarDatosUsuario.setBackground(new java.awt.Color(58, 181, 235));
        btnActualizarDatosUsuario.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnActualizarDatosUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarDatosUsuario.setText("Guardar");
        btnActualizarDatosUsuario.setBorderPainted(false);
        btnActualizarDatosUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(887, Short.MAX_VALUE)
                .addComponent(btnActualizarDatosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnActualizarDatosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 1199, Short.MAX_VALUE)
            .addComponent(panelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(panelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        miMenuPrincipal.setText("Inicio");

        jMenuItem1.setText("Menú principal");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        miMenuPrincipal.add(jMenuItem1);

        miCerrarSesion.setText("Cerrar sesión");
        miCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCerrarSesionActionPerformed(evt);
            }
        });
        miMenuPrincipal.add(miCerrarSesion);

        miSalirApp.setText("Cerrar Fixora");
        miSalirApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalirAppActionPerformed(evt);
            }
        });
        miMenuPrincipal.add(miSalirApp);

        miPerfil1.setText("Perfil");
        miPerfil1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPerfil1ActionPerformed(evt);
            }
        });
        miMenuPrincipal.add(miPerfil1);

        jMenuBar1.add(miMenuPrincipal);

        jMenu2.setText("Gestiones");

        miPerfil.setText("Perfil");
        miPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPerfilActionPerformed(evt);
            }
        });
        jMenu2.add(miPerfil);

        miGestionAverias.setText("Averías");
        miGestionAverias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGestionAveriasActionPerformed(evt);
            }
        });
        jMenu2.add(miGestionAverias);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        vHomeOper home = new vHomeOper();
        home.setLocationRelativeTo(null);
        home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed
        LoginControlador loginControlador = new LoginControlador();
        vLogin login = new vLogin(loginControlador);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miCerrarSesionActionPerformed

    private void miSalirAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirAppActionPerformed
        System.exit(0);
    }//GEN-LAST:event_miSalirAppActionPerformed

    private void miGestionAveriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGestionAveriasActionPerformed
        GestionAveriaOper gestionAveria = new GestionAveriaOper();
        gestionAveria.setLocationRelativeTo(null);
        gestionAveria.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miGestionAveriasActionPerformed

    private void miPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPerfilActionPerformed
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }//GEN-LAST:event_miPerfilActionPerformed

    private void btnGenerarPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPasswordActionPerformed
        String nuevaPassword = gestionUsuarioControlador.generarPasswordAleatoria();
        pwdPassword.setText(nuevaPassword);
    }//GEN-LAST:event_btnGenerarPasswordActionPerformed

    private void btnActualizarDatosUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosUsuarioActionPerformed

        //obtenemso el valor del cbbox
        String email = txtEmail.getText().trim();
        String rol = txtRol.getText().trim();
        int selectPrefijo = cbbPrefijosTelefonos.getSelectedIndex();
        String prefijo = cbbPrefijosTelefonos.getItemAt(selectPrefijo);
        String telefono = txtTelefono.getText().trim();
        telefono = prefijo + telefono;
        // obtener password del JPasswordField
        char[] passwordArray = pwdPassword.getPassword();
        // convertir a String
        String password = new String(passwordArray).trim();

        if (telefono.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes rellenar todos los campos del formulario", "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            boolean operacionExitosa = gestionUsuarioControlador.actualizarDatosUsuario(null, null, rol, telefono, email, password, null);

            if (operacionExitosa) {
                JOptionPane.showMessageDialog(this, "Datos del usuario actualizado", "Actualización realizada", JOptionPane.INFORMATION_MESSAGE);

                //cierro el modal
                this.dispose();
            }
        }
    }//GEN-LAST:event_btnActualizarDatosUsuarioActionPerformed

    private void tgbtnVerPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgbtnVerPasswordActionPerformed

        if (tgbtnVerPassword.isSelected()) {
            pwdPassword.setEchoChar((char) 0);
            FlatSVGIcon iconoOjoAbierto = new FlatSVGIcon("recursos/iconos/ojoAbierto.svg", 32, 32);
            tgbtnVerPassword.setIcon(iconoOjoAbierto);
            tgbtnVerPassword.setContentAreaFilled(false);

        } else {
            pwdPassword.setEchoChar(echoCharOriginal);
            FlatSVGIcon iconoOjoCerrado = new FlatSVGIcon("recursos/iconos/ojoCerrado.svg", 32, 32);
            tgbtnVerPassword.setIcon(iconoOjoCerrado);
        }
    }//GEN-LAST:event_tgbtnVerPasswordActionPerformed

    private void txtRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRolActionPerformed

    private void miPerfil1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPerfil1ActionPerformed
        GestionUsuarioPerfil gestionUsuarioPerfil = new GestionUsuarioPerfil();
        gestionUsuarioPerfil.setLocationRelativeTo(null);
        gestionUsuarioPerfil.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miPerfil1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarDatosUsuario;
    private javax.swing.JButton btnGenerarPassword;
    private javax.swing.JComboBox<String> cbbPrefijosTelefonos;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JPanel jpCabecera;
    private javax.swing.JMenuItem miCerrarSesion;
    private javax.swing.JMenuItem miGestionAverias;
    private javax.swing.JMenu miMenuPrincipal;
    private javax.swing.JMenuItem miPerfil;
    private javax.swing.JMenuItem miPerfil1;
    private javax.swing.JMenuItem miSalirApp;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JToggleButton tgbtnVerPassword;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRol;
    private javax.swing.JLabel txtSaludo;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
