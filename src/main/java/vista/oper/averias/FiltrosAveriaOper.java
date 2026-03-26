/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.oper.averias;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.AveriaControlador;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;
import utils.PanelImgFondo;

/**
 *
 * @author Netri
 */
public class FiltrosAveriaOper extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FiltrosAveriaOper.class.getName());

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
     * Creates new form FiltrosAveria
     */
    public FiltrosAveriaOper(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

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
        //Campo Maquina txtMaquinaBuscar
        FlatSVGIcon iconoMaquina = new FlatSVGIcon("recursos/iconos/engranajes_exact.svg", 16, 16);
        txtMaquinaBuscar.putClientProperty("JTextField.leadingIcon", iconoMaquina);
        txtMaquinaBuscar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtMaquinaBuscar.putClientProperty("JTextField.placeholderText", "Buscar máquina: ");
        //campo de ID txtAveriaId
        FlatSVGIcon iconoIdAveria = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        txtAveriaId.putClientProperty("JTextField.leadingIcon", iconoIdAveria);
        txtAveriaId.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtAveriaId.putClientProperty("JTextField.placeholderText", "Código de avería: ");

    }

    private void configurarListas() {
        modelMaquinas = new DefaultListModel<>();
        listaMaquinas.setModel(modelMaquinas);

        modelTipos = new DefaultComboBoxModel<>();
        cbAveriaTipo.setModel(modelTipos);
    }

    private void cargarDatos() {
        try {
            todasLasMaquinas = controlador.obtenerTodasLasMaquinas();
            List<TipoAveria> tipoAveria = controlador.obtenerTiposAveria();

            if (todasLasMaquinas != null) {
                modelMaquinas.addAll(todasLasMaquinas);
            }

            if (tipoAveria != null) {
                // 1. Añadimos un elemento nulo para crear visualmente una "casilla en blanco"
                modelTipos.addElement(null);

                // 2. Añadimos los tipos reales
                for (TipoAveria t : tipoAveria) {
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
    }

    private void configurarEventosCheckBoxes() {
        // Estado inicial (Apagados por defecto)
        spFechaReporte.setEnabled(cbFechaReporte.isSelected());
        spFechaFinal.setEnabled(cbFechaFin.isSelected());

        // Reacción al clic
        cbFechaReporte.addActionListener(e -> spFechaReporte.setEnabled(cbFechaReporte.isSelected()));
        cbFechaFin.addActionListener(e -> spFechaFinal.setEnabled(cbFechaFin.isSelected()));
    }

    // GETTERS PÚBLICOS (Para que AveriaListar extraiga los datos elegidos)
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
        txtMaquinaBuscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaMaquinas = new javax.swing.JList<>();
        jLabel10 = new javax.swing.JLabel();
        cbFechaReporte = new javax.swing.JCheckBox();
        spFechaReporte = new javax.swing.JSpinner();
        spFechaFinal = new javax.swing.JSpinner();
        cbFechaFin = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnFiltrar = new javax.swing.JButton();
        txtAveriaId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Filtrar por");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        cbAveriaTipo.setBackground(new java.awt.Color(237, 243, 251));

        txtMaquinaBuscar.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane3.setViewportView(listaMaquinas);

        jLabel10.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 204));
        jLabel10.setText("Fecha de informe:");

        spFechaReporte.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaReporte.setModel(new javax.swing.SpinnerDateModel());

        spFechaFinal.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        spFechaFinal.setModel(new javax.swing.SpinnerDateModel());

        jLabel14.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 204));
        jLabel14.setText("Fecha de resolución:");

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

        txtAveriaId.setBackground(new java.awt.Color(237, 243, 251));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFiltrar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAveriaId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(cbFechaReporte))
                                    .addComponent(cbFechaFin))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaquinaBuscar)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE))))
                .addGap(0, 34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbFechaReporte)
                                .addComponent(spFechaReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(24, 24, 24)
                            .addComponent(spFechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(34, 34, 34)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cbFechaFin)
                                .addComponent(jLabel14)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
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

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación?", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        this.aplicarFiltros = true; // Avisamos que sí queremos filtrar
        this.dispose();             // Cerramos la ventana
    }//GEN-LAST:event_btnFiltrarActionPerformed


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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<Maquinaria> listaMaquinas;
    private javax.swing.JSpinner spFechaFinal;
    private javax.swing.JSpinner spFechaReporte;
    private javax.swing.JTextField txtAveriaId;
    private javax.swing.JTextField txtMaquinaBuscar;
    // End of variables declaration//GEN-END:variables
}
