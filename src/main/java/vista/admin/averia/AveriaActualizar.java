/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import vista.PanelImgFondo;
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

/**
 *
 * @author Netri
 */
public class AveriaActualizar extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AveriaActualizar.class.getName());
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
     * Creates new form Averiaactualizarv2
     */
    public AveriaActualizar(java.awt.Frame parent, boolean modal, Averia averiaSeleccionada) {
        super(parent, modal);
        initComponents();
        this.averiaSeleccionada = averiaSeleccionada;

        // 3. Mostrar iconos y fuentes 
        mostrarImagenes();
        // 1. Configurar y cargar datos base
        configurarListas();
        cargarDatos();
        activarFiltros();

        // 2. Preparar el formulario con los datos de la avería
        configurarEventosCheckBoxes();
        prepararModoEdicion();
    }

    // DISEÑO 
    public void mostrarImagenes() {
        //icno de la app
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        this.setLocationRelativeTo(null);

        //ICONOS JtextField
        //cbb Tipo de avería 
        FlatSVGIcon iconoTipoAveria = new FlatSVGIcon("recursos/iconos/llave_exact.svg", 16, 16);
        cbAveriaTipo.setRenderer(new DefaultListCellRenderer() {
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
        txtAveriaId.putClientProperty("JTextField.leadingIcon", iconoIdAveria);
        txtAveriaId.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtAveriaId.putClientProperty("JTextField.placeholderText", "Código de avería: ");
        //Campo Maquina txtMaquinaBuscar
        FlatSVGIcon iconoMaquina = new FlatSVGIcon("recursos/iconos/engranajes_exact.svg", 16, 16);
        txtMaquinaBuscar.putClientProperty("JTextField.leadingIcon", iconoMaquina);
        txtMaquinaBuscar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtMaquinaBuscar.putClientProperty("JTextField.placeholderText", "Buscar máquina: ");
        //Campo usuario que informa txtUsuarioBuscar
        FlatSVGIcon iconoUsuarioReporta = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 15, 15);
        txtUsuarioBuscar.putClientProperty("JTextField.leadingIcon", iconoUsuarioReporta);
        txtUsuarioBuscar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtUsuarioBuscar.putClientProperty("JTextField.placeholderText", "Buscar usuario que informa: ");
        //Campo usuario que informa txtUsuarioBuscar
        FlatSVGIcon iconoUsuariotecnico = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 15, 15);
        txtTecnicoBuscar.putClientProperty("JTextField.leadingIcon", iconoUsuariotecnico);
        txtTecnicoBuscar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTecnicoBuscar.putClientProperty("JTextField.placeholderText", "Buscar técnico asingado (opcional): ");

        //PlaceHolder textArea
        //txtDescripcion
        txtDescripcion.setText("Descripcion de la averia");
        txtDescripcion.setForeground(Color.GRAY);
        txtDescripcion.setMargin(new Insets(5, 8, 5, 8));
        txtDescripcion.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtDescripcion.getText().equals("Descripcion de la averia")) {
                    txtDescripcion.setText("");
                    txtDescripcion.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtDescripcion.getText().trim().isEmpty()) {
                    txtDescripcion.setText("Descripcion de la averia");
                    txtDescripcion.setForeground(Color.GRAY);
                }
            }

        });

        //txtProcRealizado
        txtProcRealizado.setText("Descripcion del procedimiento");
        txtProcRealizado.setForeground(Color.GRAY);
        txtProcRealizado.setMargin(new Insets(5, 8, 5, 8));
        txtProcRealizado.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtProcRealizado.getText().equals("Descripcion del procedimiento")) {
                    txtProcRealizado.setText("");
                    txtProcRealizado.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtProcRealizado.getText().trim().isEmpty()) {
                    txtProcRealizado.setText("Descripcion del procedimiento realizado");
                    txtProcRealizado.setForeground(Color.GRAY);
                }
            }

        });

    }

    // =========================================================================
    // 1. CONFIGURACIÓN INICIAL Y CARGA DE DATOS
    // =========================================================================
    private void configurarListas() {
        modelMaquinas = new DefaultListModel<>();
        listaMaquinas.setModel(modelMaquinas);

        modelUsuarios = new DefaultListModel<>();
        listaUsuarios.setModel(modelUsuarios);

        modelTecnicos = new DefaultListModel<>();
        listaTecnicos.setModel(modelTecnicos);

        modelTipos = new DefaultComboBoxModel<>();
        cbAveriaTipo.setModel(modelTipos);
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

        txtMaquinaBuscar.getDocument().addDocumentListener(listenerUnificado);
        txtUsuarioBuscar.getDocument().addDocumentListener(listenerUnificado);
        txtTecnicoBuscar.getDocument().addDocumentListener(listenerUnificado);

        // Aplicar el primer filtro para limpiar las vistas si fuera necesario
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String textoMaq = txtMaquinaBuscar.getText();
        List<Maquinaria> maqFiltrada = controlador.filtrarMaquinas(todasLasMaquinas, textoMaq);
        modelMaquinas.clear();
        if (maqFiltrada != null) {
            modelMaquinas.addAll(maqFiltrada);
        }

        String textoUsu = txtUsuarioBuscar.getText();
        List<Usuario> usuFiltrados = controlador.filtrarUsuarios(todosLosUsuarios, textoUsu);
        modelUsuarios.clear();
        if (usuFiltrados != null) {
            modelUsuarios.addAll(usuFiltrados);
        }

        String textoTec = txtTecnicoBuscar.getText();
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
        cbFechaAsig.addActionListener(e -> spFechaAsig.setEnabled(cbFechaAsig.isSelected()));
        cbFechaAcep.addActionListener(e -> spFechaAcep.setEnabled(cbFechaAcep.isSelected()));
        cbFechaFinal.addActionListener(e -> spFechaFinal.setEnabled(cbFechaFinal.isSelected()));
    }

    private void prepararModoEdicion() {
        if (averiaSeleccionada == null) {
            return;
        }

        try {
            // Textos
            txtAveriaId.setText(String.valueOf(averiaSeleccionada.getCodigoAveria()));
            txtDescripcion.setText(averiaSeleccionada.getDescInicAveria());
            txtProcRealizado.setText(averiaSeleccionada.getProcRealizadoTecnico());

            // Seleccionar Máquina y hacer auto-scroll
            for (int i = 0; i < modelMaquinas.getSize(); i++) {
                if (modelMaquinas.getElementAt(i).getCodigoMaquinaria() == averiaSeleccionada.getMaquinariaFK()) {
                    listaMaquinas.setSelectedIndex(i);
                    listaMaquinas.ensureIndexIsVisible(i);
                    break;
                }
            }

            // Seleccionar Usuario que reporta
            for (int i = 0; i < modelUsuarios.getSize(); i++) {
                if (modelUsuarios.getElementAt(i).getCodigoUsuario() == averiaSeleccionada.getUsuarioReportaFK()) {
                    listaUsuarios.setSelectedIndex(i);
                    listaUsuarios.ensureIndexIsVisible(i);
                    break;
                }
            }

            // Seleccionar Técnico (Opcional)
            if (averiaSeleccionada.getUsuarioTecnicoFK() != null && averiaSeleccionada.getUsuarioTecnicoFK() > 0) {
                for (int i = 0; i < modelTecnicos.getSize(); i++) {
                    if (modelTecnicos.getElementAt(i).getCodigoUsuario() == averiaSeleccionada.getUsuarioTecnicoFK()) {
                        listaTecnicos.setSelectedIndex(i);
                        listaTecnicos.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }

            // Seleccionar Tipo
            for (int i = 0; i < modelTipos.getSize(); i++) {
                if (modelTipos.getElementAt(i).getCodigoTipoAveria() == averiaSeleccionada.getTipoAveriaFK()) {
                    cbAveriaTipo.setSelectedIndex(i);
                    break;
                }
            }

            // Fechas y CheckBoxes
            if (averiaSeleccionada.getFechaInicioAver() != null) {
                spFechaReporte.setValue(convertirALocalDate(averiaSeleccionada.getFechaInicioAver()));
            }

            if (averiaSeleccionada.getFechaAsigTecnico() != null) {
                cbFechaAsig.setSelected(true);
                spFechaAsig.setEnabled(true);
                spFechaAsig.setValue(convertirALocalDate(averiaSeleccionada.getFechaAsigTecnico()));
            }

            if (averiaSeleccionada.getFechaAcepTecnico() != null) {
                cbFechaAcep.setSelected(true);
                spFechaAcep.setEnabled(true);
                spFechaAcep.setValue(convertirALocalDate(averiaSeleccionada.getFechaAcepTecnico()));
            }

            if (averiaSeleccionada.getFechaFinalizTecnico() != null) {
                cbFechaFinal.setSelected(true);
                spFechaFinal.setEnabled(true);
                spFechaFinal.setValue(convertirALocalDate(averiaSeleccionada.getFechaFinalizTecnico()));
            }

            // BLOQUEAR FKs SI EL TÉCNICO YA HA ACEPTADO ---
            if (averiaSeleccionada.getFechaAcepTecnico() != null) {
                // Deshabilitamos los componentes si al averia esta finalizada para que no se peuda modificar pero si ver
                //Listas
                listaMaquinas.setEnabled(false);
                listaUsuarios.setEnabled(false);
                listaTecnicos.setEnabled(false);
                //Combobox
                cbAveriaTipo.setEnabled(false);
                //textos 
                txtDescripcion.setEnabled(false);
                txtProcRealizado.setEnabled(false);
                //fechas
                spFechaReporte.setEnabled(false);
                spFechaAsig.setEnabled(false);
                spFechaAcep.setEnabled(false);
                spFechaFinal.setEnabled(false);
                //checkboxes
                cbFechaAsig.setEnabled(false);
                cbFechaAcep.setEnabled(false);
                cbFechaFinal.setEnabled(false);
                // Por buena experiencia de usuario, bloqueamos también sus buscadores
                txtMaquinaBuscar.setEnabled(false);
                txtUsuarioBuscar.setEnabled(false);
                txtTecnicoBuscar.setEnabled(false);
                
                // boton de guardar desabilitado 
                btnModificarAveria.setEnabled(false);
                // cambiamos el texto del boton. cancelar pasa a cerrar
                btnCancelar.setText("Cerrar");
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

        jPanel1 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtAveriaId = new javax.swing.JTextField();
        cbAveriaTipo = new javax.swing.JComboBox<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        listaMaquinas = new javax.swing.JList<>();
        jScrollPane14 = new javax.swing.JScrollPane();
        listaUsuarios = new javax.swing.JList<>();
        jScrollPane15 = new javax.swing.JScrollPane();
        listaTecnicos = new javax.swing.JList<>();
        txtMaquinaBuscar = new javax.swing.JTextField();
        txtUsuarioBuscar = new javax.swing.JTextField();
        txtTecnicoBuscar = new javax.swing.JTextField();
        jScrollPane16 = new javax.swing.JScrollPane();
        txtProcRealizado = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        spFechaReporte = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        spFechaAsig = new javax.swing.JSpinner();
        cbFechaAsig = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        cbFechaAcep = new javax.swing.JCheckBox();
        spFechaAcep = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        cbFechaFinal = new javax.swing.JCheckBox();
        spFechaFinal = new javax.swing.JSpinner();
        btnModificarAveria = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar avería");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Editar avería");
        jLabel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        txtAveriaId.setBackground(new java.awt.Color(237, 243, 251));
        txtAveriaId.setText("id");
        txtAveriaId.setEnabled(false);
        txtAveriaId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAveriaIdActionPerformed(evt);
            }
        });

        cbAveriaTipo.setBackground(new java.awt.Color(237, 243, 251));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane12.setViewportView(txtDescripcion);

        jScrollPane13.setViewportView(listaMaquinas);

        jScrollPane14.setViewportView(listaUsuarios);

        jScrollPane15.setViewportView(listaTecnicos);

        txtMaquinaBuscar.setBackground(new java.awt.Color(237, 243, 251));

        txtUsuarioBuscar.setBackground(new java.awt.Color(237, 243, 251));

        txtTecnicoBuscar.setBackground(new java.awt.Color(237, 243, 251));
        txtTecnicoBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecnicoBuscarActionPerformed(evt);
            }
        });

        txtProcRealizado.setColumns(20);
        txtProcRealizado.setRows(5);
        jScrollPane16.setViewportView(txtProcRealizado);

        jLabel18.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 204));
        jLabel18.setText("Fecha de reporte:");

        spFechaReporte.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaReporte.setModel(new javax.swing.SpinnerDateModel());

        jLabel19.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 102, 204));
        jLabel19.setText("Fecha de asignación:");

        spFechaAsig.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaAsig.setModel(new javax.swing.SpinnerDateModel());
        spFechaAsig.setEnabled(false);

        jLabel20.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 204));
        jLabel20.setText("Fecha de aceptación:");

        spFechaAcep.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaAcep.setModel(new javax.swing.SpinnerDateModel());
        spFechaAcep.setEnabled(false);

        jLabel21.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 204));
        jLabel21.setText("Fecha de resolución:");

        spFechaFinal.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaFinal.setModel(new javax.swing.SpinnerDateModel());
        spFechaFinal.setEnabled(false);

        btnModificarAveria.setBackground(new java.awt.Color(58, 181, 235));
        btnModificarAveria.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnModificarAveria.setForeground(new java.awt.Color(255, 255, 255));
        btnModificarAveria.setText("Guardar");
        btnModificarAveria.setBorderPainted(false);
        btnModificarAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarAveriaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setText("Procedimiento realizado: ");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 998, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(83, 83, 83)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel21)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cbFechaFinal))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel20)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cbFechaAcep))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cbFechaAsig)))
                                        .addGap(44, 44, 44)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(spFechaAsig, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(spFechaAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(btnModificarAveria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel1)
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbFechaAsig)
                            .addComponent(spFechaAsig, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(spFechaAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbFechaAcep)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21)
                            .addComponent(cbFechaFinal)
                            .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 965, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

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
    // 4. ACCIÓN DE ACTUALIZAR
    // =========================================================================

    private void btnModificarAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarAveriaActionPerformed
        try {
            int idAveria = averiaSeleccionada.getCodigoAveria();
            String descripcion = txtDescripcion.getText();
            String procedimiento = txtProcRealizado.getText();

            Maquinaria maquinaSel = listaMaquinas.getSelectedValue();
            Usuario usuarioRepSel = listaUsuarios.getSelectedValue();
            Usuario tecnicoSel = listaTecnicos.getSelectedValue();
            TipoAveria tipoSel = (TipoAveria) cbAveriaTipo.getSelectedItem();

            // La fecha de reporte siempre está activa
            java.time.LocalDateTime fechaReporte = extraerFechaSpinner(spFechaReporte);

            // Las otras 3 dependen del CheckBox
            java.time.LocalDateTime fechaAsig = cbFechaAsig.isSelected() ? extraerFechaSpinner(spFechaAsig) : null;
            java.time.LocalDateTime fechaAcep = cbFechaAcep.isSelected() ? extraerFechaSpinner(spFechaAcep) : null;
            java.time.LocalDateTime fechaFinal = cbFechaFinal.isSelected() ? extraerFechaSpinner(spFechaFinal) : null;

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
    }//GEN-LAST:event_btnModificarAveriaActionPerformed

    private void txtAveriaIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAveriaIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAveriaIdActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        if (averiaSeleccionada != null && averiaSeleccionada.getFechaAcepTecnico() != null) {
            dispose();
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtTecnicoBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecnicoBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecnicoBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnModificarAveria;
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo;
    private javax.swing.JCheckBox cbFechaAcep;
    private javax.swing.JCheckBox cbFechaAsig;
    private javax.swing.JCheckBox cbFechaFinal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JList<Maquinaria> listaMaquinas;
    private javax.swing.JList<Usuario> listaTecnicos;
    private javax.swing.JList<Usuario> listaUsuarios;
    private javax.swing.JSpinner spFechaAcep;
    private javax.swing.JSpinner spFechaAsig;
    private javax.swing.JSpinner spFechaFinal;
    private javax.swing.JSpinner spFechaReporte;
    private javax.swing.JTextField txtAveriaId;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtMaquinaBuscar;
    private javax.swing.JTextArea txtProcRealizado;
    private javax.swing.JTextField txtTecnicoBuscar;
    private javax.swing.JTextField txtUsuarioBuscar;
    // End of variables declaration//GEN-END:variables
}
