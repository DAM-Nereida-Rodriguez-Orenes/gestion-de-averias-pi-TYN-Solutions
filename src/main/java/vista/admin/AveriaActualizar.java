/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin;

import controlador.AveriaControlador;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.logging.Level;

import modelo.Averia;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;

/**
 * Ventana para Actualizar una Avería existente.
 * @author yosnavmol
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
     * Constructor
     */
    public AveriaActualizar(java.awt.Frame parent, boolean modal, Averia averiaSeleccionada) {
        super(parent, modal);
        this.averiaSeleccionada = averiaSeleccionada;
        
        initComponents();
        
        // Centrar la ventana respecto al padre
        this.setLocationRelativeTo(parent);
        
        // 1. Configurar y cargar datos base
        configurarListas(); 
        cargarDatos();      
        activarFiltros();   
        
        // 2. Preparar el formulario con los datos de la avería
        configurarEventosCheckBoxes(); 
        prepararModoEdicion();         
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
            todosLosTecnicos = controlador.obtenerSoloTecnicos(todosLosUsuarios);

            // Llenar modelos
            if (todasLasMaquinas != null) modelMaquinas.addAll(todasLasMaquinas);
            if (todosLosUsuarios != null) modelUsuarios.addAll(todosLosUsuarios);
            if (todosLosTecnicos != null) modelTecnicos.addAll(todosLosTecnicos);

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
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
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
        if (maqFiltrada != null) modelMaquinas.addAll(maqFiltrada);

        String textoUsu = txtUsuarioBuscar.getText();
        List<Usuario> usuFiltrados = controlador.filtrarUsuarios(todosLosUsuarios, textoUsu);
        modelUsuarios.clear();
        if (usuFiltrados != null) modelUsuarios.addAll(usuFiltrados);

        String textoTec = txtTecnicoBuscar.getText();
        List<Usuario> tecFiltrados = controlador.filtrarUsuarios(todosLosTecnicos, textoTec);
        modelTecnicos.clear();
        if (tecFiltrados != null) modelTecnicos.addAll(tecFiltrados);
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
        if (averiaSeleccionada == null) return;

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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTecnicoBuscar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaTecnicos = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMaquinaBuscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaMaquinas = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaUsuarios = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtUsuarioBuscar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbAveriaTipo = new javax.swing.JComboBox<>();
        btnAveriaActualizar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtAveriaId = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtProcRealizado = new javax.swing.JTextArea();
        spFechaReporte = new javax.swing.JSpinner();
        spFechaAsig = new javax.swing.JSpinner();
        cbFechaAsig = new javax.swing.JCheckBox();
        spFechaAcep = new javax.swing.JSpinner();
        cbFechaAcep = new javax.swing.JCheckBox();
        spFechaFinal = new javax.swing.JSpinner();
        cbFechaFinal = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Actualizar Avería");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Descripción:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Técnico asingado:");

        jLabel4.setText("Buscar:");

        jScrollPane2.setViewportView(listaTecnicos);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Máquina:");

        jLabel6.setText("Buscar:");

        jScrollPane3.setViewportView(listaMaquinas);

        jScrollPane4.setViewportView(listaUsuarios);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Usuario que informa:");

        jLabel8.setText("Buscar:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Tipo de avería:");

        btnAveriaActualizar.setText("Actualizar Avería");
        btnAveriaActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAveriaActualizarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Fecha de reporte:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Código de avería:");

        txtAveriaId.setEditable(false);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Fecha de asignación:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Fecha de aceptación:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Fecha de resolución:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Procedimiento realizado:");

        txtProcRealizado.setColumns(20);
        txtProcRealizado.setRows(5);
        jScrollPane5.setViewportView(txtProcRealizado);

        spFechaReporte.setModel(new javax.swing.SpinnerDateModel());

        spFechaAsig.setModel(new javax.swing.SpinnerDateModel());
        spFechaAsig.setEnabled(false);

        spFechaAcep.setModel(new javax.swing.SpinnerDateModel());
        spFechaAcep.setEnabled(false);

        spFechaFinal.setModel(new javax.swing.SpinnerDateModel());
        spFechaFinal.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1208, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbFechaAsig, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spFechaAsig, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(145, 145, 145)
                                        .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbFechaAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel14)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cbFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(spFechaAcep, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane2)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(36, 36, 36))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(layout.createSequentialGroup()
                .addGap(536, 536, 536)
                .addComponent(btnAveriaActualizar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addComponent(cbFechaAsig))
                            .addComponent(spFechaAsig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(cbFechaAcep))
                            .addComponent(spFechaAcep, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbFechaFinal, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(29, 29, 29)
                .addComponent(btnAveriaActualizar)
                .addContainerGap(274, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    // 4. ACCIÓN DE ACTUALIZAR
    // =========================================================================
    
    private void btnAveriaActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaActualizarActionPerformed
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
    }//GEN-LAST:event_btnAveriaActualizarActionPerformed

    // =========================================================================
    // 5. CÓDIGO AUTOGENERADO (Diseño de Interfaz)
    // =========================================================================

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAveriaActualizar;
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo;
    private javax.swing.JCheckBox cbFechaAcep;
    private javax.swing.JCheckBox cbFechaAsig;
    private javax.swing.JCheckBox cbFechaFinal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
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
