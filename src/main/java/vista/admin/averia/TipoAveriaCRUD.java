/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
 * @author yosnavmol
 */
public class TipoAveriaCRUD extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TipoAveriaCRUD.class.getName());

    // 1. Instanciamos nuestro nuevo controlador y el modelo de la tabla
    private final controlador.TipoAveriaControlador controlador = new controlador.TipoAveriaControlador();
    private javax.swing.table.DefaultTableModel modeloTabla;

    /**
     * Creates new form TipoAveriaListar
     */
    public TipoAveriaCRUD(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        // Centrar ventana principal
        this.setLocationRelativeTo(parent);

        // Configurar el tamaño de las sub-ventanas ocultas para que no salgan enanas
        TipoAveriaNueva.pack();
        TipoAveriaNueva.setLocationRelativeTo(this);

        TipoAveriaActualizar.pack();
        TipoAveriaActualizar.setLocationRelativeTo(this);

        // Cargar datos
        inicializarTabla();
        cargarDatos();

        inicializarSpinnerNumeroTipo();
        inicializarSpinnerNumeroTipoActualizar();
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
        // Tamaño fijo de todas las ventanas
        this.setSize(1200, 800);
        // Centrar ventana en pantalla
        this.setLocationRelativeTo(null);
        // Evitar que el usuario cambie el tamaño
        this.setResizable(false);

        //LOGO
        URL urlLogo = getClass().getClassLoader().getResource("recursos/logos/fixora_logo_140x70.svg");
        System.out.println("urlLogo = " + urlLogo);

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 70, 34);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

//interfaz principal
        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 24, 24);
        jlSaldoIcono.setIcon(iconUsuarioAdmin);
        jlSaldoIcono.setText("Hola, Admin");
        jlSaldoIcono.setHorizontalTextPosition(SwingConstants.LEFT);
        jlSaldoIcono.setVerticalTextPosition(SwingConstants.CENTER);
        jlSaldoIcono.setIconTextGap(8);

//JDialog Nuevo Tipo de Averia
        //ide Nuevo txtIdNuevo
        FlatSVGIcon iconoCodigo = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        txtIdNuevo.putClientProperty("JTextField.leadingIcon", iconoCodigo);
        txtIdNuevo.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtIdNuevo.putClientProperty("JTextField.placeholderText", "Código Rol:  4 ");
        txtIdNuevo.setEditable(false);
        txtIdNuevo.setEnabled(false);
        // tiempo 
        FlatSVGIcon iconoTime = new FlatSVGIcon("recursos/iconos/temporizador.svg", 16, 16);
        txtTiempoNuevo.putClientProperty("JTextField.leadingIcon", iconoTime);
        txtTiempoNuevo.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTiempoNuevo.putClientProperty("JTextField.placeholderText", "tiempo estimado en reparar (en horas):  ");
        //Descrpcion
        FlatSVGIcon iconoDescripcion = new FlatSVGIcon("recursos/iconos/icnEtiqueta.svg", 16, 16);
        txtDescripcionTipoAveria.putClientProperty("JTextField.leadingIcon", iconoDescripcion);
        txtDescripcionTipoAveria.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtDescripcionTipoAveria.putClientProperty("JTextField.placeholderText", "Descripción ");
//Jdialog Actualizar
        //Actualizar id
        FlatSVGIcon iconoCodigoActualizar = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        txtIdActualizar.putClientProperty("JTextField.leadingIcon", iconoCodigoActualizar);
        txtIdActualizar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtIdActualizar.putClientProperty("JTextField.placeholderText", "Código Rol:  4 ");
        txtIdActualizar.setEditable(false);
        txtIdActualizar.setEnabled(false);
        //SPinner 
        spnNumeroAveriaActualizar.setEnabled(false);
        // tiempo 
        FlatSVGIcon iconoTimeActualizar = new FlatSVGIcon("recursos/iconos/temporizador.svg", 16, 16);
        txtTiempoActualizar.putClientProperty("JTextField.leadingIcon", iconoTimeActualizar);
        txtTiempoActualizar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTiempoActualizar.putClientProperty("JTextField.placeholderText", "tiempo estimado en reparar (en horas):  ");
        //Descrpcion
        FlatSVGIcon iconoDescripcionActualizar = new FlatSVGIcon("recursos/iconos/icnEtiqueta.svg", 16, 16);
        txtDescripcionActualizar.putClientProperty("JTextField.leadingIcon", iconoDescripcionActualizar);
        txtDescripcionActualizar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtDescripcionActualizar.putClientProperty("JTextField.placeholderText", "Descripción ");
    }

    /**
     * Aquí hemos hecho que el spinner empiece en 1, tenga como mínimo 1, como
     * máximo 99 y avance de uno en uno. Después le ponemos el formato "00" para
     * que visualmente se vea 01, 02, 03... hasta 99.
     */
    private void inicializarSpinnerNumeroTipo() {
        SpinnerNumberModel modeloNumeroRol = new SpinnerNumberModel(1, 1, 99, 1);
        spnNumeroTipo.setModel(modeloNumeroRol);

        JSpinner.NumberEditor editorNumeroRol = new JSpinner.NumberEditor(spnNumeroTipo, "00");
        spnNumeroTipo.setEditor(editorNumeroRol);
    }

    private String obtenerNumeroTipoFormateado() {
        int numeroRol = (int) spnNumeroTipo.getValue();
        String numeroRolFormateado = String.format("%02d", numeroRol);
        return numeroRolFormateado;
    }

    private String obtenerCodigoTipoCompleto() {
        String numeroRolFormateado = obtenerNumeroTipoFormateado();
        String codigoRolCompleto = "4" + numeroRolFormateado;
        return codigoRolCompleto;
    }

    /**
     * ACTUALIZAR TIPO AVERIA. metodos que utiliza ese spinner
     */
    private void inicializarSpinnerNumeroTipoActualizar() {
        SpinnerNumberModel modeloNumeroTipoActualizar = new SpinnerNumberModel(1, 1, 99, 1);
        spnNumeroAveriaActualizar.setModel(modeloNumeroTipoActualizar);

        JSpinner.NumberEditor editorNumeroTipoActualizar = new JSpinner.NumberEditor(spnNumeroAveriaActualizar, "00");
        spnNumeroAveriaActualizar.setEditor(editorNumeroTipoActualizar);
    }

    private void cargarSpinnerActualizarDesdeId(int idCompleto) {
        String textoId = String.valueOf(idCompleto);

        if (textoId.startsWith("4") && textoId.length() >= 2) {
            String parteNumerica = textoId.substring(1); // por ejemplo 401 -> 01
            int numeroSpinner = Integer.parseInt(parteNumerica);
            spnNumeroAveriaActualizar.setValue(numeroSpinner);
        }
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

        TipoAveriaNueva = new javax.swing.JDialog();
        jPanel2 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel1 = new javax.swing.JPanel();
        txtTiempoNuevo = new javax.swing.JTextField();
        btnTipoAveriaCrear = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtIdNuevo = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        spnNumeroTipo = new javax.swing.JSpinner();
        txtDescripcionTipoAveria = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        TipoAveriaActualizar = new javax.swing.JDialog();
        jPanel4 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel5 = new javax.swing.JPanel();
        btnTipoAveriaGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtIdActualizar = new javax.swing.JTextField();
        txtTiempoActualizar = new javax.swing.JTextField();
        txtDescripcionActualizar = new javax.swing.JTextField();
        spnNumeroAveriaActualizar = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        btnCancelar1 = new javax.swing.JButton();
        jPanel3 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAveria = new javax.swing.JTable();
        btnTipoAveriaNuevo = new javax.swing.JButton();
        btnTipoAveriaActualizar = new javax.swing.JButton();
        btnTipoAveriaEliminar = new javax.swing.JButton();
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

        TipoAveriaNueva.setTitle("Nuevo tipo de avería");
        TipoAveriaNueva.setMaximumSize(new java.awt.Dimension(1000, 600));
        TipoAveriaNueva.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel2.setMaximumSize(new java.awt.Dimension(1000, 600));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txtTiempoNuevo.setBackground(new java.awt.Color(237, 243, 251));

        btnTipoAveriaCrear.setBackground(new java.awt.Color(58, 181, 235));
        btnTipoAveriaCrear.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnTipoAveriaCrear.setForeground(new java.awt.Color(255, 255, 255));
        btnTipoAveriaCrear.setText("Crear");
        btnTipoAveriaCrear.setBorderPainted(false);
        btnTipoAveriaCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAveriaCrearActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nuevo tipo de avería");

        txtIdNuevo.setBackground(new java.awt.Color(237, 243, 251));

        txtDescripcionTipoAveria.setBackground(new java.awt.Color(237, 243, 251));

        btnCancelar.setBackground(new java.awt.Color(234, 242, 251));
        btnCancelar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(67, 113, 177));
        btnCancelar.setText("Cancelar");
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTipoAveriaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTiempoNuevo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtIdNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(spnNumeroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDescripcionTipoAveria, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(94, 94, 94))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnNumeroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(txtTiempoNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(txtDescripcionTipoAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTipoAveriaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(97, 97, 97))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout TipoAveriaNuevaLayout = new javax.swing.GroupLayout(TipoAveriaNueva.getContentPane());
        TipoAveriaNueva.getContentPane().setLayout(TipoAveriaNuevaLayout);
        TipoAveriaNuevaLayout.setHorizontalGroup(
            TipoAveriaNuevaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        TipoAveriaNuevaLayout.setVerticalGroup(
            TipoAveriaNuevaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        TipoAveriaActualizar.setTitle("Editar tipo avería");

        jPanel4.setMaximumSize(new java.awt.Dimension(1000, 600));
        jPanel4.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnTipoAveriaGuardar.setBackground(new java.awt.Color(58, 181, 235));
        btnTipoAveriaGuardar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnTipoAveriaGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnTipoAveriaGuardar.setText("Editar");
        btnTipoAveriaGuardar.setBorderPainted(false);
        btnTipoAveriaGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAveriaGuardarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 204));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Editar tipo de avería");

        txtIdActualizar.setEditable(false);
        txtIdActualizar.setBackground(new java.awt.Color(237, 243, 251));

        txtTiempoActualizar.setBackground(new java.awt.Color(237, 243, 251));

        txtDescripcionActualizar.setBackground(new java.awt.Color(237, 243, 251));

        btnCancelar1.setBackground(new java.awt.Color(234, 242, 251));
        btnCancelar1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnCancelar1.setForeground(new java.awt.Color(67, 113, 177));
        btnCancelar1.setText("Cancelar");
        btnCancelar1.setBorderPainted(false);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTipoAveriaGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDescripcionActualizar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTiempoActualizar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtIdActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addComponent(spnNumeroAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(94, 94, 94))
            .addComponent(jSeparator2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnNumeroAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(txtTiempoActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(txtDescripcionActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTipoAveriaGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout TipoAveriaActualizarLayout = new javax.swing.GroupLayout(TipoAveriaActualizar.getContentPane());
        TipoAveriaActualizar.getContentPane().setLayout(TipoAveriaActualizarLayout);
        TipoAveriaActualizarLayout.setHorizontalGroup(
            TipoAveriaActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        TipoAveriaActualizarLayout.setVerticalGroup(
            TipoAveriaActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de Tipos de Averías");

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei UI", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Gestión de Tipos de Averías");

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

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnTipoAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnTipoAveriaEliminar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTipoAveriaNuevo))
                .addGap(61, 61, 61))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(66, 66, 66)
                .addComponent(btnTipoAveriaNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTipoAveriaEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTipoAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
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
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    // EVENTOS DE LOS BOTONES PRINCIPALES
    // =========================================================================

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

        // 3. Rellenar las cajas de la ventana de actualización
        txtIdActualizar.setText(String.valueOf(id));
        txtDescripcionActualizar.setText(desc);
        txtTiempoActualizar.setText(String.valueOf(tiempo));
        // El spinner muestra el numero correspondiente al ID, pero no se usa para actualizarlo
        cargarSpinnerActualizarDesdeId(id);

        // 4. Mostrar la ventana interna
        TipoAveriaActualizar.setIconImage(new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage());
        TipoAveriaActualizar.setModal(true);
        TipoAveriaActualizar.setLocationRelativeTo(this);
        TipoAveriaActualizar.setVisible(true);
    }//GEN-LAST:event_btnTipoAveriaActualizarActionPerformed

    private void btnTipoAveriaNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaNuevoActionPerformed
        // Limpiamos las cajas de texto por si quedaron cosas escritas de antes
        txtIdNuevo.setText("");
        txtDescripcionTipoAveria.setText("");
        txtTiempoNuevo.setText("");

        // Mostramos la ventana interna
        TipoAveriaNueva.setIconImage(new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage());
        TipoAveriaNueva.setModal(true);
        TipoAveriaNueva.setLocationRelativeTo(this);
        TipoAveriaNueva.setVisible(true);
    }//GEN-LAST:event_btnTipoAveriaNuevoActionPerformed

    private void btnTipoAveriaCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaCrearActionPerformed
        try {
            // Obtenemos el ID completo desde el spinner
            String numeroTipoFormateado = obtenerNumeroTipoFormateado();
            String textoId = obtenerCodigoTipoCompleto();

            // Validacion para que el ID empiece por 4
            if (!textoId.startsWith("4")) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "El ID del nuevo tipo de averia debe empezar obligatoriamente por el numero 4.",
                        "ID Invalido",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int id = Integer.parseInt(textoId);
            float tiempo = Float.parseFloat(txtTiempoNuevo.getText().trim());
            String desc = txtDescripcionTipoAveria.getText().trim();

            // Llamamos al controlador. Recuerda que este método internamente llama al void del DAO
            boolean exito = controlador.registrar(id, desc, tiempo);

            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(this, "Tipo de avería creado con éxito.");
                TipoAveriaNueva.setVisible(false); // Ocultamos la sub-ventana
                cargarDatos(); // Refrescamos la tabla principal
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número entero y el tiempo un número (ej. 1.5).",
                    "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTipoAveriaCrearActionPerformed

    private void btnTipoAveriaGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaGuardarActionPerformed
        // TODO add your handling code here:
        try {
            // El ID lo sacamos pero no dejamos que lo editen
            int id = Integer.parseInt(txtIdActualizar.getText().trim());
            // El spinner solo refleja visualmente el numero del ID
            // pero no se utiliza para modificarlo
            int numeroSpinner = (int) spnNumeroAveriaActualizar.getValue();

            float tiempo = Float.parseFloat(txtTiempoActualizar.getText().trim());
            String desc = txtDescripcionActualizar.getText().trim();

            boolean exito = controlador.actualizar(id, desc, tiempo);

            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(this, "Tipo de avería actualizado con éxito.");
                TipoAveriaActualizar.setVisible(false); // Ocultamos la sub-ventana
                cargarDatos(); // Refrescamos la tabla principal
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El tiempo debe ser un valor numérico (ej. 2.5).",
                    "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTipoAveriaGuardarActionPerformed

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

    private void miTipoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoMaquinariaActionPerformed
        GestionTipoMaquina gestionTipoMaquina = new GestionTipoMaquina();
        gestionTipoMaquina.setLocationRelativeTo(null);
        gestionTipoMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miTipoMaquinariaActionPerformed

    private void miTipoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoAveriaActionPerformed

    }//GEN-LAST:event_miTipoAveriaActionPerformed

    private void miSalirAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirAppActionPerformed
        System.exit(0);
    }//GEN-LAST:event_miSalirAppActionPerformed

    private void miMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMaquinariaActionPerformed
        GestionMaquinas gestionMaquina = new GestionMaquinas();
        gestionMaquina.setLocationRelativeTo(null);
        gestionMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMaquinariaActionPerformed

    private void miEstadoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadoMaquinariaActionPerformed
        GestionEstadoMaquina gestionEstadosMaquina = new GestionEstadoMaquina();
        gestionEstadosMaquina.setLocationRelativeTo(null);
        gestionEstadosMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miEstadoMaquinariaActionPerformed

    private void miRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRolesActionPerformed
        GestionRolControlador gestionRolControlador = new GestionRolControlador();
        GestionRol gestionRol = new GestionRol(gestionRolControlador);
        gestionRol.setLocationRelativeTo(null);
        gestionRol.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miRolesActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelar1ActionPerformed

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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                TipoAveriaCRUD dialog = new TipoAveriaCRUD(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog TipoAveriaActualizar;
    private javax.swing.JDialog TipoAveriaNueva;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnTipoAveriaActualizar;
    private javax.swing.JButton btnTipoAveriaCrear;
    private javax.swing.JButton btnTipoAveriaEliminar;
    private javax.swing.JButton btnTipoAveriaGuardar;
    private javax.swing.JButton btnTipoAveriaNuevo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
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
    private javax.swing.JSpinner spnNumeroAveriaActualizar;
    private javax.swing.JSpinner spnNumeroTipo;
    private javax.swing.JTable tablaAveria;
    private javax.swing.JTextField txtDescripcionActualizar;
    private javax.swing.JTextField txtDescripcionTipoAveria;
    private javax.swing.JTextField txtIdActualizar;
    private javax.swing.JTextField txtIdNuevo;
    private javax.swing.JTextField txtTiempoActualizar;
    private javax.swing.JTextField txtTiempoNuevo;
    // End of variables declaration//GEN-END:variables
}
