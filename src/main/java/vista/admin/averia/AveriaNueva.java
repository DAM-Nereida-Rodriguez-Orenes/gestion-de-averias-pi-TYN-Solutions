/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.AveriaControlador;
import controlador.TipoAveriaControlador;
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
import utils.PanelImgFondo;

/**
 * Ventana para la creación de una Nueva Avería.
 *
 * @author yosnavmol
 */
public class AveriaNueva extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AveriaNueva.class.getName());

    // --- Controlador y Listas Maestras ---
    private final AveriaControlador controlador;
    private List<Maquinaria> todasLasMaquinas;
    //private List<Usuario> todosLosUsuarios;
    private List<Usuario> todosLosTecnicos;
    private TipoAveriaControlador tipoAveriaControlador;

    // --- Modelos Visuales ---
    private DefaultListModel<Maquinaria> modelMaquinas;
    //private DefaultListModel<Usuario> modelUsuarios;
    private DefaultListModel<Usuario> modelTecnicos;
    private DefaultComboBoxModel<TipoAveria> modelTipos;

    /**
     * Constructor
     */
    public AveriaNueva(java.awt.Frame parent, boolean modal, AveriaControlador controlador) {
        super(parent, modal);
        this.controlador = controlador;
        initComponents();

        // Centrar la ventana respecto al padre
        this.setLocationRelativeTo(parent);

        // Configuración inicial
        configurarListas();
        cargarDatos();
        activarFiltros();
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

        //Campo usuario que informa txtUsuarioBuscar
        FlatSVGIcon iconoUsuarioReporta = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 16, 16);
        txtUsuarioBuscar.putClientProperty("JTextField.leadingIcon", iconoUsuarioReporta);
        txtUsuarioBuscar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtUsuarioBuscar.setFont(new java.awt.Font("Microsoft JhengHei", java.awt.Font.BOLD, 14));
        txtUsuarioBuscar.setDisabledTextColor(new java.awt.Color(67, 113, 177));
        txtUsuarioBuscar.setEditable(false);
        txtUsuarioBuscar.setEnabled(false);

        // Campo descripcion de averia
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
    }

    // =========================================================================
    // 1. CONFIGURACIÓN INICIAL Y CARGA DE DATOS
    // =========================================================================
    private void configurarListas() {
        modelMaquinas = new DefaultListModel<>();
        listaMaquinas.setModel(modelMaquinas);

        // setModel(modelUsuarios);
        modelTecnicos = new DefaultListModel<>();

        modelTipos = new DefaultComboBoxModel<>();
        modelTipos.addElement(null);  // Placeholder
        cbAveriaTipo.setModel(modelTipos);
    }

    private void cargarDatos() {
        try {
            // 1. Descargar datos de la BD
            todasLasMaquinas = controlador.obtenerTodasLasMaquinas();
            // todosLosUsuarios = controlador.obtenerTodosLosUsuarios();
            List<TipoAveria> tipos = controlador.obtenerTiposAveria();
            todosLosTecnicos = controlador.obtenerSoloTecnicos();

            // 3. Llenar los modelos visuales
            if (todasLasMaquinas != null) {
                modelMaquinas.addAll(todasLasMaquinas);
            }
            /*if (todosLosUsuarios != null) {
                modelUsuarios.addAll(todosLosUsuarios);
            }*/
            if (todosLosTecnicos != null) {
                modelTecnicos.addAll(todosLosTecnicos);
            }

            /**
             * En este for es donde estamos rellenando los datos del combobox
             * desde la base de datos. En cargarDatos() se obtienen los tipos
             * desde el controlador:
             */
            if (tipos != null) {
                for (TipoAveria t : tipos) {
                    modelTipos.addElement(t);
                }
            }

            // Obtener el usuario logueado y mostrar su nombre en el campo
            Usuario usuarioLogueado = controlador.getUsuarioSesion();
            if (usuarioLogueado != null) {
                txtUsuarioBuscar.setText("Reporta: " + usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido());
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar los datos en AveriaNueva", e);
            JOptionPane.showMessageDialog(this,
                    "Error de conexión al cargar los datos iniciales.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 2. LÓGICA DE FILTRADO (Buscadores en tiempo real)
    // =========================================================================
    private void activarFiltros() {
        // Listener unificado que reacciona cada vez que se escribe o borra una letra
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

        // Aplicar el filtro inicial vacío para cargar las listas por primera vez
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        // --- Filtrar Máquinas ---
        String textoMaq = txtMaquinaBuscar.getText();
        List<Maquinaria> maqFiltrada = controlador.filtrarMaquinas(todasLasMaquinas, textoMaq);
        modelMaquinas.clear();
        if (maqFiltrada != null) {
            modelMaquinas.addAll(maqFiltrada);
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
        jPanel2 = new javax.swing.JPanel();
        cbAveriaTipo = new javax.swing.JComboBox<>();
        btnAveriaCrear = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMaquinaBuscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaMaquinas = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        txtUsuarioBuscar = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nueva avería");
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(950, 590));

        cbAveriaTipo.setBackground(new java.awt.Color(237, 243, 251));
        cbAveriaTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAveriaTipoActionPerformed(evt);
            }
        });

        btnAveriaCrear.setBackground(new java.awt.Color(58, 181, 235));
        btnAveriaCrear.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnAveriaCrear.setForeground(new java.awt.Color(255, 255, 255));
        btnAveriaCrear.setText("Crear");
        btnAveriaCrear.setBorderPainted(false);
        btnAveriaCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAveriaCrearActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nueva avería");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        txtMaquinaBuscar.setBackground(new java.awt.Color(237, 243, 251));

        jScrollPane3.setPreferredSize(new java.awt.Dimension(214, 86));

        jScrollPane3.setViewportView(listaMaquinas);

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        txtUsuarioBuscar.setBackground(new java.awt.Color(237, 243, 251));

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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAveriaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbAveriaTipo, 0, 350, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1)
                                    .addComponent(txtUsuarioBuscar))
                                .addGap(45, 45, 45)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMaquinaBuscar)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)))))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAveriaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
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
    // 3. ACCIÓN DE GUARDAR
    // =========================================================================
    /**
     * Este metodo recoge los datos escritos o seleccionados en el formulario de
     * nueva averia y se los envia al controlador.
     *
     * Antes tambien se obtenia manualmente el usuario que reportaba la averia,
     * pero eso se ha cambiado para mantener mejor el patron MVC.
     *
     * Ahora la vista ya no se encarga de saber que usuario ha iniciado sesion.
     * Solo recoge los datos visuales del formulario: - descripcion - maquina -
     * tecnico - tipo de averia
     *
     * El usuario que reporta la averia se obtiene automaticamente en
     * AveriaControlador a partir de la sesion guardada en LoginControlador.
     */
    private void btnAveriaCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaCrearActionPerformed
        try {
            // Recogemos los datos del formulario
            String descripcion = txtDescripcion.getText();
            Maquinaria maquina = listaMaquinas.getSelectedValue();
            TipoAveria tipo = (TipoAveria) cbAveriaTipo.getSelectedItem();

            if (descripcion.isEmpty() || descripcion.equals("Descripcion de la averia")) {
                JOptionPane.showMessageDialog(this,
                        "Debes introducir una descripcion de la averia.",
                        "Validacion",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (maquina == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar una maquina.",
                        "Validacion",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tipo == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un tipo de averia.",
                        "Validacion",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 2. Mandar al controlador
            // Mandamos al controlador solo los datos del formulario.
            // El usuario que reporta ya no se pasa desde la vista,
            // porque el controlador lo recupera automaticamente desde la sesion.
            boolean exito = controlador.registrarAveria(descripcion, maquina, null, tipo);

            // 4. Procesar resultado
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Avería registrada con éxito.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Cierra el JDialog y devuelve el control
            }
            // Nota: Si exito es false, el controlador ya se encarga de mostrar el JOptionPane específico.

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al extraer los datos del formulario", e);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al intentar leer los datos del formulario.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAveriaCrearActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cbAveriaTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAveriaTipoActionPerformed

    }//GEN-LAST:event_cbAveriaTipoActionPerformed

    // =========================================================================
    // 4. CÓDIGO AUTOGENERADO (Diseño de la Interfaz)
    // =========================================================================

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAveriaCrear;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<Maquinaria> listaMaquinas;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtMaquinaBuscar;
    private javax.swing.JTextField txtUsuarioBuscar;
    // End of variables declaration//GEN-END:variables
}
