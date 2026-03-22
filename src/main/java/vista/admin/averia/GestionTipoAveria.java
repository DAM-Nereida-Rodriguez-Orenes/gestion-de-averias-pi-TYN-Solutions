 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import vista.PanelImgFondo;
import vista.admin.maquinas.GestionEstadoMaquina;
import vista.admin.maquinas.GestionMaquinas;
import vista.admin.maquinas.GestionTipoMaquina;
import vista.admin.usuario.GestionRol;
import vista.admin.usuario.GestionUsuario;
import vista.vHomeAdmin;
import vista.vLogin;

/**
 *
 * @author Netri
 */
public class GestionTipoAveria extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GestionTipoAveria.class.getName());
    // 1. Instanciamos nuestro nuevo controlador y el modelo de la tabla
    private final controlador.TipoAveriaControlador controlador = new controlador.TipoAveriaControlador();
    private javax.swing.table.DefaultTableModel modeloTabla;

    /**
     * Creates new form GestionTipoAveria
     */
    public GestionTipoAveria() {
        initComponents();
        // Cargar datos
        inicializarTabla();
        cargarDatos();
        mostrarImagenes();
    }

    /**
     * mostrarImagenes(). Metodo encargado de aplicar los ajustes visuales de la
     * ventana.
     *
     * En este metodo se configuran los elementos graficos comunes de la
     * interfaz, como el icono del JFrame, el tamaño fijo de la ventana, su
     * posicion en pantalla y la desactivacion del redimensionado.
     *
     * Tambien se cargan y asignan los iconos SVG utilizados en la interfaz,
     * como el logo de la aplicacion o los iconos de usuario. Ademas se ajustan
     * propiedades visuales de los componentes Swing (labels, textfields, etc.)
     * para mantener un diseño uniforme en todas las pantallas de la aplicacion.
     */
    public void mostrarImagenes() {
        //Ajustes del deisño del JFrame/Layout
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

        //LOGO
        URL urlLogo = getClass().getClassLoader().getResource("recursos/logos/fixora_logo_140x70.svg");
        System.out.println("urlLogo = " + urlLogo);

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 70, 34);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 24, 24);
        jlSaldoIcono.setIcon(iconUsuarioAdmin);
        GestionUsuarioControlador userContr = new GestionUsuarioControlador();
        jlSaldoIcono.setText("Hola, " + userContr.obtenerNombreUsuarioLogueado() );
        jlSaldoIcono.setHorizontalTextPosition(SwingConstants.LEFT);
        jlSaldoIcono.setVerticalTextPosition(SwingConstants.CENTER);
        jlSaldoIcono.setIconTextGap(8);
    }

    private void inicializarTabla() {
        String[] columnas = {"Cód. Tipo", "Descripción", "Tiempo Prom. (Horas)"};
        modeloTabla = new javax.swing.table.DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que se edite directamente haciendo doble clic
            }
        };
        tablaAveria.setModel(modeloTabla);
        //Ajustes al diseño de la tabla
        tablaAveria.setRowHeight(36); // este valor aumenta el tamaño de las tuplas
        tablaAveria.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); //esto aumneta el tamaño de la fuente de la tabla y cambia la fuente
        tablaAveria.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); // esto aumenta el tamaño de la fuente del header
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        java.util.List<Object[]> datos = controlador.listarParaTabla();
        if (datos != null) {
            for (Object[] fila : datos) {
                modeloTabla.addRow(fila);
            }
        }
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
        jlSaldoIcono = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnTipoAveriaActualizar = new javax.swing.JButton();
        btnTipoAveriaEliminar = new javax.swing.JButton();
        panelFiltros = new javax.swing.JPanel();
        btnTipoAveriaNuevo = new javax.swing.JButton();
        panelTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAveria = new javax.swing.JTable();
        panelTitulo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        miInicio = new javax.swing.JMenu();
        miMenuPrincipal = new javax.swing.JMenuItem();
        miCerrarSesion = new javax.swing.JMenuItem();
        miSalirApp = new javax.swing.JMenuItem();
        miGestion = new javax.swing.JMenu();
        miAveria = new javax.swing.JMenuItem();
        miUsuario = new javax.swing.JMenuItem();
        miMaquinaria = new javax.swing.JMenuItem();
        miTipoMaquinaria = new javax.swing.JMenuItem();
        miEstadoMaquinaria = new javax.swing.JMenuItem();
        miTipoAveria = new javax.swing.JMenuItem();
        miRoles = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelFondo.setBackground(new java.awt.Color(204, 204, 204));

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jpCabecera.setOpaque(false);

        jlLogo.setText("jLabel2");

        jlSaldoIcono.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        jlSaldoIcono.setForeground(new java.awt.Color(67, 113, 177));
        jlSaldoIcono.setText("Hola, Admin");

        javax.swing.GroupLayout jpCabeceraLayout = new javax.swing.GroupLayout(jpCabecera);
        jpCabecera.setLayout(jpCabeceraLayout);
        jpCabeceraLayout.setHorizontalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlSaldoIcono)
                .addGap(86, 86, 86))
        );
        jpCabeceraLayout.setVerticalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlLogo)
                    .addComponent(jlSaldoIcono))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAcciones.setBackground(new java.awt.Color(153, 153, 153));
        panelAcciones.setOpaque(false);

        btnTipoAveriaActualizar.setBackground(new java.awt.Color(234, 242, 251));
        btnTipoAveriaActualizar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnTipoAveriaActualizar.setForeground(new java.awt.Color(67, 113, 177));
        btnTipoAveriaActualizar.setText("Editar");
        btnTipoAveriaActualizar.setBorderPainted(false);
        btnTipoAveriaActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAveriaActualizarActionPerformed(evt);
            }
        });

        btnTipoAveriaEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnTipoAveriaEliminar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnTipoAveriaEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnTipoAveriaEliminar.setText("Eliminar Tipo");
        btnTipoAveriaEliminar.setBorderPainted(false);
        btnTipoAveriaEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAveriaEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTipoAveriaEliminar)
                .addGap(807, 807, 807)
                .addComponent(btnTipoAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTipoAveriaEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTipoAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelFiltros.setBackground(new java.awt.Color(153, 153, 153));
        panelFiltros.setOpaque(false);

        btnTipoAveriaNuevo.setBackground(new java.awt.Color(58, 181, 235));
        btnTipoAveriaNuevo.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnTipoAveriaNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnTipoAveriaNuevo.setText("+ Nuevo Tipo ");
        btnTipoAveriaNuevo.setBorderPainted(false);
        btnTipoAveriaNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAveriaNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFiltrosLayout = new javax.swing.GroupLayout(panelFiltros);
        panelFiltros.setLayout(panelFiltrosLayout);
        panelFiltrosLayout.setHorizontalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltrosLayout.createSequentialGroup()
                .addContainerGap(997, Short.MAX_VALUE)
                .addComponent(btnTipoAveriaNuevo)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        panelFiltrosLayout.setVerticalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltrosLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(btnTipoAveriaNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelTabla.setBackground(new java.awt.Color(153, 153, 153));
        panelTabla.setOpaque(false);

        tablaAveria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaAveria);

        javax.swing.GroupLayout panelTablaLayout = new javax.swing.GroupLayout(panelTabla);
        panelTabla.setLayout(panelTablaLayout);
        panelTablaLayout.setHorizontalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTablaLayout.setVerticalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelTitulo.setBackground(new java.awt.Color(153, 153, 153));
        panelTitulo.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Gestión de Tipos de Averías");

        javax.swing.GroupLayout panelTituloLayout = new javax.swing.GroupLayout(panelTitulo);
        panelTitulo.setLayout(panelTituloLayout);
        panelTituloLayout.setHorizontalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelTituloLayout.setVerticalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFondoLayout = new javax.swing.GroupLayout(panelFondo);
        panelFondo.setLayout(panelFondoLayout);
        panelFondoLayout.setHorizontalGroup(
            panelFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelFiltros, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFondoLayout.setVerticalGroup(
            panelFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFondoLayout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 37, Short.MAX_VALUE)
                .addComponent(panelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 37, Short.MAX_VALUE)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        miInicio.setText("Inicio");

        miMenuPrincipal.setText("Menú principal");
        miMenuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMenuPrincipalActionPerformed(evt);
            }
        });
        miInicio.add(miMenuPrincipal);

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

        jMenuBar1.add(miInicio);

        miGestion.setText("Gestión");

        miAveria.setText("Avería");
        miAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAveriaActionPerformed(evt);
            }
        });
        miGestion.add(miAveria);

        miUsuario.setText("Usuario");
        miUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miUsuarioActionPerformed(evt);
            }
        });
        miGestion.add(miUsuario);

        miMaquinaria.setText("Maquinaria");
        miMaquinaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMaquinariaActionPerformed(evt);
            }
        });
        miGestion.add(miMaquinaria);

        miTipoMaquinaria.setText("Tipo de maquinaria");
        miTipoMaquinaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTipoMaquinariaActionPerformed(evt);
            }
        });
        miGestion.add(miTipoMaquinaria);

        miEstadoMaquinaria.setText("Estado de maquinaria");
        miEstadoMaquinaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEstadoMaquinariaActionPerformed(evt);
            }
        });
        miGestion.add(miEstadoMaquinaria);

        miTipoAveria.setText("Tipos de avería");
        miTipoAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTipoAveriaActionPerformed(evt);
            }
        });
        miGestion.add(miTipoAveria);

        miRoles.setText("Roles");
        miRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRolesActionPerformed(evt);
            }
        });
        miGestion.add(miRoles);

        jMenuBar1.add(miGestion);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMenuPrincipalActionPerformed
        vHomeAdmin homeAdmin = new vHomeAdmin();
        homeAdmin.setLocationRelativeTo(null);
        homeAdmin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMenuPrincipalActionPerformed

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

    private void miAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAveriaActionPerformed
        GestionAveriaListar gestionAveriaListar = new GestionAveriaListar();
        gestionAveriaListar.setLocationRelativeTo(null);
        gestionAveriaListar.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miAveriaActionPerformed

    private void miUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miUsuarioActionPerformed
        GestionUsuarioControlador gestionUsuarioControlador = new GestionUsuarioControlador();
        GestionUsuario gestionUsuario = new GestionUsuario(gestionUsuarioControlador);
        gestionUsuario.setLocationRelativeTo(null);
        gestionUsuario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miUsuarioActionPerformed

    private void miMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMaquinariaActionPerformed
        GestionMaquinas gestionMaquina = new GestionMaquinas();
        gestionMaquina.setLocationRelativeTo(null);
        gestionMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMaquinariaActionPerformed

    private void miTipoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoMaquinariaActionPerformed
        GestionTipoMaquina gestionTipoMaquina = new GestionTipoMaquina();
        gestionTipoMaquina.setLocationRelativeTo(null);
        gestionTipoMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miTipoMaquinariaActionPerformed

    private void miEstadoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadoMaquinariaActionPerformed
        GestionEstadoMaquina gestionEstadosMaquina = new GestionEstadoMaquina();
        gestionEstadosMaquina.setLocationRelativeTo(null);
        gestionEstadosMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miEstadoMaquinariaActionPerformed

    private void miTipoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoAveriaActionPerformed

    }//GEN-LAST:event_miTipoAveriaActionPerformed

    private void miRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRolesActionPerformed
        GestionRolControlador gestionRolControlador = new GestionRolControlador();
        GestionRol gestionRol = new GestionRol(gestionRolControlador);
        gestionRol.setLocationRelativeTo(null);
        gestionRol.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miRolesActionPerformed

    private void btnTipoAveriaActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaActualizarActionPerformed
        // 1. Verificar selección
        int fila = tablaAveria.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Selecciona un tipo de avería de la tabla.",
                    "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Extraer datos de la tabla seleccionada
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String desc = (String) modeloTabla.getValueAt(fila, 1);
        float tiempo = (float) modeloTabla.getValueAt(fila, 2);

        TipoAveriaActualizar tipoAveriaActualizar = new TipoAveriaActualizar(this, true, id, desc, tiempo);
        tipoAveriaActualizar.setLocationRelativeTo(this);
        tipoAveriaActualizar.setVisible(true);

        cargarDatos();

    }//GEN-LAST:event_btnTipoAveriaActualizarActionPerformed

    private void btnTipoAveriaEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaEliminarActionPerformed
        // 1. Verificar selección
        int fila = tablaAveria.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Selecciona un tipo de avería para eliminar.",
                    "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Extraer ID y Descripción
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String desc = (String) modeloTabla.getValueAt(fila, 1);

        // 3. Confirmación
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Eliminar el tipo #" + id + ": " + desc + "?\nNo podrás borrarlo si ya se está usando en alguna avería.",
                "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);

        // 4. Procesar eliminación (Llamando al boolean del Controlador)
        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            boolean exito = controlador.eliminar(id);
            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(this, "Eliminado con éxito.");
                cargarDatos(); // Recargamos la tabla
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se puede eliminar porque está en uso por alguna Avería.",
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnTipoAveriaEliminarActionPerformed

    private void btnTipoAveriaNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaNuevoActionPerformed
        TipoAveriaNueva tipoAveriaNueva = new TipoAveriaNueva(this, true);
        tipoAveriaNueva.setLocationRelativeTo(null);
        tipoAveriaNueva.setVisible(true);
    }//GEN-LAST:event_btnTipoAveriaNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTipoAveriaActualizar;
    private javax.swing.JButton btnTipoAveriaEliminar;
    private javax.swing.JButton btnTipoAveriaNuevo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JLabel jlSaldoIcono;
    private javax.swing.JPanel jpCabecera;
    private javax.swing.JMenuItem miAveria;
    private javax.swing.JMenuItem miCerrarSesion;
    private javax.swing.JMenuItem miEstadoMaquinaria;
    private javax.swing.JMenu miGestion;
    private javax.swing.JMenu miInicio;
    private javax.swing.JMenuItem miMaquinaria;
    private javax.swing.JMenuItem miMenuPrincipal;
    private javax.swing.JMenuItem miRoles;
    private javax.swing.JMenuItem miSalirApp;
    private javax.swing.JMenuItem miTipoAveria;
    private javax.swing.JMenuItem miTipoMaquinaria;
    private javax.swing.JMenuItem miUsuario;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel panelFondo;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JTable tablaAveria;
    // End of variables declaration//GEN-END:variables
}
