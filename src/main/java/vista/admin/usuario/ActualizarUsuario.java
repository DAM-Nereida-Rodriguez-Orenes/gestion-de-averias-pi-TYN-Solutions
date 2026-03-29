/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.usuario;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionUsuarioControlador;
import modelo.Rol;
import utils.PanelImgFondo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Thanya
 */
public class ActualizarUsuario extends javax.swing.JDialog {

    private final GestionUsuarioControlador gestionUsuarioControlador;
    private final GestionUsuario gestionUsuario;
    private final List<Rol> listaRoles;
    private Boolean activo;

    /**
     * Creates new form ActualizarUsuario
     */
    public ActualizarUsuario(java.awt.Frame parent, boolean modal, GestionUsuarioControlador gestionUsuarioControlador, GestionUsuario gestionUsuario) {
        super(parent, modal);
        initComponents();
        this.gestionUsuarioControlador = gestionUsuarioControlador;
        this.gestionUsuario = gestionUsuario;
        mostrarImagenes();
        cargarEstadoUsuario();
        /**
         * Hemos recuperado los roles de la base de datos y relleno el cbb con
         * esos valores y luego los obtengo como string para poder pasarselos a
         * mi metopdo gestionUsuarioControlador.crearUsuario(nombre, apellido,
         * rol, telefono, email, password);
         */
        listaRoles = gestionUsuarioControlador.recuperarListadoRoles();
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        for (int i = 0; i < listaRoles.size(); i++) {
            modelo.addElement(listaRoles.get(i).getDescripcionRol());
        }
        cbbRol.setModel(modelo);
        mostrarDatos();
    }

    private void mostrarDatos() {
        //Nos traemos los datos del usuario     
        txtNombre.setText(gestionUsuarioControlador.getUsuario().getNombre());
        txtApellidos.setText(gestionUsuarioControlador.getUsuario().getApellido());
        txtEmail.setText(gestionUsuarioControlador.getUsuario().getEmail());
        String telefono = gestionUsuarioControlador.getUsuario().getTelefono();
        String prefijoEncontrado = "";
        String numero = telefono;
        // recorrer los prefijos del combobox
        for (int i = 0; i < cbbPrefijosTelefonos.getItemCount(); i++) {

            String prefijo = cbbPrefijosTelefonos.getItemAt(i);

            if (telefono.startsWith(prefijo)) {
                prefijoEncontrado = prefijo;
                numero = telefono.substring(prefijo.length());
                break;
            }
        }
        // pintar en la interfaz
        cbbPrefijosTelefonos.setSelectedItem(prefijoEncontrado);
        txtTelefono.setText(numero);
        pwdPassword.setText(gestionUsuarioControlador.getUsuario().getPassword());

        // Seleccionar el rol del usuario en el combobox
        if (gestionUsuarioControlador.getUsuario().getRol() != null) {
            cbbRol.setSelectedItem(gestionUsuarioControlador.getUsuario().getRol().getDescripcionRol());
        }
        /**
         * Recuperamos el estado actual del usuario desde el controlador. Si el
         * usuario esta activo marcamos el toggle y el boton mostrara la accion
         * que se puede realizar sobre el usuario. - Si esta activo, el boton
         * indicara "Dar de baja". - Si esta inactivo, el boton indicara "Dar de
         * alta".
         */
        activo = gestionUsuarioControlador.getUsuario().isActivo();
        tgbtnReactivar.setSelected(activo);

        if (activo) {
            tgbtnReactivar.setText("Dar de baja");
        } else {
            tgbtnReactivar.setText("Dar de alta");
        }
    }

    public void mostrarImagenes() {
        //icno de la app
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

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
     * Este metodo me ayuda a pintar los estados del usuario en el formulario.
     */
    private void cargarEstadoUsuario() {
        activo = gestionUsuarioControlador.getUsuario().isActivo();
        tgbtnReactivar.setSelected(activo);

        if (activo) {
            estadoUsuario.setText("Activo - dado de alta");
            estadoUsuario.setForeground(Color.GREEN);
        } else {
            estadoUsuario.setText("Inactivo - dado de baja");
            estadoUsuario.setForeground(Color.RED);
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

        jPanel2 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel1 = new javax.swing.JPanel();
        Jlabel5 = new javax.swing.JLabel();
        btnActualizarDatosUsuario = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        cbbRol = new javax.swing.JComboBox<>();
        txtEmail = new javax.swing.JTextField();
        pwdPassword = new javax.swing.JPasswordField();
        cbbPrefijosTelefonos = new javax.swing.JComboBox<>();
        txtTelefono = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        tgbtnReactivar = new javax.swing.JToggleButton();
        estadoUsuario = new javax.swing.JLabel();
        btnGenerarPassword = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar usuario");

        jPanel2.setMaximumSize(new java.awt.Dimension(1000, 600));
        jPanel2.setMinimumSize(new java.awt.Dimension(1000, 600));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 515));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 515));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 515));

        Jlabel5.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        Jlabel5.setForeground(new java.awt.Color(0, 102, 204));
        Jlabel5.setText("Estado del usuario:");

        btnActualizarDatosUsuario.setBackground(new java.awt.Color(58, 181, 235));
        btnActualizarDatosUsuario.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnActualizarDatosUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarDatosUsuario.setText("Guardar");
        btnActualizarDatosUsuario.setBorderPainted(false);
        btnActualizarDatosUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosUsuarioActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Editar usuario");

        txtNombre.setBackground(new java.awt.Color(237, 243, 251));

        txtApellidos.setBackground(new java.awt.Color(237, 243, 251));

        cbbRol.setBackground(new java.awt.Color(237, 243, 251));

        txtEmail.setBackground(new java.awt.Color(237, 243, 251));

        pwdPassword.setBackground(new java.awt.Color(237, 243, 251));

        cbbPrefijosTelefonos.setBackground(new java.awt.Color(237, 243, 251));
        cbbPrefijosTelefonos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "+43", "+32", "+359", "+385", "+357", "+420", "+45", "+372", "+358", "+33", "+49", "+30", "+36", "+353", "+39", "+371", "+370", "+352", "+356", "+31", "+48", "+351", "+40", "+421", "+386", "+34", "+46", "+55", "+81", "+61" }));

        txtTelefono.setBackground(new java.awt.Color(237, 243, 251));

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

        tgbtnReactivar.setBackground(new java.awt.Color(234, 242, 251));
        tgbtnReactivar.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        tgbtnReactivar.setForeground(new java.awt.Color(67, 113, 177));
        tgbtnReactivar.setText("Dar de alta");
        tgbtnReactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgbtnReactivarActionPerformed(evt);
            }
        });

        estadoUsuario.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        estadoUsuario.setForeground(new java.awt.Color(0, 102, 204));
        estadoUsuario.setText("Activo - dado de alta");

        btnGenerarPassword.setBackground(new java.awt.Color(237, 243, 251));
        btnGenerarPassword.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 14)); // NOI18N
        btnGenerarPassword.setForeground(new java.awt.Color(67, 113, 177));
        btnGenerarPassword.setText("Generar Contraseña");
        btnGenerarPassword.setBorderPainted(false);
        btnGenerarPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(340, 340, 340)
                                .addComponent(btnActualizarDatosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cbbPrefijosTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(80, 80, 80))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(Jlabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(estadoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(23, 23, 23)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tgbtnReactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pwdPassword)
                                .addGap(18, 18, 18)
                                .addComponent(btnGenerarPassword)))))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addGap(14, 14, 14)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(cbbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGenerarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbPrefijosTelefonos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Jlabel5)
                    .addComponent(tgbtnReactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(estadoUsuario))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarDatosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPasswordActionPerformed
        String nuevaPassword = gestionUsuarioControlador.generarPasswordAleatoria();
        pwdPassword.setText(nuevaPassword);
    }//GEN-LAST:event_btnGenerarPasswordActionPerformed

    private void tgbtnReactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgbtnReactivarActionPerformed
        boolean nuevoEstado = tgbtnReactivar.isSelected();
        String mensaje;
        String titulo;
        int respuesta;

        if (nuevoEstado) {
            mensaje = "¿Estas segura de que quieres dar de alta a este usuario?";
            titulo = "Reactivar usuario";
        } else {
            mensaje = "¿Estas segura de que quieres dar de baja a este usuario?";
            titulo = "Dar de baja usuario";
        }

        respuesta = JOptionPane.showConfirmDialog(
            this,
            mensaje,
            titulo,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            activo = nuevoEstado;

            if (activo) {
                estadoUsuario.setText("Activo - dado de alta");
                estadoUsuario.setForeground(Color.GREEN);
                tgbtnReactivar.setText("Dar de baja");   //Si esta activo, cambiamos la etiqueta del boton a "Dar de baja"
            } else {
                estadoUsuario.setText("Inactivo - dado de baja");
                estadoUsuario.setForeground(Color.RED);
                tgbtnReactivar.setText("Dar de alta"); //Si esta inactivo, cambiamos la etiqueta del boton a "Dar de alta"
            }
        } else {
            tgbtnReactivar.setSelected(!nuevoEstado);

            if (activo) {
                estadoUsuario.setText("Activo - dado de alta");
                estadoUsuario.setForeground(Color.GREEN);
                tgbtnReactivar.setText("Dar de baja");
            } else {
                estadoUsuario.setText("Inactivo - dado de baja");
                estadoUsuario.setForeground(Color.RED);
                tgbtnReactivar.setText("Dar de alta");
            }
        }
    }//GEN-LAST:event_tgbtnReactivarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnActualizarDatosUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosUsuarioActionPerformed
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
            JOptionPane.showMessageDialog(this, "Debes rellenar todos los campos del formulario", "Datos incompletos",
                JOptionPane.WARNING_MESSAGE);
        } else {
            boolean operacionExitosa = gestionUsuarioControlador.actualizarDatosUsuario(nombre, apellido, rol, telefono, email, password, activo);

            if (operacionExitosa) {
                JOptionPane.showMessageDialog(this, "Datos del usuario actualizado", "Actualización realizada", JOptionPane.INFORMATION_MESSAGE);

                //Actualizo la tabla
                gestionUsuario.mostrarTabla(null);
                //cierro el modal
                this.dispose();
            }
        }
    }//GEN-LAST:event_btnActualizarDatosUsuarioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Jlabel5;
    private javax.swing.JButton btnActualizarDatosUsuario;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGenerarPassword;
    private javax.swing.JComboBox<String> cbbPrefijosTelefonos;
    private javax.swing.JComboBox<String> cbbRol;
    private javax.swing.JLabel estadoUsuario;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JToggleButton tgbtnReactivar;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
