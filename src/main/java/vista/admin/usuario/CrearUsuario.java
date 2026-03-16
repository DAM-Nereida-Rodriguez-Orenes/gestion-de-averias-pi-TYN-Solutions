/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.usuario;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionUsuarioControlador;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import modelo.Rol;
import vista.PanelImgFondo;

/**
 *
 * @author Netri
 */
public class CrearUsuario extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CrearUsuario.class.getName());
    private final GestionUsuarioControlador gestionUsuarioControlador;
    private final GestionUsuario gestionUsuario;

    /**
     * Creates new form CrearUsuario
     */
    public CrearUsuario(java.awt.Frame parent, boolean modal, GestionUsuarioControlador gestionUsuarioControlador, GestionUsuario gestionUsuario) {
        super(parent, modal);
        initComponents();
        mostrarImagenes();
        this.gestionUsuarioControlador = gestionUsuarioControlador;
        this.gestionUsuario = gestionUsuario;

        /**
         * Hemos recuperado los roles de la base de datos y relleno el cbb con
         * esos valores y luego los obtengo como string para poder pasarselos a
         * mi metopdo gestionUsuarioControlador.crearUsuario(nombre, apellido,
         * rol, telefono, email, password);
         */
        List<Rol> listaRoles = gestionUsuarioControlador.recuperarListadoRoles();
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        for (int i = 0; i < listaRoles.size(); i++) {
            modelo.addElement(listaRoles.get(i).getDescripcionRol());
        }
        cbbRol.setModel(modelo);

    }

    public void mostrarImagenes() {
        //icno de la app
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        // Centrar ventana en pantalla
        setLocationRelativeTo(null);
        // Evitar que el usuario cambie el tamaño
        setResizable(false);

        //ICONOS LtextField
        //Campo nombre y apeliido 
        FlatSVGIcon iconoUsuario = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 16, 16);
        txtNombre.putClientProperty("JTextField.leadingIcon", iconoUsuario);
        txtNombre.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtNombre.putClientProperty("JTextField.placeholderText", "Nombre:");
        txtApellidos.putClientProperty("JTextField.leadingIcon", iconoUsuario);
        txtApellidos.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtApellidos.putClientProperty("JTextField.placeholderText", "Apellidos:");
        //Telefono
        FlatSVGIcon iconoTelefono = new FlatSVGIcon("recursos/iconos/icTelefono.svg", 16, 16);
        txtTelefono.putClientProperty("JTextField.leadingIcon", iconoTelefono);
        txtTelefono.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTelefono.putClientProperty("JTextField.placeholderText", "Teléfono:");
        // cbb Prefijos telefonos
        cbbPrefijosTelefonos.setSelectedItem("+34");
        cbbPrefijosTelefonos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean estaSeleccionado,
                    boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, estaSeleccionado, tieneFoco);

                if (indice == -1) {
                    etiqueta.setIcon(iconoTelefono);
                } else {
                    etiqueta.setIcon(null);
                }

                return etiqueta;
            }
        });
        //Correo
        FlatSVGIcon iconoEmail = new FlatSVGIcon("recursos/iconos/icnCorreo.svg", 16, 16);
        txtEmail.putClientProperty("JTextField.leadingIcon", iconoEmail);
        txtEmail.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtEmail.putClientProperty("JTextField.placeholderText", "Email:");
        //contraseña
        FlatSVGIcon iconoPassword = new FlatSVGIcon("recursos/iconos/icnPassword.svg", 16, 16);
        pwdPassword.putClientProperty("JTextField.leadingIcon", iconoPassword);
        pwdPassword.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        pwdPassword.putClientProperty("JTextField.placeholderText", "Contraseña:");
        //cbb Tipo de trabajador
        FlatSVGIcon iconoTipoTrabajador = new FlatSVGIcon("recursos/iconos/icnTpUsuario.svg", 16, 16);
        cbbRol.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> lista,
                    Object valor,
                    int indice,
                    boolean estaSeleccionado,
                    boolean tieneFoco) {

                JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                        lista, valor, indice, estaSeleccionado, tieneFoco);

                if (indice == -1) {
                    etiqueta.setIcon(iconoTipoTrabajador);
                } else {
                    etiqueta.setIcon(null);
                }
                return etiqueta;
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new PanelImgFondo("/recursos/fondoFormularios.png");
        panelBlanco = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        cbbRol = new javax.swing.JComboBox<>();
        pwdPassword = new javax.swing.JPasswordField();
        txtTelefono = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        cbbPrefijosTelefonos = new javax.swing.JComboBox<>();
        btnDarAlta = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGenerarPassword = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nuevo usuario");
        setMaximumSize(new java.awt.Dimension(1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));

        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel2.setRequestFocusEnabled(false);

        panelBlanco.setBackground(new java.awt.Color(255, 255, 255));
        panelBlanco.setMaximumSize(new java.awt.Dimension(800, 515));
        panelBlanco.setMinimumSize(new java.awt.Dimension(800, 515));
        panelBlanco.setPreferredSize(new java.awt.Dimension(800, 515));

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nuevo usuario");

        txtNombre.setBackground(new java.awt.Color(237, 243, 251));

        txtApellidos.setBackground(new java.awt.Color(237, 243, 251));

        cbbRol.setBackground(new java.awt.Color(237, 243, 251));

        pwdPassword.setBackground(new java.awt.Color(237, 243, 251));

        txtTelefono.setBackground(new java.awt.Color(237, 243, 251));

        txtEmail.setBackground(new java.awt.Color(237, 243, 251));

        cbbPrefijosTelefonos.setBackground(new java.awt.Color(237, 243, 251));
        cbbPrefijosTelefonos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+43", "+32", "+359", "+385", "+357", "+420", "+45", "+372", "+358", "+33", "+49", "+30", "+36", "+353", "+39", "+371", "+370", "+352", "+356", "+31", "+48", "+351", "+40", "+421", "+386", "+34", "+46" }));

        btnDarAlta.setBackground(new java.awt.Color(58, 181, 235));
        btnDarAlta.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnDarAlta.setForeground(new java.awt.Color(255, 255, 255));
        btnDarAlta.setText("Crear");
        btnDarAlta.setBorderPainted(false);
        btnDarAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDarAltaActionPerformed(evt);
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

        btnGenerarPassword.setBackground(new java.awt.Color(237, 243, 251));
        btnGenerarPassword.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnGenerarPassword.setForeground(new java.awt.Color(67, 113, 177));
        btnGenerarPassword.setText("Generar Contraseña");
        btnGenerarPassword.setBorderPainted(false);
        btnGenerarPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBlancoLayout = new javax.swing.GroupLayout(panelBlanco);
        panelBlanco.setLayout(panelBlancoLayout);
        panelBlancoLayout.setHorizontalGroup(
            panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBlancoLayout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBlancoLayout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDarAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBlancoLayout.createSequentialGroup()
                        .addComponent(cbbPrefijosTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(txtTelefono))
                    .addGroup(panelBlancoLayout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(txtApellidos))
                    .addComponent(cbbRol, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelBlancoLayout.createSequentialGroup()
                        .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGenerarPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(105, Short.MAX_VALUE))
            .addComponent(jSeparator1)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBlancoLayout.setVerticalGroup(
            panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBlancoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(cbbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbPrefijosTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(panelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDarAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(panelBlanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(panelBlanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDarAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarAltaActionPerformed
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellidos.getText().trim();
        //obtenemso el valor del cbbox
        int selectPrefijo = cbbPrefijosTelefonos.getSelectedIndex();
        String prefijo = cbbPrefijosTelefonos.getItemAt(selectPrefijo);
        String telefono = txtTelefono.getText().trim();
        telefono = prefijo + telefono;
        String email = txtEmail.getText().trim();
        // obtener password del JPasswordField
        char[] passwordArray = pwdPassword.getPassword();
        // convertir a String
        String password = new String(passwordArray).trim();
        //optenemos el rol del comobobox
        int selectedItem = cbbRol.getSelectedIndex();
        String rol = cbbRol.getItemAt(selectedItem);

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes rellenar todos los campos del formulario", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
        } else {
            boolean operacionExitosa = gestionUsuarioControlador.crearUsuario(nombre, apellido, rol, telefono, email, password);
            if (operacionExitosa) {
                JOptionPane.showMessageDialog(this, "Nuevo usuario registrado con exito", "Inserción realizada", JOptionPane.INFORMATION_MESSAGE);

                //limpiamos los campos del fomrulario
                txtNombre.setText("");
                txtApellidos.setText("");
                txtTelefono.setText("");
                txtEmail.setText("");
                pwdPassword.setText("");

                //Actualizo la tabla
                gestionUsuario.mostrarTabla(null);
                //cierro el modal
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se ha podido registrar al usuario", "Error de inserción", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDarAltaActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    /**
     * Genera una password aleatoria valida a traves del controlador y la
     * muestra en el campo de password del formulario.
     */
    private void btnGenerarPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPasswordActionPerformed

        String nuevaPassword = gestionUsuarioControlador.generarPasswordAleatoria();
        pwdPassword.setText(nuevaPassword);
    }//GEN-LAST:event_btnGenerarPasswordActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDarAlta;
    private javax.swing.JButton btnGenerarPassword;
    private javax.swing.JComboBox<String> cbbPrefijosTelefonos;
    private javax.swing.JComboBox<String> cbbRol;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel panelBlanco;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
