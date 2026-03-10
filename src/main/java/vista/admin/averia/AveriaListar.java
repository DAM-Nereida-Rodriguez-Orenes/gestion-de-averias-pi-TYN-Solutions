/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.JOptionPane;

import controlador.AveriaControlador;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import modelo.Averia;
import vista.PanelImgFondo;
import vista.admin.maquinas.GestionMaquinas;
import vista.admin.usuario.GestionRol;
import vista.admin.usuario.GestionUsuario;
import vista.vHomeAdmin;

/**
 * Ventana principal para la gestión de Averías. Muestra el listado general y
 * actúa como panel de control para CRUD y filtros.
 *
 * @author yosue
 */
public class AveriaListar extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AveriaListar.class.getName());

    // --- Controladores y Modelos ---
    private final AveriaControlador controladorAveria;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    // --- Estados de Interfaz ---
    private boolean filtrosAplicados = false;

    /**
     * Creates new form vAdminViewAverias
     */
    public AveriaListar() {
        controladorAveria = new AveriaControlador();

        setTitle("Mi JFrame Centrado");
        setSize(1200, 800); // Darle un tamaño es obligatorio antes de centrar
        setLocationRelativeTo(null);

        initComponents();

        // Configuraciones iniciales
        inicializarTabla();
        cargarDatos();
        configurarBuscador();
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
        jLabel3.setIcon(iconUsuarioAdmin);
        jLabel3.setText("Hola, Admin");
        jLabel3.setHorizontalTextPosition(SwingConstants.LEFT);
        jLabel3.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel3.setIconTextGap(8);
    }

    // =========================================================================
    // 1. CONFIGURACIÓN Y CARGA DE DATOS
    // =========================================================================
    private void inicializarTabla() {
        String[] columnas = {
            "Cód.", "Descripción", "Máquina", "Tipo Avería",
            "F. Inicio", "F. Asignación", "F. Aceptación", "F. Fin",
            "Estado", "Reportado Por", "Técnico", "Procedimiento"
        };

        // Modelo que impide la edición directa de celdas
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAveria.setModel(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tablaAveria.setRowSorter(sorter);

        // Ajuste visual de la columna ID
        //javax.swing.table.TableColumnModel columnModel = tablaAveria.getColumnModel();
        //columnModel.getColumn(0).setPreferredWidth(40);
        //columnModel.getColumn(0).setMaxWidth(60);

        //Ajustes al diseño de la tabla 
        tablaAveria.setRowHeight(36); // este valor aumenta el tamaño de las tuplas
        tablaAveria.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); //esto aumneta el tamaño de la fuente de la tabla y cambia la fuente 
        tablaAveria.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); // esto aumenta el tamaño de la fuente del header y cambia la fuente       
    }

    /**
     * Descarga todas las averías de la BD y refresca la tabla.
     */
    private void cargarDatos() {
        List<Object[]> datos = controladorAveria.listarAveriasParaVista();
        actualizarModeloTabla(datos);
    }

    /**
     * Método auxiliar para rellenar la tabla evitando duplicar código.
     */
    private void actualizarModeloTabla(List<Object[]> datos) {
        modeloTabla.setRowCount(0); // Limpia la tabla actual
        if (datos != null) {
            for (Object[] fila : datos) {
                modeloTabla.addRow(fila);
            }
            tablaAveria.setRowHeight(36); // este valor aumenta el tamaño de las tuplas
            tablaAveria.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); //esto aumneta el tamaño de la fuente de la tabla
            tablaAveria.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); // esto aumenta el tamaño de la fuente del header
        }
    }

    // =========================================================================
    // 2. LÓGICA DE FILTRADO LOCAL (Buscador Rápido)
    // =========================================================================
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

    private void filtrar() {
        String texto = txtAveriaBuscar.getText();
        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
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

        jPanel1 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jpCabecera = new PanelImgFondo("/recursos/fondoFormularios2.png");
        jlLogo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAveriaNueva = new javax.swing.JButton();
        tgbtnFiltros = new javax.swing.JToggleButton();
        txtAveriaBuscar = new javax.swing.JTextField();
        btnAveriaActualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAveria = new javax.swing.JTable();
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
        setTitle("Gestión de avería");

        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 800));

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jpCabecera.setPreferredSize(new java.awt.Dimension(400, 50));

        jlLogo.setText("jLabel2");

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(67, 113, 177));
        jLabel3.setText("Hola, Admin");

        javax.swing.GroupLayout jpCabeceraLayout = new javax.swing.GroupLayout(jpCabecera);
        jpCabecera.setLayout(jpCabeceraLayout);
        jpCabeceraLayout.setHorizontalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(86, 86, 86))
        );
        jpCabeceraLayout.setVerticalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlLogo)
                    .addComponent(jLabel3))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setText("Gestión de Avería");

        btnAveriaNueva.setBackground(new java.awt.Color(58, 181, 235));
        btnAveriaNueva.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnAveriaNueva.setForeground(new java.awt.Color(255, 255, 255));
        btnAveriaNueva.setText("+ Nueva avería");
        btnAveriaNueva.setBorderPainted(false);
        btnAveriaNueva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAveriaNuevaActionPerformed(evt);
            }
        });

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

        txtAveriaBuscar.setBackground(new java.awt.Color(234, 242, 251));
        txtAveriaBuscar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        txtAveriaBuscar.setForeground(new java.awt.Color(67, 113, 177));
        txtAveriaBuscar.setBorder(null);
        txtAveriaBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAveriaBuscarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAveriaBuscarFocusLost(evt);
            }
        });

        btnAveriaActualizar.setBackground(new java.awt.Color(234, 242, 251));
        btnAveriaActualizar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnAveriaActualizar.setForeground(new java.awt.Color(67, 113, 177));
        btnAveriaActualizar.setText("Editar");
        btnAveriaActualizar.setBorderPainted(false);
        btnAveriaActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAveriaActualizarActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(439, 439, 439)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtAveriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tgbtnFiltros)
                        .addGap(473, 473, 473)
                        .addComponent(btnAveriaNueva))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 62, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAveriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(tgbtnFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(btnAveriaNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    // 3. EVENTOS DE BOTONES (CRUD y Filtros Avanzados)
    // =========================================================================

    private void btnAveriaNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaNuevaActionPerformed
        // 1. Crear la instancia del JDialog
        AveriaNueva ventanaNueva = new AveriaNueva(this, true);
        // 2. Centrar la ventana respecto a la pantalla o al padre
        ventanaNueva.setLocationRelativeTo(this);
        // 3. Mostrar la ventana
        ventanaNueva.setVisible(true);
        // 4. Refrescar la tabla al volver
        cargarDatos();
    }//GEN-LAST:event_btnAveriaNuevaActionPerformed

    private void btnAveriaActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaActualizarActionPerformed
        try {
            // 1. Obtener la fila seleccionada (getSelectedRow coge la primera si hay varias)
            int filaVista = tablaAveria.getSelectedRow();

            if (filaVista == -1) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, selecciona una avería de la tabla para actualizar.",
                        "Selección requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Convertir índice por si hay filtros aplicados
            int filaModelo = tablaAveria.convertRowIndexToModel(filaVista);
            int idAveria = (int) modeloTabla.getValueAt(filaModelo, 0);

            // 3. Buscar la avería en la BD
            Averia averiaSeleccionada = controladorAveria.obtenerAveriaPorId(idAveria);

            if (averiaSeleccionada != null) {
                AveriaActualizar ventanaActualizar = new AveriaActualizar(this, true, averiaSeleccionada);
                ventanaActualizar.setLocationRelativeTo(this);
                ventanaActualizar.setVisible(true); // Se pausa aquí
                ventanaActualizar.setSize(1000, 600);

                cargarDatos(); // Refrescar al volver
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo cargar la información de la avería seleccionada.\nEs posible que haya sido eliminada por otro usuario.",
                        "Error de lectura", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al preparar la actualización", e);
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAveriaActualizarActionPerformed

    private void tgbtnFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgbtnFiltrosActionPerformed
        // ESTADO 1: Si ya hay filtros aplicados, el botón actúa para ELIMINARLOS
        if (filtrosAplicados) {
            cargarDatos(); // Recarga todo desde la BD
            tgbtnFiltros.setText("Filtros");
            filtrosAplicados = false;
        } // ESTADO 2: Si NO hay filtros, el botón actúa para APLICARLOS (Abre el JDialog)
        else {
            AveriaFiltros ventanaFiltros = new AveriaFiltros(this, true);
            ventanaFiltros.setLocationRelativeTo(this);
            ventanaFiltros.setVisible(true); // El programa espera aquí

            // Si el usuario le dio a "Aplicar filtros" en el JDialog...
            if (ventanaFiltros.isAplicarFiltros()) {

                // 1. Extraemos los valores del JDialog
                Integer id = ventanaFiltros.getFiltroId();
                Integer idMaq = ventanaFiltros.getFiltroMaquina();
                Integer idUsu = ventanaFiltros.getFiltroUsuario();
                Integer idTec = ventanaFiltros.getFiltroTecnico();
                Integer idTipo = ventanaFiltros.getFiltroTipo();
                java.time.LocalDateTime fIni = ventanaFiltros.getFiltroFechaReporte();
                java.time.LocalDateTime fFin = ventanaFiltros.getFiltroFechaFinal();

                // 2. Traemos los datos filtrados
                List<Object[]> datosFiltrados = controladorAveria.obtenerAveriasFiltradas(
                        id, null, fIni, fFin, idUsu, idTec, idMaq, idTipo
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
                    cargarDatos();
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

    private void miAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAveriaActionPerformed
        // TODO add your handling code here:
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
        // TODO add your handling code here:
    }//GEN-LAST:event_miTipoMaquinariaActionPerformed

    private void miRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRolesActionPerformed
        GestionRolControlador gestionRolControlador = new GestionRolControlador();
        GestionRol gestionRol = new GestionRol(gestionRolControlador);
        gestionRol.setLocationRelativeTo(null);
        gestionRol.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miRolesActionPerformed

    private void miMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMenuPrincipalActionPerformed
        vHomeAdmin homeAdmin = new vHomeAdmin();
        homeAdmin.setLocationRelativeTo(null);
        homeAdmin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMenuPrincipalActionPerformed

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miCerrarSesionActionPerformed

    private void txtAveriaBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAveriaBuscarFocusGained
        String texto = txtAveriaBuscar.getText().trim();

        if (texto.equalsIgnoreCase("Buscar descripción")) {
            txtAveriaBuscar.setText("");
        }
    }//GEN-LAST:event_txtAveriaBuscarFocusGained

    private void txtAveriaBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAveriaBuscarFocusLost
        String texto = txtAveriaBuscar.getText().trim();

        if (texto.isEmpty()) {
            txtAveriaBuscar.setText("Buscar descripción");
        }
    }//GEN-LAST:event_txtAveriaBuscarFocusLost

    private void miTipoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoAveriaActionPerformed
       TipoAveriaCRUD tipoAveria = new TipoAveriaCRUD(this, rootPaneCheckingEnabled);
        tipoAveria.setLocationRelativeTo(null);
        tipoAveria.setVisible(true);
    }//GEN-LAST:event_miTipoAveriaActionPerformed

    // =========================================================================
    // 4. CÓDIGO AUTOGENERADO (Diseño de la Interfaz)
    // =========================================================================
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
        java.awt.EventQueue.invokeLater(() -> new AveriaListar().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAveriaActualizar;
    private javax.swing.JButton btnAveriaNueva;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlLogo;
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
    private javax.swing.JTable tablaAveria;
    private javax.swing.JToggleButton tgbtnFiltros;
    private javax.swing.JTextField txtAveriaBuscar;
    // End of variables declaration//GEN-END:variables
}
