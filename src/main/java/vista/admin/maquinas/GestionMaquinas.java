/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.admin.maquinas;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.JOptionPane;
import controlador.GestionMaquinasControlador;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import modelo.Estado;
import modelo.Maquinaria;
import modelo.TipoMaquinaria;
import vista.PanelImgFondo;
import vista.admin.averia.AveriaListar;
import vista.admin.averia.TipoAveriaCRUD;
import vista.admin.usuario.GestionRol;
import vista.admin.usuario.GestionUsuario;
import vista.vHomeAdmin;
import vista.vLogin;

/**
 *
 * @author Nereida Rodríguez Orenes 2ºDAM
 */
public class GestionMaquinas extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GestionMaquinas.class.getName());
    private final GestionMaquinasControlador contr = new GestionMaquinasControlador();
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    List<Maquinaria> listaTotal = contr.listarMaquinaria();
    Map<Integer, String> estados = mapEstados();
    Map<Integer, String> tipos = mapTipos();
    private Map<String, Integer> estadoDescToId = new HashMap<>();
    private Map<String, Integer> tipoDescToId = new HashMap<>();

    /**
     * Creates new form GestionMaquinas
     */
    public GestionMaquinas() {
        initComponents();
        cargarComboEstados();
        cargarComboTipos();
        inicializarTabla();
        configurarOrdenacion();
        cargarTablaMaquinaria(listaTotal);
        mostrarImagenes();
    }

    public void mostrarImagenes() {
        //Ajustes del deisño del JFrame/Layout
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        // Tamaño fijo de todas las ventanas
        this.setSize(1200, 800);
        // Centrar ventana en pantalla
        this.setLocationRelativeTo(null);
        // Evitar que el usuario cambie el tamaño
        this.setResizable(false);

        URL urlLogo = getClass().getClassLoader().getResource("recursos/logos/fixora_logo_140x70.svg");
        System.out.println("urlLogo = " + urlLogo);

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 60, 30);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 24, 24);
        jlSaldoIcono.setIcon(iconUsuarioAdmin);
        jlSaldoIcono.setText("Hola, Admin");
        jlSaldoIcono.setHorizontalTextPosition(SwingConstants.LEFT);
        jlSaldoIcono.setVerticalTextPosition(SwingConstants.CENTER);
        jlSaldoIcono.setIconTextGap(8);

        //Campo ID maquina
        //FlatSVGIcon iconoIdMaquina = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        //txtID.putClientProperty("JTextField.leadingIcon", iconoIdMaquina);
        //txtID.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtID.putClientProperty("JTextField.placeholderText", "Código máquina: ");

        //Campo maquina txtNameFilter
        //FlatSVGIcon iconoNombreMaquina = new FlatSVGIcon("recursos/iconos/engranajes_exact.svg", 16, 16);
        //txtNameFilter.putClientProperty("JTextField.leadingIcon", iconoNombreMaquina);
        //txtNameFilter.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtNameFilter.putClientProperty("JTextField.placeholderText", "Buscar máquina: ");

        //Combobox Tipo maquinaria
        /*FlatSVGIcon iconoTipoMaquinaria = new FlatSVGIcon("recursos/iconos/engranajes_exact.svg", 16, 16);
        cbbTipo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean estaSeleccionado,
                    boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, estaSeleccionado, tieneFoco);

                // Solo muestra el icono cuando el combobox esta cerrado
                if (indice == -1) {
                    etiqueta.setIcon(iconoTipoMaquinaria);
                } else {
                    etiqueta.setIcon(null);
                }
                return etiqueta;
            }
        });*/
        //cbbStatus tipo estado
        /*FlatSVGIcon iconoEstadoMaquina = new FlatSVGIcon("recursos/iconos/llave_exact.svg", 16, 16);
        cbbStatus.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean estaSeleccionado,
                    boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, estaSeleccionado, tieneFoco);

                // Solo muestra el icono cuando el combobox esta cerrado
                if (indice == -1) {
                    etiqueta.setIcon(iconoEstadoMaquina);
                } else {
                    etiqueta.setIcon(null);
                }
                return etiqueta;
            }
        });*/
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
        jLabel1 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        cbbTipo = new javax.swing.JComboBox<>();
        cbbStatus = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMaquinaria = new javax.swing.JTable();
        btnNuevaMaquina = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtNameFilter = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        spFechaAlta = new javax.swing.JSpinner();
        spFechaBaja = new javax.swing.JSpinner();
        chbxHFechaAlta = new javax.swing.JCheckBox();
        chbxHFechaBaja = new javax.swing.JCheckBox();
        btnLimpiar = new javax.swing.JButton();
        jpCabecera = new PanelImgFondo("/recursos/fondoFormularios2.png");
        jlLogo = new javax.swing.JLabel();
        jlSaldoIcono = new javax.swing.JLabel();
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
        setTitle("Fixora");

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestión de Máquinas");

        txtID.setBackground(new java.awt.Color(237, 243, 251));
        txtID.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(234, 242, 251)));

        cbbTipo.setBackground(new java.awt.Color(234, 242, 251));
        cbbTipo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(234, 242, 251)));

        cbbStatus.setBackground(new java.awt.Color(234, 242, 251));
        cbbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Operativa", "Averiada", "En mantenimiento", "Fuera de servicio" }));
        cbbStatus.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(234, 242, 251)));

        tbMaquinaria.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbMaquinaria);

        btnNuevaMaquina.setBackground(new java.awt.Color(58, 181, 235));
        btnNuevaMaquina.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnNuevaMaquina.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevaMaquina.setText("+ Nueva Máquina");
        btnNuevaMaquina.setBorderPainted(false);
        btnNuevaMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaMaquinaActionPerformed(evt);
            }
        });

        btnActualizar.setBackground(new java.awt.Color(234, 242, 251));
        btnActualizar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(67, 113, 177));
        btnActualizar.setText("Actualizar");
        btnActualizar.setBorderPainted(false);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorderPainted(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        txtNameFilter.setBackground(new java.awt.Color(237, 243, 251));
        txtNameFilter.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(234, 242, 251)));

        btnFiltrar.setBackground(new java.awt.Color(234, 242, 251));
        btnFiltrar.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        btnFiltrar.setForeground(new java.awt.Color(67, 113, 177));
        btnFiltrar.setText("Aplicar filtro");
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 204));
        jLabel6.setText("Fecha de Alta:");

        jLabel7.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 204));
        jLabel7.setText("Fecha de Baja:");

        spFechaAlta.setModel(new javax.swing.SpinnerDateModel());
        spFechaAlta.setEnabled(false);

        spFechaBaja.setModel(new javax.swing.SpinnerDateModel());
        spFechaBaja.setEnabled(false);

        chbxHFechaAlta.setToolTipText("");
        chbxHFechaAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbxHFechaAltaActionPerformed(evt);
            }
        });

        chbxHFechaBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbxHFechaBajaActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(234, 242, 251));
        btnLimpiar.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(67, 113, 177));
        btnLimpiar.setText("Limpiar filtros");
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jpCabecera.setPreferredSize(new java.awt.Dimension(400, 50));

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
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(113, 113, 113)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(chbxHFechaAlta)
                        .addGap(12, 12, 12)
                        .addComponent(spFechaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(chbxHFechaBaja)
                        .addGap(12, 12, 12)
                        .addComponent(spFechaBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(cbbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(240, 240, 240)
                                .addComponent(btnNuevaMaquina))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnActualizar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNuevaMaquina)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spFechaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spFechaBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chbxHFechaAlta)
                            .addComponent(chbxHFechaBaja))))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar))
                .addContainerGap())
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        int id = getIdSeleccionado();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una máquina (pulse una fila).",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        var opt = contr.buscarMaquinaPorID(id);
        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró la máquina en la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        ActualizarMaquina dlg = new ActualizarMaquina(this, true);
        dlg.setLocationRelativeTo(this);
        dlg.setMaquina(opt.get());      // carga datos en el diálogo
        dlg.setVisible(true);           // modal: bloquea hasta cerrar

        cargarTablaMaquinaria(listaTotal);        //READ de nuevo para ver cambios
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnNuevaMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaMaquinaActionPerformed
        // TODO add your handling code here:
        NuevaMaquina nm = new NuevaMaquina(this, true);
        nm.setLocationRelativeTo(this);
        nm.setVisible(true);

        cargarTablaMaquinaria(listaTotal);
    }//GEN-LAST:event_btnNuevaMaquinaActionPerformed
    //eliminar máquina
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int id = getIdSeleccionado();
        if (id == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una máquina (pulse una fila).",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar la máquina con ID " + id + "?\n"
                + "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return; // cancelado
        }

        boolean ok = contr.eliminarMaquina(id);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Máquina eliminada con éxito.",
                    "Eliminación realizada",
                    JOptionPane.INFORMATION_MESSAGE);
            cargarTablaMaquinaria(listaTotal); // refrescar READ
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se ha podido eliminar.\n"
                    + "Puede que no exista o que esté relacionada con otras tablas (restricción de base de datos).",
                    "Error de eliminación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed
    //Habilitar las fechas cuando esté chequeado
    private void chbxHFechaAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbxHFechaAltaActionPerformed
        if (chbxHFechaAlta.isSelected()) {
            spFechaAlta.setEnabled(true);
        } else {
            spFechaAlta.setEnabled(false);
        }
    }//GEN-LAST:event_chbxHFechaAltaActionPerformed

    private void chbxHFechaBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbxHFechaBajaActionPerformed
        if (chbxHFechaBaja.isSelected()) {
            spFechaBaja.setEnabled(true);
        } else {
            spFechaBaja.setEnabled(false);
        }
    }//GEN-LAST:event_chbxHFechaBajaActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        Integer id = null;
        String textoId = txtID.getText().trim();

        if (!textoId.isBlank()) {
            try {
                id = Integer.valueOf(textoId);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "El ID debe ser un número entero.",
                        "Filtro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String nombre = txtNameFilter.getText().trim();

        boolean usarFechaAlta = chbxHFechaAlta.isSelected();
        boolean usarFechaBaja = chbxHFechaBaja.isSelected();

        Date fechaAlta = usarFechaAlta ? (Date) spFechaAlta.getValue() : null;
        Date fechaBaja = usarFechaBaja ? (Date) spFechaBaja.getValue() : null;

        Integer estadoId = null;
        String estadoDesc = (String) cbbStatus.getSelectedItem();
        if (estadoDesc != null && !estadoDesc.equals("Estado de máquinaria: ")) {
            estadoId = estadoDescToId.get(estadoDesc);
        }

        Integer tipoId = null;
        String tipoDesc = (String) cbbTipo.getSelectedItem();
        if (tipoDesc != null && !tipoDesc.equals("Tipo de máquinaria: ")) {
            tipoId = tipoDescToId.get(tipoDesc);
        }

        List<Maquinaria> lista = contr.filtrarMaquinaria(
                id,
                nombre,
                fechaAlta, usarFechaAlta,
                fechaBaja, usarFechaBaja,
                estadoId,
                tipoId
        );

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay máquinas que cumplan todos los filtros.",
                    "Resultado del filtrado",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        cargarTablaMaquinaria(lista);

    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // Limpiar campos de texto
        txtID.setText("");
        txtNameFilter.setText("");

        // Resetear combos
        cbbTipo.setSelectedItem("Todos");
        cbbStatus.setSelectedItem("Todos");

        // Desmarcar checks y deshabilitar fechas
        chbxHFechaAlta.setSelected(false);
        chbxHFechaBaja.setSelected(false);
        spFechaAlta.setEnabled(false);
        spFechaBaja.setEnabled(false);

        // Reiniciar valor de fechas
        spFechaAlta.setValue(new Date());
        spFechaBaja.setValue(new Date());

        // Recargar lista completa desde BD
        listaTotal = contr.listarMaquinaria();
        cargarTablaMaquinaria(listaTotal);

        // Mantener orden por ID ascendente
        if (sorter != null) {
            sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        }
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void miMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMenuPrincipalActionPerformed
        vHomeAdmin homeAdmin = new vHomeAdmin();
        homeAdmin.setLocationRelativeTo(null);
        homeAdmin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMenuPrincipalActionPerformed

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed

    }//GEN-LAST:event_miCerrarSesionActionPerformed

    private void miAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAveriaActionPerformed
        AveriaListar gestionAveria = new AveriaListar();
        gestionAveria.setLocationRelativeTo(null);
        gestionAveria.setVisible(true);
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
    }//GEN-LAST:event_miTipoMaquinariaActionPerformed

    private void miEstadoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadoMaquinariaActionPerformed
        GestionEstadoMaquina gestionEstadoMaquina = new GestionEstadoMaquina();
        gestionEstadoMaquina.setLocationRelativeTo(null);
        gestionEstadoMaquina.setVisible(true);
    }//GEN-LAST:event_miEstadoMaquinariaActionPerformed

    private void miTipoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoAveriaActionPerformed
        TipoAveriaCRUD tipoAveria = new TipoAveriaCRUD(this, rootPaneCheckingEnabled);
        tipoAveria.setLocationRelativeTo(null);
        tipoAveria.setVisible(true);
    }//GEN-LAST:event_miTipoAveriaActionPerformed

    private void miRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRolesActionPerformed
        GestionRolControlador gestionRolControlador = new GestionRolControlador();
        GestionRol gestionRol = new GestionRol(gestionRolControlador);
        gestionRol.setLocationRelativeTo(null);
        gestionRol.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miRolesActionPerformed

    //gestión de la tabla (read, ordenación)
    private void inicializarTabla() {
        /*
        Crea un DefaultTableModel con nombres de columnas, 0 filas iniciales, celdas no editables, tipo de dato por columna (muy importante para ordenar bien)
         */
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Estado", "Tipo", "Fecha alta", "Fecha baja"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } //que el usuario NO edite datos desde aquí

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 ->
                        Integer.class; // ID, primera columna: ordena como número
                    default ->
                        String.class; // resto: ordena como texto
                };
            }
        };
        tbMaquinaria.getTableHeader().setReorderingAllowed(false);
        tbMaquinaria.setModel(modeloTabla);
        tbMaquinaria.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbMaquinaria.setRowHeight(36); // este valor aumenta el tamaño de las tuplas
        tbMaquinaria.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); //esto aumneta el tamaño de la fuente de la tabla y cambia la fuente
        tbMaquinaria.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); // esto aumenta el tamaño de la fuente del header
        // Crear el renderer alineado a la izquierda
        DefaultTableCellRenderer renderIzquierda = new DefaultTableCellRenderer();
        renderIzquierda.setHorizontalAlignment(SwingConstants.LEFT);

        // Aplicarlo a una columna concreta (por ejemplo la columna 2)
        tbMaquinaria.getColumnModel().getColumn(0).setCellRenderer(renderIzquierda);

    }

    private void configurarOrdenacion() {
        sorter = new TableRowSorter<>(modeloTabla);
        tbMaquinaria.setRowSorter(sorter);

        //Orden inicial por ID ascendente
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
    }

    /*
    ¿POR QUÉ SE USAN MAPAS?
    La tabla maquinaria guarda FKs (codigoEstadoFK, tipoMaquinariaFK).
    El usuario no quiere ver 801 o 303, quiere ver averiada, etc.
    
    Si por cada máquina hiciéramos una query para buscar el estado/tipo, sería lento.
    En cambio, es más rápido hacer 1 query para estados, 1 para tipos
    y luego traducir en memoria con un Map
     */
    private void cargarTablaMaquinaria(List<Maquinaria> lista) {
        modeloTabla.setRowCount(0);

        for (Maquinaria m : lista) {
            String estadoDesc = "";
            if (m.getEstado() != null) {
                int idEstado = m.getEstado().getCodigoEstado();
                estadoDesc = estados.getOrDefault(idEstado, String.valueOf(idEstado));
            }

            String tipoDesc = "";
            if (m.getTipoMaquinaria() != null) {
                int idTipo = m.getTipoMaquinaria().getCodigoTipoMaquinaria();
                tipoDesc = tipos.getOrDefault(idTipo, String.valueOf(idTipo));
            }

            String fechaAlta = (m.getFechaAlta() != null) ? m.getFechaAlta().toString() : "";
            String fechaBaja = (m.getFechaBaja() != null) ? m.getFechaBaja().toString() : "";

            modeloTabla.addRow(new Object[]{
                m.getCodigoMaquinaria(),
                m.getNombre(),
                estadoDesc,
                tipoDesc,
                fechaAlta,
                fechaBaja
            });
        }
    }

    private Map<Integer, String> mapEstados() {
        Map<Integer, String> map = new HashMap<>();
        for (Estado e : contr.listarEstado()) {
            map.put(e.getCodigoEstado(), e.getDescripcionEstado());
        }
        return map;
    }

    private Map<Integer, String> mapTipos() {
        Map<Integer, String> map = new HashMap<>();
        for (TipoMaquinaria t : contr.listarTipoMaquinaria()) {
            map.put(t.getCodigoTipoMaquinaria(), t.getDescripcionMaq());
        }
        return map;
    }

    //conseguir la máquina real a pesar del orden (el filtro solo es visual, no lógico)
    private int getIdSeleccionado() {
        int filaVista = tbMaquinaria.getSelectedRow();
        if (filaVista == -1) {
            return -1;
        }

        int filaModelo = tbMaquinaria.convertRowIndexToModel(filaVista);
        Object idObj = tbMaquinaria.getModel().getValueAt(filaModelo, 0);

        return Integer.parseInt(String.valueOf(idObj));
    }

    //cargar comboboxes bien
    private void cargarComboEstados() {
        cbbStatus.removeAllItems();
        estadoDescToId.clear();

        cbbStatus.addItem("Todos");

        for (Estado e : contr.listarEstado()) {
            cbbStatus.addItem(e.getDescripcionEstado());
            estadoDescToId.put(e.getDescripcionEstado(), e.getCodigoEstado());
        }
    }

    private void cargarComboTipos() {
        cbbTipo.removeAllItems();
        tipoDescToId.clear();

        cbbTipo.addItem("Todos");

        for (TipoMaquinaria t : contr.listarTipoMaquinaria()) {
            cbbTipo.addItem(t.getDescripcionMaq());
            tipoDescToId.put(t.getDescripcionMaq(), t.getCodigoTipoMaquinaria());
        }
    }

    /*FILTROS (la vista recoge los datos y se los pasa al controlador, que validará y mandará las cosas traducidas al DAO*/
 /*ID*/
    private void filtrarPorId() {
        String texto = txtID.getText().trim();

        if (texto.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Introduce un ID.",
                    "Filtro por ID",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id;
        try {
            id = Integer.valueOf(texto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número entero.",
                    "Filtro por ID",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        var opt = contr.buscarMaquinaPorID(id);

        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Puede que no exista una máquina con ese ID o que esté mal escrito",
                    "Error filtrando por ID",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            List<Maquinaria> lista = new ArrayList<>();
            lista.add(opt.get());
            cargarTablaMaquinaria(lista);
        }
    }

    /*nombre*/
    private void filtrarPorNombre() {
        //que pase String
        String nombre = txtNameFilter.getText();

        if (contr.filtrarPorNombre(nombre).isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Puede que no exista una máquina con ese nombre o que esté mal escrito",
                    "Error filtrando por nombre",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            List<Maquinaria> lista = new ArrayList<>();
            lista = contr.filtrarPorNombre(nombre);
            cargarTablaMaquinaria(lista);
        };
    }

    /*fechas*/
    private void filtrarPorFechas() {
        boolean usarFechaAlta = chbxHFechaAlta.isSelected();
        boolean usarFechaBaja = chbxHFechaBaja.isSelected();

        Date fechaAlta = null;
        Date fechaBaja = null;

        if (usarFechaAlta) {
            fechaAlta = (Date) spFechaAlta.getValue();
        }

        if (usarFechaBaja) {
            fechaBaja = (Date) spFechaBaja.getValue();
        }

        if (!usarFechaAlta && !usarFechaBaja) {
            JOptionPane.showMessageDialog(this,
                    "Debes habilitar al menos una fecha para filtrar.",
                    "Filtro por fechas",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Maquinaria> lista = contr.filtrarPorFechas(
                fechaAlta, usarFechaAlta,
                fechaBaja, usarFechaBaja
        );

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay máquinas que coincidan con las fechas indicadas.",
                    "Filtro por fechas",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        cargarTablaMaquinaria(lista);
    }

    /*estado*/
    private void filtrarPorStatus() {
        String estadoDesc = (String) cbbStatus.getSelectedItem();

        Integer estadoId = null;

        if (estadoDesc != null && !estadoDesc.equals("Todos")) {
            estadoId = estadoDescToId.get(estadoDesc);
        }

        List<Maquinaria> lista = contr.filtrarPorStatus(estadoId);

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay máquinas con ese estado.", "Filtro por estado", JOptionPane.INFORMATION_MESSAGE);
        }

        cargarTablaMaquinaria(lista);
    }

    /*tipo*/
    private void filtrarPorTipo() {
        String tipoDesc = (String) cbbTipo.getSelectedItem();

        Integer tipoId = null;

        if (tipoDesc != null && !tipoDesc.equals("Todos")) {
            tipoId = tipoDescToId.get(tipoDesc);
        }

        List<Maquinaria> lista = contr.filtrarPorTipo(tipoId);

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay máquinas de ese tipo.",
                    "Filtro por tipo",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        cargarTablaMaquinaria(lista);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GestionMaquinas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevaMaquina;
    private javax.swing.JComboBox<String> cbbStatus;
    private javax.swing.JComboBox<String> cbbTipo;
    private javax.swing.JCheckBox chbxHFechaAlta;
    private javax.swing.JCheckBox chbxHFechaBaja;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JSpinner spFechaAlta;
    private javax.swing.JSpinner spFechaBaja;
    private javax.swing.JTable tbMaquinaria;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNameFilter;
    // End of variables declaration//GEN-END:variables
}
