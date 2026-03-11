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

import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;
import vista.PanelImgFondo;

/**
 * Ventana emergente para configurar los Filtros Avanzados de las Averías.
 *
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
        mostrarImagenes();
    }

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
        FlatSVGIcon iconoMaquina = new FlatSVGIcon("recursos/iconos/engranaje.svg", 16, 16);
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

            todosLosTecnicos = controlador.obtenerSoloTecnicos();

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

        // Carga inicial
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

        jPanel1 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbAveriaTipo = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtAveriaId = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTecnicoBuscar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaTecnicos = new javax.swing.JList<>();
        spFechaReporte = new javax.swing.JSpinner();
        spFechaFinal = new javax.swing.JSpinner();
        txtMaquinaBuscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaMaquinas = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaUsuarios = new javax.swing.JList<>();
        txtUsuarioBuscar = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        cbFechaReporte = new javax.swing.JCheckBox();
        cbFechaFin = new javax.swing.JCheckBox();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Filtros");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(950, 590));

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Filtrar por:");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        cbAveriaTipo.setBackground(new java.awt.Color(237, 243, 251));

        jLabel10.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 204));
        jLabel10.setText("Fecha de informe:");

        txtAveriaId.setBackground(new java.awt.Color(237, 243, 251));

        jLabel14.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 204));
        jLabel14.setText("Fecha de resolución:");

        txtTecnicoBuscar.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane2.setViewportView(listaTecnicos);

        spFechaReporte.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaReporte.setModel(new javax.swing.SpinnerDateModel());

        spFechaFinal.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaFinal.setModel(new javax.swing.SpinnerDateModel());

        txtMaquinaBuscar.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane3.setViewportView(listaMaquinas);

        jScrollPane4.setViewportView(listaUsuarios);

        txtUsuarioBuscar.setBackground(new java.awt.Color(237, 243, 251));

        btnFiltrar.setBackground(new java.awt.Color(58, 181, 235));
        btnFiltrar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnFiltrar.setForeground(new java.awt.Color(255, 255, 255));
        btnFiltrar.setText("Aplicar filtros");
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

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
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(75, 75, 75)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(cbFechaReporte))
                            .addComponent(cbFechaFin))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(575, 575, 575)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel14))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbFechaReporte)
                                .addGap(35, 35, 35)
                                .addComponent(cbFechaFin)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación?", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    // =========================================================================
    // 4. GETTERS PÚBLICOS (Para que AveriaListar extraiga los datos elegidos)
    // =========================================================================
    public Integer getFiltroId() {
        try {
            String textoId = txtAveriaId.getText().trim();
            if (textoId.isEmpty()) {
                return null;
            }
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
        if (cbAveriaTipo.getSelectedIndex() == -1) {
            return null;
        }

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
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo;
    private javax.swing.JCheckBox cbFechaFin;
    private javax.swing.JCheckBox cbFechaReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
