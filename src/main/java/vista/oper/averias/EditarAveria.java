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
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import modelo.Averia;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;
import vista.PanelImgFondo;

/**
 *
 * @author Netri
 */
public class EditarAveria extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EditarAveria.class.getName());
    private Averia averiaSeleccionada;
    private final GestionMaquinasControlador controladorMaquina;
    private final AveriaControlador averiaControlador;
    private final TipoAveriaControlador controladorTipoAveria;
    private List<Maquinaria> listaMaquinasCompleta;
    private final LoginControlador loginControlador;
    private Usuario usuarioLogueado;

    /**
     * Creates new form EditarAveria
     */
    public EditarAveria(java.awt.Frame parent, boolean modal, Averia averiaSeleccionada) {
        super(parent, modal);
        initComponents();

        this.averiaSeleccionada = averiaSeleccionada;
        this.controladorMaquina = new GestionMaquinasControlador();
        this.averiaControlador = new AveriaControlador();
        this.controladorTipoAveria = new TipoAveriaControlador();
        this.loginControlador = new LoginControlador();
        this.usuarioLogueado = loginControlador.getUsuarioSesion();

        System.out.println("Descripcion: " + averiaSeleccionada.getDescInicAveria());
        System.out.println("MaquinariaFK: " + averiaSeleccionada.getMaquinariaFK());
        System.out.println("TipoAveriaFK: " + averiaSeleccionada.getTipoAveriaFK());

        mostrarImagenes();
        cargarTiposAveria();
        cargarMaquinas();
        cargarDatosAveria();
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

        //PlaceHolder textArea
        //txtDescripcion
        if (txtDescripcion.getText() == null || txtDescripcion.getText().trim().isEmpty()) {
            txtDescripcion.setText("Descripcion de la averia");
            txtDescripcion.setForeground(Color.GRAY);
        } else {
            txtDescripcion.setForeground(Color.BLACK);
        }

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

    private void cargarDatosAveria() {
        if (averiaSeleccionada == null) {
            return;
        }
        // Descripcion
        txtDescripcion.setText(averiaSeleccionada.getDescInicAveria());

        if (usuarioLogueado != null) {
            txtUsuarioBuscar.setText("Reporta: "
                    + usuarioLogueado.getNombre() + " "
                    + usuarioLogueado.getApellido());
        } else {
            txtUsuarioBuscar.setText("Usuario no identificado");
        }

        // Maquina
        seleccionarMaquina();
        // Tipo averia
        seleccionarTipoAveria();
    }

    private void cargarMaquinas() {
        DefaultListModel<Maquinaria> modeloLista = new DefaultListModel<>();
        listaMaquinasCompleta = controladorMaquina.listarMaquinaria();

        for (int i = 0; i < listaMaquinasCompleta.size(); i++) {
            modeloLista.addElement(listaMaquinasCompleta.get(i));
        }

        listaMaquinas.setModel(modeloLista);
    }

    private void cargarTiposAveria() {
        cbAveriaTipo.removeAllItems();

        List<TipoAveria> listaTipos = controladorTipoAveria.listarTiposAveria();

        for (int i = 0; i < listaTipos.size(); i++) {
            cbAveriaTipo.addItem(listaTipos.get(i));
        }
    }

    private void seleccionarMaquina() {
        javax.swing.ListModel<Maquinaria> modelo = listaMaquinas.getModel();

        for (int i = 0; i < modelo.getSize(); i++) {
            Maquinaria maquina = modelo.getElementAt(i);

            if (maquina.getCodigoMaquinaria() == averiaSeleccionada.getMaquinariaFK()) {
                listaMaquinas.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarTipoAveria() {
        for (int i = 0; i < cbAveriaTipo.getItemCount(); i++) {
            TipoAveria tipo = (TipoAveria) cbAveriaTipo.getItemAt(i);

            if (tipo.getCodigoTipoAveria() == averiaSeleccionada.getTipoAveriaFK()) {
                cbAveriaTipo.setSelectedIndex(i);
                break;
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

        jPanel1 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel2 = new javax.swing.JPanel();
        txtMaquinaBuscar = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        listaMaquinas = new javax.swing.JList<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        cbAveriaTipo = new javax.swing.JComboBox<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        txtUsuarioBuscar = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnEditarAveria = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar avería");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtMaquinaBuscar.setBackground(new java.awt.Color(237, 243, 251));
        txtMaquinaBuscar.setForeground(new java.awt.Color(67, 113, 177));

        jScrollPane13.setViewportView(listaMaquinas);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Editar Avería");

        cbAveriaTipo.setBackground(new java.awt.Color(237, 243, 251));
        cbAveriaTipo.setForeground(new java.awt.Color(67, 113, 177));
        cbAveriaTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAveriaTipoActionPerformed(evt);
            }
        });

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane12.setViewportView(txtDescripcion);

        txtUsuarioBuscar.setEditable(false);
        txtUsuarioBuscar.setBackground(new java.awt.Color(237, 243, 251));
        txtUsuarioBuscar.setForeground(new java.awt.Color(67, 113, 177));

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

        btnEditarAveria.setBackground(new java.awt.Color(58, 181, 235));
        btnEditarAveria.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnEditarAveria.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarAveria.setText("Guardar");
        btnEditarAveria.setBorderPainted(false);
        btnEditarAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarAveriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEditarAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(23, 23, 23))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaquinaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbAveriaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(txtUsuarioBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditarAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
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

    private void cbAveriaTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAveriaTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbAveriaTipoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEditarAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarAveriaActionPerformed
        try {
            //Datos que sacamos del formulario
            String descripcion = txtDescripcion.getText();
            Maquinaria maquinaSel = listaMaquinas.getSelectedValue();
            TipoAveria tipoSel = (TipoAveria) cbAveriaTipo.getSelectedItem();

            //Validaciones 
            if (descripcion.isEmpty() || descripcion.equals("Descripcion de la averia")) {
                JOptionPane.showMessageDialog(this,
                        "La descripcion es obligatoria.",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (maquinaSel == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar una maquina.",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tipoSel == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un tipo de averia.",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //Mantenemos los datos de la averia selecionada 
            int idAveria = averiaSeleccionada.getCodigoAveria();
            String procedimiento = averiaSeleccionada.getProcRealizadoTecnico();
            Usuario usuarioReporta = usuarioLogueado;
            Usuario usuTecnico = null;

            // Mantener fechas originales
            java.time.LocalDateTime fechaReporte = averiaSeleccionada.getFechaInicioAver();
            java.time.LocalDateTime fechaAsig = averiaSeleccionada.getFechaAsigTecnico();
            java.time.LocalDateTime fechaAcep = averiaSeleccionada.getFechaAcepTecnico();
            java.time.LocalDateTime fechaFinal = averiaSeleccionada.getFechaFinalizTecnico();

            boolean exito = averiaControlador.actualizarAveria(
                    idAveria,
                    descripcion,
                    procedimiento,
                    maquinaSel,
                    usuarioReporta,
                    usuTecnico,
                    tipoSel,
                    fechaReporte,
                    fechaAsig,
                    fechaAcep,
                    fechaFinal
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
    }//GEN-LAST:event_btnEditarAveriaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditarAveria;
    private javax.swing.JComboBox<TipoAveria> cbAveriaTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<Maquinaria> listaMaquinas;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtMaquinaBuscar;
    private javax.swing.JTextField txtUsuarioBuscar;
    // End of variables declaration//GEN-END:variables
}
