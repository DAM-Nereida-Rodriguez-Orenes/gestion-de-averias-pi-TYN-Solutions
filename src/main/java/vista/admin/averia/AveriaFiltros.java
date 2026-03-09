/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import controlador.AveriaControlador;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.logging.Level;

import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;

/**
 * Ventana emergente para configurar los Filtros Avanzados de las Averías.
 * @author yosnavmol
 */
public class AveriaFiltros extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AveriaFiltros.class.getName());

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
    
    // --- Bandera de Estado ---
    // Indica si el usuario pulsó "Aplicar" (true) o cerró la ventana en la 'X' (false)
    private boolean aplicarFiltros = false;
    
    public boolean isAplicarFiltros() {
        return aplicarFiltros;
    }
    
    /**
     * Constructor
     */
    public AveriaFiltros(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        // Centrar ventana respecto al panel principal
        this.setLocationRelativeTo(parent);
        
        // Configuración inicial
        configurarListas(); 
        cargarDatos();      
        activarFiltros();   
        configurarEventosCheckBoxes(); 
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

            todosLosTecnicos = controlador.obtenerSoloTecnicos(todosLosUsuarios);

            if (todasLasMaquinas != null) modelMaquinas.addAll(todasLasMaquinas);
            if (todosLosUsuarios != null) modelUsuarios.addAll(todosLosUsuarios);
            if (todosLosTecnicos != null) modelTecnicos.addAll(todosLosTecnicos);

            if (tipos != null) {
                // 1. Añadimos un elemento nulo para crear visualmente una "casilla en blanco"
                modelTipos.addElement(null); 
                
                // 2. Añadimos los tipos reales
                for (TipoAveria t : tipos) {
                    modelTipos.addElement(t);
                }
                
                // 3. Seleccionamos la casilla en blanco por defecto
                cbAveriaTipo.setSelectedIndex(0);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar datos base en AveriaFiltros", e);
            JOptionPane.showMessageDialog(this, 
                "Error de conexión al cargar los datos para filtrar.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    
    
    // =========================================================================
    // 2. LÓGICA DE INTERFAZ (Buscadores y CheckBoxes)
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
        
        // Carga inicial
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

    private void configurarEventosCheckBoxes() {
        // Estado inicial (Apagados por defecto)
        spFechaReporte.setEnabled(cbFechaReporte.isSelected());
        spFechaFinal.setEnabled(cbFechaFin.isSelected());

        // Reacción al clic
        cbFechaReporte.addActionListener(e -> spFechaReporte.setEnabled(cbFechaReporte.isSelected()));
        cbFechaFin.addActionListener(e -> spFechaFinal.setEnabled(cbFechaFin.isSelected()));
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
        jLabel5 = new javax.swing.JLabel();
        spFechaReporte = new javax.swing.JSpinner();
        spFechaFinal = new javax.swing.JSpinner();
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
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtAveriaId = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTecnicoBuscar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaTecnicos = new javax.swing.JList<>();
        btnFiltrar = new javax.swing.JButton();
        cbFechaReporte = new javax.swing.JCheckBox();
        cbFechaFin = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Filtrar por:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Máquina:");

        spFechaReporte.setModel(new javax.swing.SpinnerDateModel());

        spFechaFinal.setModel(new javax.swing.SpinnerDateModel());

        jLabel6.setText("Buscar:");

        jScrollPane3.setViewportView(listaMaquinas);

        jScrollPane4.setViewportView(listaUsuarios);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Usuario que informa:");

        jLabel8.setText("Buscar:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Tipo de avería:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Fecha de informe:");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Código de avería:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Fecha de resolución:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Técnico asingado:");

        jLabel4.setText("Buscar:");

        jScrollPane2.setViewportView(listaTecnicos);

        btnFiltrar.setText("Aplicar filtros");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbFechaFin))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbFechaReporte)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(446, 446, 446)
                        .addComponent(btnFiltrar)))
                .addContainerGap(37, Short.MAX_VALUE))
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
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbFechaReporte))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbFechaFin)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)))))
                .addGap(18, 18, 18)
                .addComponent(btnFiltrar)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // =========================================================================
    // 3. ACCIÓN DE FILTRAR (Botón Aplicar)
    // =========================================================================
    
    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        this.aplicarFiltros = true; // Avisamos que sí queremos filtrar
        this.dispose();             // Cerramos la ventana
    }//GEN-LAST:event_btnFiltrarActionPerformed

    // =========================================================================
    // 4. GETTERS PÚBLICOS (Para que AveriaListar extraiga los datos elegidos)
    // =========================================================================

    public Integer getFiltroId() {
        try {
            String textoId = txtAveriaId.getText().trim();
            if (textoId.isEmpty()) return null;
            return Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
            // Si el usuario escribe letras en el ID, lo ignoramos y no filtramos por ID
            return null;
        }
    }

    public Integer getFiltroMaquina() {
        Maquinaria m = listaMaquinas.getSelectedValue();
        return (m != null) ? m.getCodigoMaquinaria() : null;
    }

    public Integer getFiltroUsuario() {
        Usuario u = listaUsuarios.getSelectedValue();
        return (u != null) ? u.getCodigoUsuario() : null;
    }

    public Integer getFiltroTecnico() {
        Usuario t = listaTecnicos.getSelectedValue();
        return (t != null) ? t.getCodigoUsuario() : null;
    }

    public Integer getFiltroTipo() {
        if (cbAveriaTipo.getSelectedIndex() == -1) return null;
        
        TipoAveria t = (TipoAveria) cbAveriaTipo.getSelectedItem();
        // Si el usuario deja la casilla en blanco (el elemento null que pusimos), esto devuelve null
        return (t != null) ? t.getCodigoTipoAveria() : null;
    }

    public java.time.LocalDateTime getFiltroFechaReporte() {
        if (cbFechaReporte.isSelected()) {
            return extraerFechaSpinner(spFechaReporte);
        }
        return null; 
    }

    public java.time.LocalDateTime getFiltroFechaFinal() {
        if (cbFechaFin.isSelected()) {
            return extraerFechaSpinner(spFechaFinal);
        }
        return null;
    }
    
    // Convertidor utilitario
    private java.time.LocalDateTime extraerFechaSpinner(javax.swing.JSpinner spinner) {
        java.util.Date date = (java.util.Date) spinner.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
    
    // =========================================================================
    // 5. CÓDIGO AUTOGENERADO (Diseño de la Interfaz)
    // =========================================================================

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo;
    private javax.swing.JCheckBox cbFechaFin;
    private javax.swing.JCheckBox cbFechaReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<Maquinaria> listaMaquinas;
    private javax.swing.JList<Usuario> listaTecnicos;
    private javax.swing.JList<Usuario> listaUsuarios;
    private javax.swing.JSpinner spFechaFinal;
    private javax.swing.JSpinner spFechaReporte;
    private javax.swing.JTextField txtAveriaId;
    private javax.swing.JTextField txtMaquinaBuscar;
    private javax.swing.JTextField txtTecnicoBuscar;
    private javax.swing.JTextField txtUsuarioBuscar;
    // End of variables declaration//GEN-END:variables
}
