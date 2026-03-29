/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.oper.averias;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.AveriaControlador;
import controlador.GestionMaquinasControlador;
import controlador.LoginControlador;
import controlador.TipoAveriaControlador;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;
import utils.PanelImgFondo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Thanya
 */
public class NuevaAveria extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NuevaAveria.class.getName());
    private final AveriaControlador controladorAveria;
    private final GestionMaquinasControlador controladorMaquina;
    private final TipoAveriaControlador controladorTipoAveria;
    private final LoginControlador loginControlador;
    private Usuario usuarioLogueado;
    private List<Maquinaria> listaMaquinasCompleta;

    /**
     * Creates new form NuevaAveria
     */
    public NuevaAveria(java.awt.Frame parent, boolean modal, AveriaControlador controladorAveria) {
        super(parent, modal);
        initComponents();
        listaMaquinas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        this.controladorAveria = controladorAveria;
        this.controladorMaquina = new GestionMaquinasControlador();
        this.controladorTipoAveria = new TipoAveriaControlador();
        this.loginControlador = new LoginControlador();

        this.usuarioLogueado = loginControlador.getUsuarioSesion();

        mostrarImagenes();
        cargarTiposAveria();
        cargarMaquinas();
        mostrarUsuarioLogueado();
        configurarBuscadorMaquinas();
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

    /**
     * Carga los tipos de avería en el JComboBox.
     */
    private void cargarTiposAveria() {
        cbAveriaTipo.removeAllItems();

        List<TipoAveria> listaTipos = controladorAveria.obtenerTiposAveria();

        for (int i = 0; i < listaTipos.size(); i++) {
            cbAveriaTipo.addItem(listaTipos.get(i));
        }
    }

    /**
     * Carga las máquinas en la lista.
     */
    private void cargarMaquinas() {
        DefaultListModel<Maquinaria> modeloLista = new DefaultListModel<>();

        listaMaquinasCompleta = controladorAveria.obtenerTodasLasMaquinas();

        for (int i = 0; i < listaMaquinasCompleta.size(); i++) {
            modeloLista.addElement(listaMaquinasCompleta.get(i));
        }

        listaMaquinas.setModel(modeloLista);
    }

    /**
     * Filtra las máquinas en la lista según el texto ingresado en el campo de búsqueda.
     */
    private void filtrarMaquinas() {
        String textoBusqueda = txtMaquinaBuscar.getText().trim().toLowerCase();
        DefaultListModel<Maquinaria> modeloLista = new DefaultListModel<>();

        for (int i = 0; i < listaMaquinasCompleta.size(); i++) {
            Maquinaria maquina = listaMaquinasCompleta.get(i);

            if (maquina.getNombre().toLowerCase().startsWith(textoBusqueda)) {
                modeloLista.addElement(maquina);
            }
        }

        listaMaquinas.setModel(modeloLista);
    }

    /**
     * Configura el buscador de máquinas para que filtre la lista en tiempo real a medida que el usuario escribe.
     */
    private void configurarBuscadorMaquinas() {
        txtMaquinaBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarMaquinas();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarMaquinas();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarMaquinas();
            }
        });
    }

    /**
     * Muestra el nombre del usuario logueado en el campo de texto correspondiente.
     */
    private void mostrarUsuarioLogueado() {
        if (usuarioLogueado != null) {
            txtUsuarioBuscar.setText("Reporta: " + usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido());
        } else {
            txtUsuarioBuscar.setText("Usuario no identificado");
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
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(505, 505, 505)
                                .addComponent(btnAveriaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(28, 28, 28))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAveriaCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 801, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    /**
     * Maneja el evento de selección de un tipo de avería en el JComboBox. Actualmente no realiza ninguna acción, pero se puede utilizar para implementar lógica adicional si es necesario.
     * @param evt
     */
    private void cbAveriaTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAveriaTipoActionPerformed

    }//GEN-LAST:event_cbAveriaTipoActionPerformed

    /**
     * Maneja el evento de clic en el botón "Crear". Valida los datos ingresados, registra la avería a través del controlador y muestra mensajes de éxito o error según corresponda.
     */
    private void btnAveriaCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAveriaCrearActionPerformed
        try {
            String descripcion = txtDescripcion.getText().trim();
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

            boolean exito = controladorAveria.registrarAveria(descripcion, maquina, null, tipo);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Averia registrada con exito.",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la averia.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al extraer los datos del formulario", e);
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error al intentar leer los datos del formulario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAveriaCrearActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed


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
