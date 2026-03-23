/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.AveriaControlador;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.logging.Level;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import modelo.Averia;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;
import vista.PanelImgFondo;

/**
 * Ventana para Actualizar una Avería existente.
 *
 * @author yosnavmol
 */
public class AveriaActualizarANTIGUO extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AveriaActualizarANTIGUO.class.getName());

    // --- Controlador y Listas Maestras ---
    private final AveriaControlador controlador = new AveriaControlador();
    private List<Maquinaria> todasLasMaquinas;
    private List<Usuario> todosLosUsuarios;
    private List<Usuario> todosLosTecnicos;

    // --- Modelos Visuales ---
    private DefaultListModel<Maquinaria> modelMaquinas;
    private DefaultListModel<Usuario> modelUsuarios;
    private DefaultListModel<Usuario> modelTecnicos;
    private DefaultComboBoxModel<TipoAveria> modelTipos;

    // Avería seleccionada desde la tabla principal
    private final Averia averiaSeleccionada;

    /**
     * Constructor
     */
    public AveriaActualizarANTIGUO(java.awt.Frame parent, boolean modal, Averia averiaSeleccionada) {
        super(parent, modal);
        this.averiaSeleccionada = averiaSeleccionada;

        initComponents();

        // 1. Configurar y cargar datos base
        configurarListas();
        cargarDatos();
        activarFiltros();

        // 2. Preparar el formulario con los datos de la avería
        configurarEventosCheckBoxes();
        prepararModoEdicion();
    }

    public void mostrarImagenes() {
        //icno de la app
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        this.setLocationRelativeTo(null);

        //ICONOS JtextField
        //cbb Tipo de avería 
        FlatSVGIcon iconoTipoAveria = new FlatSVGIcon("recursos/iconos/llave_exact.svg", 16, 16);
        cbAveriaTipo2.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean estaSeleccionado,
                    boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, estaSeleccionado, tieneFoco);

                // Si es el placeholder
                if (valor == null) {
                    etiqueta.setText("Seleccionar tipo de averia: ");
                }

                // Solo muestra el icono cuando el combobox esta cerrado
                if (indice == -1) {
                    etiqueta.setIcon(iconoTipoAveria);
                } else {
                    etiqueta.setIcon(null);
                }

                return etiqueta;
            }
        });
        //campo de ID txtAveriaId
        FlatSVGIcon iconoIdAveria = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        txtAveriaId2.putClientProperty("JTextField.leadingIcon", iconoIdAveria);
        txtAveriaId2.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtAveriaId2.putClientProperty("JTextField.placeholderText", "Código de avería: ");
        //Campo Maquina txtMaquinaBuscar
        FlatSVGIcon iconoMaquina = new FlatSVGIcon("recursos/iconos/engranaje.svg", 16, 16);
        txtMaquinaBuscar2.putClientProperty("JTextField.leadingIcon", iconoMaquina);
        txtMaquinaBuscar2.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtMaquinaBuscar2.putClientProperty("JTextField.placeholderText", "Buscar máquina: ");
        //Campo usuario que informa txtUsuarioBuscar
        FlatSVGIcon iconoUsuarioReporta = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 15, 15);
        txtUsuarioBuscar2.putClientProperty("JTextField.leadingIcon", iconoUsuarioReporta);
        txtUsuarioBuscar2.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtUsuarioBuscar2.putClientProperty("JTextField.placeholderText", "Buscar usuario que informa: ");
        //Campo usuario que informa txtUsuarioBuscar
        FlatSVGIcon iconoUsuariotecnico = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 15, 15);
        txtTecnicoBuscar2.putClientProperty("JTextField.leadingIcon", iconoUsuariotecnico);
        txtTecnicoBuscar2.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTecnicoBuscar2.putClientProperty("JTextField.placeholderText", "Buscar técnico asingado (opcional): ");

        //PlaceHolder textArea
        //txtDescripcion
        txtDescripcion2.setText("Descripcion de la averia");
        txtDescripcion2.setForeground(Color.GRAY);
        txtDescripcion2.setMargin(new Insets(5, 8, 5, 8));
        txtDescripcion2.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtDescripcion2.getText().equals("Descripcion de la averia")) {
                    txtDescripcion2.setText("");
                    txtDescripcion2.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtDescripcion2.getText().trim().isEmpty()) {
                    txtDescripcion2.setText("Descripcion de la averia");
                    txtDescripcion2.setForeground(Color.GRAY);
                }
            }

        });

        //txtProcRealizado
        txtProcRealizado2.setText("Descripcion del procedimiento");
        txtProcRealizado2.setForeground(Color.GRAY);
        txtProcRealizado2.setMargin(new Insets(5, 8, 5, 8));
        txtProcRealizado2.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtProcRealizado2.getText().equals("Descripcion del procedimiento")) {
                    txtProcRealizado2.setText("");
                    txtProcRealizado2.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtProcRealizado2.getText().trim().isEmpty()) {
                    txtProcRealizado2.setText("Descripcion del procedimiento realizado");
                    txtProcRealizado2.setForeground(Color.GRAY);
                }
            }

        });

    }

    // =========================================================================
    // 1. CONFIGURACIÓN INICIAL Y CARGA DE DATOS
    // =========================================================================
    private void configurarListas() {
        modelMaquinas = new DefaultListModel<>();
        listaMaquinas2.setModel(modelMaquinas);

        modelUsuarios = new DefaultListModel<>();
        listaUsuarios2.setModel(modelUsuarios);

        modelTecnicos = new DefaultListModel<>();
        listaTecnicos2.setModel(modelTecnicos);

        modelTipos = new DefaultComboBoxModel<>();
        cbAveriaTipo2.setModel(modelTipos);
    }

    private void cargarDatos() {
        try {
            todasLasMaquinas = controlador.obtenerTodasLasMaquinas();
            todosLosUsuarios = controlador.obtenerTodosLosUsuarios();
            List<TipoAveria> tipos = controlador.obtenerTiposAveria();

            // Filtrar técnicos
            todosLosTecnicos = controlador.obtenerSoloTecnicos();

            // Llenar modelos
            if (todasLasMaquinas != null) {
                modelMaquinas.addAll(todasLasMaquinas);
            }
            if (todosLosUsuarios != null) {
                modelUsuarios.addAll(todosLosUsuarios);
            }
            if (todosLosTecnicos != null) {
                modelTecnicos.addAll(todosLosTecnicos);
            }

            if (tipos != null) {
                for (TipoAveria t : tipos) {
                    modelTipos.addElement(t);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar datos base en AveriaActualizar", e);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos de la base de datos.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 2. LÓGICA DE FILTRADO (Buscadores en tiempo real)
    // =========================================================================
    private void activarFiltros() {
        DocumentListener listenerUnificado = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltros();
            }
        };

        txtMaquinaBuscar2.getDocument().addDocumentListener(listenerUnificado);
        txtUsuarioBuscar2.getDocument().addDocumentListener(listenerUnificado);
        txtTecnicoBuscar2.getDocument().addDocumentListener(listenerUnificado);

        // Aplicar el primer filtro para limpiar las vistas si fuera necesario
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String textoMaq = txtMaquinaBuscar2.getText();
        List<Maquinaria> maqFiltrada = controlador.filtrarMaquinas(todasLasMaquinas, textoMaq);
        modelMaquinas.clear();
        if (maqFiltrada != null) {
            modelMaquinas.addAll(maqFiltrada);
        }

        String textoUsu = txtUsuarioBuscar2.getText();
        List<Usuario> usuFiltrados = controlador.filtrarUsuarios(todosLosUsuarios, textoUsu);
        modelUsuarios.clear();
        if (usuFiltrados != null) {
            modelUsuarios.addAll(usuFiltrados);
        }

        String textoTec = txtTecnicoBuscar2.getText();
        List<Usuario> tecFiltrados = controlador.filtrarUsuarios(todosLosTecnicos, textoTec);
        modelTecnicos.clear();
        if (tecFiltrados != null) {
            modelTecnicos.addAll(tecFiltrados);
        }
    }

    // =========================================================================
    // 3. PREPARACIÓN DE LA EDICIÓN (Rellenar Formulario)
    // =========================================================================
    private void configurarEventosCheckBoxes() {
        cbFechaAsig2.addActionListener(e -> spFechaAsig2.setEnabled(cbFechaAsig2.isSelected()));
        cbFechaAcep2.addActionListener(e -> spFechaAcep2.setEnabled(cbFechaAcep2.isSelected()));
        cbFechaFinal2.addActionListener(e -> spFechaFinal2.setEnabled(cbFechaFinal2.isSelected()));
    }

    private void prepararModoEdicion() {
        if (averiaSeleccionada == null) {
            return;
        }

        try {
            // Textos
            txtAveriaId2.setText(String.valueOf(averiaSeleccionada.getCodigoAveria()));
            txtDescripcion2.setText(averiaSeleccionada.getDescInicAveria());
            txtProcRealizado2.setText(averiaSeleccionada.getProcRealizadoTecnico());

            // Seleccionar Máquina y hacer auto-scroll
            for (int i = 0; i < modelMaquinas.getSize(); i++) {
                if (modelMaquinas.getElementAt(i).getCodigoMaquinaria() == averiaSeleccionada.getMaquinariaFK()) {
                    listaMaquinas2.setSelectedIndex(i);
                    listaMaquinas2.ensureIndexIsVisible(i);
                    break;
                }
            }

            // Seleccionar Usuario que reporta
            for (int i = 0; i < modelUsuarios.getSize(); i++) {
                if (modelUsuarios.getElementAt(i).getCodigoUsuario() == averiaSeleccionada.getUsuarioReportaFK()) {
                    listaUsuarios2.setSelectedIndex(i);
                    listaUsuarios2.ensureIndexIsVisible(i);
                    break;
                }
            }

            // Seleccionar Técnico (Opcional)
            if (averiaSeleccionada.getUsuarioTecnicoFK() != null && averiaSeleccionada.getUsuarioTecnicoFK() > 0) {
                for (int i = 0; i < modelTecnicos.getSize(); i++) {
                    if (modelTecnicos.getElementAt(i).getCodigoUsuario() == averiaSeleccionada.getUsuarioTecnicoFK()) {
                        listaTecnicos2.setSelectedIndex(i);
                        listaTecnicos2.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }

            // Seleccionar Tipo
            for (int i = 0; i < modelTipos.getSize(); i++) {
                if (modelTipos.getElementAt(i).getCodigoTipoAveria() == averiaSeleccionada.getTipoAveriaFK()) {
                    cbAveriaTipo2.setSelectedIndex(i);
                    break;
                }
            }

            // Fechas y CheckBoxes
            if (averiaSeleccionada.getFechaInicioAver() != null) {
                spFechaReporte2.setValue(convertirALocalDate(averiaSeleccionada.getFechaInicioAver()));
            }

            if (averiaSeleccionada.getFechaAsigTecnico() != null) {
                cbFechaAsig2.setSelected(true);
                spFechaAsig2.setEnabled(true);
                spFechaAsig2.setValue(convertirALocalDate(averiaSeleccionada.getFechaAsigTecnico()));
            }

            if (averiaSeleccionada.getFechaAcepTecnico() != null) {
                cbFechaAcep2.setSelected(true);
                spFechaAcep2.setEnabled(true);
                spFechaAcep2.setValue(convertirALocalDate(averiaSeleccionada.getFechaAcepTecnico()));
            }

            if (averiaSeleccionada.getFechaFinalizTecnico() != null) {
                cbFechaFinal2.setSelected(true);
                spFechaFinal2.setEnabled(true);
                spFechaFinal2.setValue(convertirALocalDate(averiaSeleccionada.getFechaFinalizTecnico()));
            }

            // BLOQUEAR FKs SI EL TÉCNICO YA HA ACEPTADO ---
            if (averiaSeleccionada.getFechaAcepTecnico() != null) {
                // Deshabilitamos las listas y el ComboBox para que no se puedan cambiar
                listaMaquinas2.setEnabled(false);
                listaUsuarios2.setEnabled(false);
                listaTecnicos2.setEnabled(false);
                cbAveriaTipo2.setEnabled(false);

                // Por buena experiencia de usuario, bloqueamos también sus buscadores
                txtMaquinaBuscar2.setEnabled(false);
                txtUsuarioBuscar2.setEnabled(false);
                txtTecnicoBuscar2.setEnabled(false);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al rellenar el formulario de edición", e);
        }
    }

    private java.util.Date convertirALocalDate(java.time.LocalDateTime ldt) {
        return java.util.Date.from(ldt.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    private java.time.LocalDateTime extraerFechaSpinner(javax.swing.JSpinner spinner) {
        java.util.Date date = (java.util.Date) spinner.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel4 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtAveriaId2 = new javax.swing.JTextField();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtDescripcion2 = new javax.swing.JTextArea();
        cbAveriaTipo2 = new javax.swing.JComboBox<>();
        txtMaquinaBuscar2 = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        listaMaquinas2 = new javax.swing.JList<>();
        txtUsuarioBuscar2 = new javax.swing.JTextField();
        jScrollPane14 = new javax.swing.JScrollPane();
        listaUsuarios2 = new javax.swing.JList<>();
        txtTecnicoBuscar2 = new javax.swing.JTextField();
        jScrollPane15 = new javax.swing.JScrollPane();
        listaTecnicos2 = new javax.swing.JList<>();
        jScrollPane16 = new javax.swing.JScrollPane();
        txtProcRealizado2 = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        spFechaReporte2 = new javax.swing.JSpinner();
        spFechaAsig2 = new javax.swing.JSpinner();
        cbFechaAsig2 = new javax.swing.JCheckBox();
        spFechaAcep2 = new javax.swing.JSpinner();
        cbFechaAcep2 = new javax.swing.JCheckBox();
        spFechaFinal2 = new javax.swing.JSpinner();
        cbFechaFinal2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Editar avería");
        jLabel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        txtAveriaId2.setEditable(false);
        txtAveriaId2.setBackground(new java.awt.Color(237, 243, 251));
        txtAveriaId2.setEnabled(false);

        txtDescripcion2.setColumns(20);
        txtDescripcion2.setRows(5);
        jScrollPane12.setViewportView(txtDescripcion2);

        cbAveriaTipo2.setBackground(new java.awt.Color(237, 243, 251));

        txtMaquinaBuscar2.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane13.setViewportView(listaMaquinas2);

        txtUsuarioBuscar2.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane14.setViewportView(listaUsuarios2);

        txtTecnicoBuscar2.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane15.setViewportView(listaTecnicos2);

        txtProcRealizado2.setColumns(20);
        txtProcRealizado2.setRows(5);
        jScrollPane16.setViewportView(txtProcRealizado2);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Fecha de reporte:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Fecha de asignación:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Fecha de aceptación:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Fecha de resolución:");

        spFechaReporte2.setModel(new javax.swing.SpinnerDateModel());

        spFechaAsig2.setModel(new javax.swing.SpinnerDateModel());
        spFechaAsig2.setEnabled(false);

        spFechaAcep2.setModel(new javax.swing.SpinnerDateModel());
        spFechaAcep2.setEnabled(false);

        spFechaFinal2.setModel(new javax.swing.SpinnerDateModel());
        spFechaFinal2.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane16)
                    .addComponent(jScrollPane14)
                    .addComponent(txtUsuarioBuscar2)
                    .addComponent(jScrollPane12)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtAveriaId2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(cbAveriaTipo2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(77, 77, 77)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addComponent(txtMaquinaBuscar2)
                        .addComponent(txtTecnicoBuscar2)
                        .addComponent(jScrollPane15))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFechaAsig2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spFechaAsig2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(spFechaReporte2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbFechaAcep2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbFechaFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spFechaFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spFechaAcep2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel18))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAveriaId2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbAveriaTipo2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMaquinaBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane13)
                    .addComponent(jScrollPane12))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsuarioBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTecnicoBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(spFechaReporte2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel19)
                                        .addComponent(cbFechaAsig2))
                                    .addComponent(spFechaAsig2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel20)
                                        .addComponent(cbFechaAcep2))
                                    .addComponent(spFechaAcep2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(spFechaFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21)))
                            .addComponent(cbFechaFinal2, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(209, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        jScrollPane6.setViewportView(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    // 4. ACCIÓN DE ACTUALIZAR
    // =========================================================================

    private void btnAveriaActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaActualizarActionPerformed
        try {
            int idAveria = averiaSeleccionada.getCodigoAveria();
            String descripcion = txtDescripcion2.getText();
            String procedimiento = txtProcRealizado2.getText();

            Maquinaria maquinaSel = listaMaquinas2.getSelectedValue();
            Usuario usuarioRepSel = listaUsuarios2.getSelectedValue();
            Usuario tecnicoSel = listaTecnicos2.getSelectedValue();
            TipoAveria tipoSel = (TipoAveria) cbAveriaTipo2.getSelectedItem();

            // La fecha de reporte siempre está activa
            java.time.LocalDateTime fechaReporte = extraerFechaSpinner(spFechaReporte2);

            // Las otras 3 dependen del CheckBox
            java.time.LocalDateTime fechaAsig = cbFechaAsig2.isSelected() ? extraerFechaSpinner(spFechaAsig2) : null;
            java.time.LocalDateTime fechaAcep = cbFechaAcep2.isSelected() ? extraerFechaSpinner(spFechaAcep2) : null;
            java.time.LocalDateTime fechaFinal = cbFechaFinal2.isSelected() ? extraerFechaSpinner(spFechaFinal2) : null;

            boolean exito = controlador.actualizarAveria(
                    idAveria, descripcion, procedimiento,
                    maquinaSel, usuarioRepSel, tecnicoSel, tipoSel,
                    fechaReporte, fechaAsig, fechaAcep, fechaFinal
            );

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Avería actualizada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error validando datos antes de actualizar", e);
            JOptionPane.showMessageDialog(this,
                    "Error al recoger los datos del formulario: " + e.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAveriaActualizarActionPerformed

    // =========================================================================
    // 5. CÓDIGO AUTOGENERADO (Diseño de Interfaz)
    // =========================================================================

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo2;
    private javax.swing.JCheckBox cbFechaAcep2;
    private javax.swing.JCheckBox cbFechaAsig2;
    private javax.swing.JCheckBox cbFechaFinal2;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JList<Maquinaria> listaMaquinas2;
    private javax.swing.JList<Usuario> listaTecnicos2;
    private javax.swing.JList<Usuario> listaUsuarios2;
    private javax.swing.JSpinner spFechaAcep2;
    private javax.swing.JSpinner spFechaAsig2;
    private javax.swing.JSpinner spFechaFinal2;
    private javax.swing.JSpinner spFechaReporte2;
    private javax.swing.JTextField txtAveriaId2;
    private javax.swing.JTextArea txtDescripcion2;
    private javax.swing.JTextField txtMaquinaBuscar2;
    private javax.swing.JTextArea txtProcRealizado2;
    private javax.swing.JTextField txtTecnicoBuscar2;
    private javax.swing.JTextField txtUsuarioBuscar2;
    // End of variables declaration//GEN-END:variables
}
