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
import controlador.LoginControlador;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import modelo.Averia;
import utils.InformesJasper;
import vista.PanelImgFondo;
import vista.admin.maquinas.GestionEstadoMaquina;
import vista.admin.maquinas.GestionMaquinas;
import vista.admin.maquinas.GestionTipoMaquina;
import vista.admin.usuario.GestionRol;
import vista.admin.usuario.GestionUsuario;
import vista.vHomeAdmin;
import vista.vLogin;

/**
 * Ventana principal para la gestión de Averías. Muestra el listado general y
 * actúa como panel de control para CRUD y filtros.
 *
 * @author yosue
 */
public class GestionAveriaListar extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GestionAveriaListar.class.getName());

    // --- Controladores y Modelos ---
    private final AveriaControlador controladorAveria;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    // --- Estados de Interfaz ---
    private boolean filtrosAplicados = false;

    /**
     * Creates new form vAdminViewAverias
     */
    public GestionAveriaListar() {
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

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 60, 30);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 24, 24);
        jlSaldoIcono.setIcon(iconUsuarioAdmin);
        GestionUsuarioControlador userContr = new GestionUsuarioControlador();
        jlSaldoIcono.setText("Hola, " + userContr.obtenerNombreUsuarioLogueado());
        jlSaldoIcono.setHorizontalTextPosition(SwingConstants.LEFT);
        jlSaldoIcono.setVerticalTextPosition(SwingConstants.CENTER);
        jlSaldoIcono.setIconTextGap(8);

        // Placeholder de FlatLaf
        txtAveriaBuscar.putClientProperty("JTextField.placeholderText", "Buscar ");
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
        javax.swing.table.TableColumnModel columnModel = tablaAveria.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(60);

        //Ajustes al diseño de la tabla 
        tablaAveria.setRowHeight(36);
        // este valor aumenta el tamaño de las tuplas
        tablaAveria.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        //esto aumneta el tamaño de la fuente de la tabla y cambia la fuente 
        tablaAveria.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        // esto aumenta el tamaño de la fuente del header y cambia la fuente 

        // Renderer para mostrar las fechas en formato dd/MM/yyyy
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

        // Aplicar el renderer a las columnas de fechas
        columnModel.getColumn(4).setCellRenderer(rendererFechas); // F. Inicio
        columnModel.getColumn(5).setCellRenderer(rendererFechas); // F. Asignacion
        columnModel.getColumn(6).setCellRenderer(rendererFechas); // F. Aceptacion
        columnModel.getColumn(7).setCellRenderer(rendererFechas); // F. Fin
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
        jlSaldoIcono = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnAveriaActualizar = new javax.swing.JButton();
        btnInfoAveria = new javax.swing.JButton();
        btnInfoTecnico = new javax.swing.JButton();
        panelFiltro = new javax.swing.JPanel();
        btnAveriaNueva = new javax.swing.JButton();
        tgbtnFiltros = new javax.swing.JToggleButton();
        txtAveriaBuscar = new javax.swing.JTextField();
        panelTitulo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelTabla = new javax.swing.JPanel();
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
        setTitle("Gestión de Avería");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

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

        panelAcciones.setBackground(new java.awt.Color(153, 153, 153));
        panelAcciones.setOpaque(false);

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

        btnInfoAveria.setBackground(new java.awt.Color(67, 113, 177));
        btnInfoAveria.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnInfoAveria.setForeground(new java.awt.Color(234, 242, 251));
        btnInfoAveria.setText("Detalles de avería");
        btnInfoAveria.setBorderPainted(false);
        btnInfoAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoAveriaActionPerformed(evt);
            }
        });

        btnInfoTecnico.setBackground(new java.awt.Color(67, 113, 177));
        btnInfoTecnico.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnInfoTecnico.setForeground(new java.awt.Color(234, 242, 251));
        btnInfoTecnico.setText("Rendimiento tecnicos");
        btnInfoTecnico.setBorderPainted(false);
        btnInfoTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoTecnicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnInfoAveria)
                .addGap(18, 18, 18)
                .addComponent(btnInfoTecnico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 512, Short.MAX_VALUE)
                .addComponent(btnAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInfoAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInfoTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        panelFiltro.setBackground(new java.awt.Color(153, 153, 153));
        panelFiltro.setOpaque(false);

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
        txtAveriaBuscar.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtAveriaBuscar.setForeground(new java.awt.Color(67, 113, 177));
        txtAveriaBuscar.setBorder(null);

        javax.swing.GroupLayout panelFiltroLayout = new javax.swing.GroupLayout(panelFiltro);
        panelFiltro.setLayout(panelFiltroLayout);
        panelFiltroLayout.setHorizontalGroup(
            panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFiltroLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(txtAveriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tgbtnFiltros)
                .addGap(473, 473, 473)
                .addComponent(btnAveriaNueva)
                .addContainerGap(75, Short.MAX_VALUE))
        );
        panelFiltroLayout.setVerticalGroup(
            panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAveriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelFiltroLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panelFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tgbtnFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAveriaNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTitulo.setBackground(new java.awt.Color(153, 153, 153));
        panelTitulo.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Gestión de Avería");

        javax.swing.GroupLayout panelTituloLayout = new javax.swing.GroupLayout(panelTitulo);
        panelTitulo.setLayout(panelTituloLayout);
        panelTituloLayout.setHorizontalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTituloLayout.setVerticalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(panelFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(panelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(panelFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    // 3. EVENTOS DE BOTONES (CRUD y Filtros Avanzados)
    // =========================================================================

    private void btnAveriaNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaNuevaActionPerformed
        // 1. Crear la instancia del JDialog
        AveriaNueva ventanaNueva = new AveriaNueva(this, true, controladorAveria);
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
                AveriaActualizar2 ventanaActualizar = new AveriaActualizar2(this, true, averiaSeleccionada);
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
        GestionTipoMaquina gestionTipoMaquina = new GestionTipoMaquina();
        gestionTipoMaquina.setLocationRelativeTo(null);
        gestionTipoMaquina.setVisible(true);
        this.dispose();
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
        LoginControlador loginControlador = new LoginControlador();
        vLogin login = new vLogin(loginControlador);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miCerrarSesionActionPerformed

    private void miTipoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoAveriaActionPerformed
        GestionTipoAveria gestionTipoAveria = new GestionTipoAveria();
        gestionTipoAveria.setLocationRelativeTo(null);
        gestionTipoAveria.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miTipoAveriaActionPerformed

    private void miEstadoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadoMaquinariaActionPerformed
        GestionEstadoMaquina gestionEstadoMaquina = new GestionEstadoMaquina();
        gestionEstadoMaquina.setLocationRelativeTo(null);
        gestionEstadoMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miEstadoMaquinariaActionPerformed

    private void miSalirAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirAppActionPerformed
        System.exit(0);
    }//GEN-LAST:event_miSalirAppActionPerformed

    private void btnInfoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoAveriaActionPerformed
        try {
            // 1. Obtener la fila seleccionada de la tabla
            int filaVista = tablaAveria.getSelectedRow();

            if (filaVista == -1) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar una averia de la tabla para generar el informe.",
                        "Seleccion requerida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Convertir la fila de vista a modelo por si hay filtros o buscador
            int filaModelo = tablaAveria.convertRowIndexToModel(filaVista);

            // 3. Obtener el id de la averia desde la columna 0
            int idAveria = (int) modeloTabla.getValueAt(filaModelo, 0);

            // 4. Llamar a la clase que genera el informe
            InformesJasper informesJasper = new InformesJasper();
            String rutaInformeGenerado = informesJasper.generarInformeAveria(idAveria);

            // 5. Mostrar resultado
            JOptionPane.showMessageDialog(this,
                    "Informe generado correctamente.\nGuardado en:\n" + rutaInformeGenerado,
                    "Informe generado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al generar el informe de averia", e);

            JOptionPane.showMessageDialog(this,
                    "No se pudo generar el informe.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnInfoAveriaActionPerformed

    private void btnInfoTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoTecnicoActionPerformed
        try {
            InformesJasper informesJasper = new InformesJasper();
            String rutaInformeGenerado = informesJasper.generarInformeTecnicos();

            JOptionPane.showMessageDialog(this,
                    "Informe generado correctamente.\nGuardado en:\n" + rutaInformeGenerado,
                    "Informe generado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al generar el informe de tecnicos", e);

            JOptionPane.showMessageDialog(this,
                    "No se pudo generar el informe.\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInfoTecnicoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAveriaActualizar;
    private javax.swing.JButton btnAveriaNueva;
    private javax.swing.JButton btnInfoAveria;
    private javax.swing.JButton btnInfoTecnico;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFiltro;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JTable tablaAveria;
    private javax.swing.JToggleButton tgbtnFiltros;
    private javax.swing.JTextField txtAveriaBuscar;
    // End of variables declaration//GEN-END:variables
}
