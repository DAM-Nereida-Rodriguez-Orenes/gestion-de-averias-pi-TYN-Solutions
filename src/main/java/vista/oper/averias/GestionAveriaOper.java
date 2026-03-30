/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.oper.averias;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.AveriaControlador;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import modelo.Averia;
import modelo.Usuario;
import utils.PanelImgFondo;
import vista.oper.usuario.GestionUsuarioPerfilOper;
import vista.vHomeOper;
import vista.vLogin;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Thanya
 */
public class GestionAveriaOper extends javax.swing.JFrame {

    private AveriaControlador controladorAveria = null;
    private LoginControlador loginControlador = null;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private boolean filtrosAplicados = false;

    /**
     * Creates new form GestionAveria
     */
    public GestionAveriaOper() {
        controladorAveria = new AveriaControlador();
        loginControlador = new LoginControlador();
        initComponents();
        inicializarTabla();
        configurarBuscador();
        mostrarImagenes();
        cargarDatosOperario();
    }

    /**
     * Configura los iconos, imágenes y estilos visuales de la ventana.
     */
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

        // Placeholder de FlatLaf
        txtAveriaBuscar.putClientProperty("JTextField.placeholderText", "Buscar ");
    }

    /**
     * Configura el modelo de la tabla, los anchos de las columnas, el formato de las fechas y otros ajustes visuales.
     */
    private void inicializarTabla() {
        String[] columnas = {
            "Cód.", "Descripción", "Máquina", "Tipo Avería",
            "F. Inicio", "F. Asignación", "F. Aceptación", "F. Fin",
            "Estado", "Reportado Por", "Técnico", "Procedimiento"
        };

        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tbAverias.setModel(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tbAverias.setRowSorter(sorter);

        javax.swing.table.TableColumnModel columnModel = tbAverias.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(60);

        tbAverias.setRowHeight(36);
        tbAverias.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        tbAverias.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));

        javax.swing.table.DefaultTableCellRenderer rendererFechas = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value == null) {
                    setText("");
                    return;
                }

                if (value instanceof java.time.LocalDate) {
                    java.time.LocalDate fecha = (java.time.LocalDate) value;
                    setText(fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else if (value instanceof java.time.LocalDateTime) {
                    java.time.LocalDateTime fechaHora = (java.time.LocalDateTime) value;
                    setText(fechaHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else if (value instanceof java.util.Date) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    setText(sdf.format((java.util.Date) value));
                } else {
                    setText(value.toString());
                }
            }
        };

        columnModel.getColumn(4).setCellRenderer(rendererFechas);
        columnModel.getColumn(5).setCellRenderer(rendererFechas);
        columnModel.getColumn(6).setCellRenderer(rendererFechas);
        columnModel.getColumn(7).setCellRenderer(rendererFechas);
    }

    /**
     * Descarga las averías de la BD y refresca la tabla. Se llama al iniciar la ventana y después de crear o editar una avería.
     */
    private void cargarDatosOperario() {
        LoginControlador loginControlador = new LoginControlador();
        Usuario usuarioLogueado = loginControlador.getUsuarioSesion();

        List<Object[]> datos = controladorAveria.listarAveriasParaVista(usuarioLogueado);
        actualizarModeloTabla(datos);
    }

    /**
     * Vacia la tabla y la rellena con los datos proporcionados. Si la lista de datos es null, solo vacía la tabla.
     */
    private void actualizarModeloTabla(List<Object[]> datos) {
        modeloTabla.setRowCount(0);

        if (datos != null) {
            for (int i = 0; i < datos.size(); i++) {
                modeloTabla.addRow(datos.get(i));
            }
        }
    }

    /** Configura el buscador para que filtre la tabla en tiempo real a medida que el usuario escribe.
     * El filtro se aplica a todas las columnas y es insensible a mayúsculas.
     */
    private void configurarBuscador() {
        txtAveriaBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });
    }

    /** Aplica un filtro de texto a la tabla usando el contenido del campo de búsqueda. Si el campo está vacío, se elimina el filtro.
     * El filtro es insensible a mayúsculas y busca coincidencias en todas las columnas.
     */
    private void filtrar() {
        String texto = txtAveriaBuscar.getText();

        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    /**
     * Descarga todas las averías de la BD y refresca la tabla.
     */
    private void cargarDatos(Usuario usuario) {
        List<Object[]> datos = controladorAveria.listarAveriasParaVista(usuario);
        actualizarModeloTabla(datos);
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
        panelTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAverias = new javax.swing.JTable();
        panelFiltros = new javax.swing.JPanel();
        btnNuevaAveria = new javax.swing.JButton();
        txtAveriaBuscar = new javax.swing.JTextField();
        tgbtnFiltros = new javax.swing.JToggleButton();
        panelAcciones = new javax.swing.JPanel();
        btnAveriaEditar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        miMenuPrincipal = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        miCerrarSesion = new javax.swing.JMenuItem();
        miSalirApp = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        miPerfil = new javax.swing.JMenuItem();
        miGestionAverias = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 800));

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jpCabecera.setPreferredSize(new java.awt.Dimension(400, 50));

        jlLogo.setText("jLabel2");

        txtSaludo.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        txtSaludo.setForeground(new java.awt.Color(67, 113, 177));
        txtSaludo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSaludo.setText("Hola, Admin");

        javax.swing.GroupLayout jpCabeceraLayout = new javax.swing.GroupLayout(jpCabecera);
        jpCabecera.setLayout(jpCabeceraLayout);
        jpCabeceraLayout.setHorizontalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSaludo, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jpCabeceraLayout.setVerticalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlLogo)
                    .addComponent(txtSaludo))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        panelTitulo.setBackground(new java.awt.Color(204, 204, 204));
        panelTitulo.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Gestión de Avería");

        javax.swing.GroupLayout panelTituloLayout = new javax.swing.GroupLayout(panelTitulo);
        panelTitulo.setLayout(panelTituloLayout);
        panelTituloLayout.setHorizontalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelTituloLayout.setVerticalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelTabla.setBackground(new java.awt.Color(204, 204, 204));
        panelTabla.setOpaque(false);

        tbAverias.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbAverias);

        javax.swing.GroupLayout panelTablaLayout = new javax.swing.GroupLayout(panelTabla);
        panelTabla.setLayout(panelTablaLayout);
        panelTablaLayout.setHorizontalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTablaLayout.setVerticalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFiltros.setBackground(new java.awt.Color(204, 204, 204));
        panelFiltros.setOpaque(false);

        btnNuevaAveria.setBackground(new java.awt.Color(58, 181, 235));
        btnNuevaAveria.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnNuevaAveria.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevaAveria.setText("+ Nueva avería");
        btnNuevaAveria.setBorderPainted(false);
        btnNuevaAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaAveriaActionPerformed(evt);
            }
        });

        txtAveriaBuscar.setBackground(new java.awt.Color(234, 242, 251));
        txtAveriaBuscar.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtAveriaBuscar.setForeground(new java.awt.Color(67, 113, 177));
        txtAveriaBuscar.setBorder(null);

        tgbtnFiltros.setBackground(new java.awt.Color(234, 242, 251));
        tgbtnFiltros.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        tgbtnFiltros.setForeground(new java.awt.Color(67, 113, 177));
        tgbtnFiltros.setText("Aplicar filtros");
        tgbtnFiltros.setBorderPainted(false);
        tgbtnFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgbtnFiltrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFiltrosLayout = new javax.swing.GroupLayout(panelFiltros);
        panelFiltros.setLayout(panelFiltrosLayout);
        panelFiltrosLayout.setHorizontalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltrosLayout.createSequentialGroup()
                .addComponent(txtAveriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(tgbtnFiltros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 439, Short.MAX_VALUE)
                .addComponent(btnNuevaAveria))
        );
        panelFiltrosLayout.setVerticalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAveriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tgbtnFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevaAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        panelAcciones.setBackground(new java.awt.Color(204, 204, 204));
        panelAcciones.setOpaque(false);

        btnAveriaEditar.setBackground(new java.awt.Color(234, 242, 251));
        btnAveriaEditar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnAveriaEditar.setForeground(new java.awt.Color(67, 113, 177));
        btnAveriaEditar.setText("Editar");
        btnAveriaEditar.setBorderPainted(false);
        btnAveriaEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAveriaEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccionesLayout.createSequentialGroup()
                .addGap(0, 957, Short.MAX_VALUE)
                .addComponent(btnAveriaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAveriaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(panelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 26, Short.MAX_VALUE)
                .addComponent(panelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
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

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed
        LoginControlador loginControlador = new LoginControlador();
        vLogin login = new vLogin(loginControlador);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miCerrarSesionActionPerformed

    /**
     * Cierra la aplicación por completo.
     */
    private void miSalirAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirAppActionPerformed
        System.exit(0);
    }//GEN-LAST:event_miSalirAppActionPerformed

    /**
     * Vuelve al menú principal del operario. En este caso, como el menú principal es el mismo que la gestión de averías, simplemente recarga la ventana.
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        vHomeOper home = new vHomeOper();
        home.setLocationRelativeTo(null);
        home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * Abre la ventana de gestión de perfil del usuario. Al cerrar esa ventana, vuelve a esta.
     */
    private void miPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPerfilActionPerformed
        GestionUsuarioPerfilOper gestionUsuarioPerfil = new GestionUsuarioPerfilOper();
        gestionUsuarioPerfil.setLocationRelativeTo(null);
        gestionUsuarioPerfil.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miPerfilActionPerformed

    /**
     * Recarga esta ventana. En este caso, como ya estamos en la gestión de averías, simplemente recarga los datos.
     */
    private void miGestionAveriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGestionAveriasActionPerformed
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }//GEN-LAST:event_miGestionAveriasActionPerformed

    /**
     * Lógica del botón de filtros:
     * - Si ya hay filtros aplicados, al hacer clic se eliminan y se recarga la tabla con todos los datos.
     * - Si no hay filtros, al hacer clic se abre el JDialog de filtros. Si el usuario aplica filtros, se traen los datos filtrados y se muestran en la tabla. El botón cambia a "Eliminar filtros".
     * - Si el usuario cierra el JDialog sin aplicar filtros, no pasa nada y el botón vuelve a su estado inicial.
     */
    private void tgbtnFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgbtnFiltrosActionPerformed
        // ESTADO 1: Si ya hay filtros aplicados, el botón actúa para ELIMINARLOS
        if (filtrosAplicados) {
            cargarDatos(loginControlador.getUsuarioSesion()); // Recarga todo desde la BD
            tgbtnFiltros.setText("Filtros");
            filtrosAplicados = false;
        } // ESTADO 2: Si NO hay filtros, el botón actúa para APLICARLOS (Abre el JDialog)
        else {
            FiltrosAveriaOper ventanaFiltros = new FiltrosAveriaOper(this, true);
            ventanaFiltros.setLocationRelativeTo(this);
            ventanaFiltros.setVisible(true); // El programa espera aquí

            // Si el usuario le dio a "Aplicar filtros" en el JDialog...
            if (ventanaFiltros.isAplicarFiltros()) {

                // 1. Extraemos los valores del JDialog
                Integer id = ventanaFiltros.getFiltroId();
                Integer idMaq = ventanaFiltros.getFiltroMaquina();
                Integer idTipo = ventanaFiltros.getFiltroTipo();
                java.time.LocalDateTime fIni = ventanaFiltros.getFiltroFechaReporte();
                java.time.LocalDateTime fFin = ventanaFiltros.getFiltroFechaFinal();

                // 2. Traemos los datos filtrados
                List<Object[]> datosFiltrados = controladorAveria.obtenerAveriasFiltradas(
                        id, null, fIni, fFin, loginControlador.getUsuarioSesion().getCodigoUsuario(), null, idMaq, idTipo
                );

                // 3. COMPROBAMOS LOS RESULTADOS
                if (datosFiltrados != null && !datosFiltrados.isEmpty()) {

                    // Hay resultados: Vaciamos la tabla y los pintamos
                    modeloTabla.setRowCount(0);
                    for (Object[] fila : datosFiltrados) {
                        modeloTabla.addRow(fila);
                    }

                    // Convertimos el botón a "Eliminar filtros"
                    tgbtnFiltros.setText("Eliminar filtros");
                    filtrosAplicados = true;
                    // El botón se queda hundido, que es lo que queremos.

                } else {
                    // NO HAY RESULTADOS
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "No se encontró ninguna avería con los filtros indicados.",
                            "Sin resultados",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    // Nos aseguramos de que la tabla muestra todo y el botón se reinicia
                    cargarDatos(loginControlador.getUsuarioSesion());
                    tgbtnFiltros.setText("Filtros");
                    filtrosAplicados = false;
                    tgbtnFiltros.setSelected(false);
                }
            } else {
                // El usuario cerró la ventana en la 'X' sin darle a "Aplicar filtros"
                tgbtnFiltros.setSelected(false);

                // Por si acaso, confirmamos el texto y el estado
                tgbtnFiltros.setText("Filtros");
                filtrosAplicados = false;
            }
        }
    }//GEN-LAST:event_tgbtnFiltrosActionPerformed

    /**
     * Lógica del botón de editar avería:
     * - Si no hay ninguna fila seleccionada, muestra un mensaje de advertencia.
     * - Si hay una fila seleccionada, obtiene el ID de la avería, descarga la avería completa desde la BD y abre el JDialog de edición.
     * - Si la avería ya tiene fecha de asignación a técnico, muestra un mensaje de advertencia indicando que no se puede editar.
     * - Al cerrar el JDialog de edición, recarga los datos para reflejar posibles cambios.
     */
    private void btnAveriaEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaEditarActionPerformed
        try {
            int filaVista = tbAverias.getSelectedRow();

            if (filaVista == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona una averia de la tabla",
                        "Editar averia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int filaModelo = tbAverias.convertRowIndexToModel(filaVista);
            int idAveria = Integer.parseInt(modeloTabla.getValueAt(filaModelo, 0).toString());

            Averia averiaSeleccionada = controladorAveria.obtenerAveriaPorId(idAveria);

            if (averiaSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo obtener la averia seleccionada",
                        "Editar averia",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (averiaSeleccionada.getFechaAsigTecnico() != null) {
                JOptionPane.showMessageDialog(this,
                        "Esta averia ya fue asignada a un tecnico y no puede editarse.",
                        "Edicion no permitida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            EditarAveria editarAveria = new EditarAveria(this, true, averiaSeleccionada);
            editarAveria.setLocationRelativeTo(this);
            editarAveria.setVisible(true);

            cargarDatosOperario();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir la ventana de editar averia",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnAveriaEditarActionPerformed

    /**
     * Lógica del botón de nueva avería:
     * - Al hacer clic, abre el JDialog de nueva avería. Al cerrar ese JDialog, recarga los datos para reflejar la posible nueva avería creada.
     */
    private void btnNuevaAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaAveriaActionPerformed
        // TODO add your handling code here: NuevaAveria
        try {
            // De momento usamos la ventana del admin (luego la cambiaremos)
            NuevaAveria nuevaAveria = new NuevaAveria(this, true, controladorAveria);
            nuevaAveria.setLocationRelativeTo(this);
            nuevaAveria.setVisible(true);

            // Al volver, recargamos tabla
            cargarDatosOperario();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al abrir la ventana de nueva avería",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNuevaAveriaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAveriaEditar;
    private javax.swing.JButton btnNuevaAveria;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JPanel jpCabecera;
    private javax.swing.JMenuItem miCerrarSesion;
    private javax.swing.JMenuItem miGestionAverias;
    private javax.swing.JMenu miMenuPrincipal;
    private javax.swing.JMenuItem miPerfil;
    private javax.swing.JMenuItem miSalirApp;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JTable tbAverias;
    private javax.swing.JToggleButton tgbtnFiltros;
    private javax.swing.JTextField txtAveriaBuscar;
    private javax.swing.JLabel txtSaludo;
    // End of variables declaration//GEN-END:variables
}
