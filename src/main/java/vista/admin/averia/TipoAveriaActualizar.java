/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import utils.PanelImgFondo;

/**
 *
 * @author Netri
 */
public class TipoAveriaActualizar extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TipoAveriaActualizar.class.getName());
    private final controlador.TipoAveriaControlador controlador = new controlador.TipoAveriaControlador();
    private javax.swing.table.DefaultTableModel modeloTabla;

    /**
     * Creates new form TipoAveriaActualizar
     */
    public TipoAveriaActualizar(java.awt.Frame parent, boolean modal, int id, String desc, float tiempo) {
        super(parent, modal);
        initComponents();
        inicializarSpinnerNumeroTipoActualizar();
        mostrarImagenes();

        txtIdActualizar.setText(String.valueOf(id));
        txtDescripcionActualizar.setText(desc);
        txtTiempoActualizar.setText(String.valueOf(tiempo));
        cargarSpinnerActualizarDesdeId(id);
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
        //Ajustes del deisño del JFrame/Layout
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        // Tamaño inical de todas las ventanas
        this.setSize(1200, 800);
        // No se puede hacer más pequeña de 1200,800
        this.setMinimumSize(new Dimension(1200, 800));
        // Permite usar el botón de maximizar
        this.setResizable(true);
        // Centrar ventana en pantalla
        this.setLocationRelativeTo(null);

        //Actualizar id
        FlatSVGIcon iconoCodigoActualizar = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        txtIdActualizar.putClientProperty("JTextField.leadingIcon", iconoCodigoActualizar);
        txtIdActualizar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtIdActualizar.putClientProperty("JTextField.placeholderText", "Código Rol:  4 ");
        txtIdActualizar.setEditable(false);
        txtIdActualizar.setEnabled(false);
        //SPinner 
        spnNumeroAveriaActualizar.setEnabled(false);
        // tiempo 
        FlatSVGIcon iconoTimeActualizar = new FlatSVGIcon("recursos/iconos/temporizador.svg", 16, 16);
        txtTiempoActualizar.putClientProperty("JTextField.leadingIcon", iconoTimeActualizar);
        txtTiempoActualizar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTiempoActualizar.putClientProperty("JTextField.placeholderText", "tiempo estimado en reparar (en horas):  ");
        //Descrpcion
        FlatSVGIcon iconoDescripcionActualizar = new FlatSVGIcon("recursos/iconos/icnEtiqueta.svg", 16, 16);
        txtDescripcionActualizar.putClientProperty("JTextField.leadingIcon", iconoDescripcionActualizar);
        txtDescripcionActualizar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtDescripcionActualizar.putClientProperty("JTextField.placeholderText", "Descripción ");
    }

    /**
     * ACTUALIZAR TIPO AVERIA. metodos que utiliza ese spinner
     */
    private void inicializarSpinnerNumeroTipoActualizar() {
        SpinnerNumberModel modeloNumeroTipoActualizar = new SpinnerNumberModel(1, 1, 99, 1);
        spnNumeroAveriaActualizar.setModel(modeloNumeroTipoActualizar);

        JSpinner.NumberEditor editorNumeroTipoActualizar = new JSpinner.NumberEditor(spnNumeroAveriaActualizar, "00");
        spnNumeroAveriaActualizar.setEditor(editorNumeroTipoActualizar);
    }

    private void cargarSpinnerActualizarDesdeId(int idCompleto) {
        String textoId = String.valueOf(idCompleto);

        if (textoId.startsWith("4") && textoId.length() >= 2) {
            String parteNumerica = textoId.substring(1); // por ejemplo 401 -> 01
            int numeroSpinner = Integer.parseInt(parteNumerica);
            spnNumeroAveriaActualizar.setValue(numeroSpinner);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        java.util.List<Object[]> datos = controlador.listarParaTabla();
        if (datos != null) {
            for (Object[] fila : datos) {
                modeloTabla.addRow(fila);
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

        jPanel4 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel5 = new javax.swing.JPanel();
        btnTipoAveriaGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtIdActualizar = new javax.swing.JTextField();
        txtTiempoActualizar = new javax.swing.JTextField();
        txtDescripcionActualizar = new javax.swing.JTextField();
        spnNumeroAveriaActualizar = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        btnCancelar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setMaximumSize(new java.awt.Dimension(1000, 600));
        jPanel4.setPreferredSize(new java.awt.Dimension(1000, 600));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnTipoAveriaGuardar.setBackground(new java.awt.Color(58, 181, 235));
        btnTipoAveriaGuardar.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnTipoAveriaGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnTipoAveriaGuardar.setText("Guardar");
        btnTipoAveriaGuardar.setBorderPainted(false);
        btnTipoAveriaGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAveriaGuardarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 204));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Editar tipo de avería");

        txtIdActualizar.setEditable(false);
        txtIdActualizar.setBackground(new java.awt.Color(237, 243, 251));

        txtTiempoActualizar.setBackground(new java.awt.Color(237, 243, 251));

        txtDescripcionActualizar.setBackground(new java.awt.Color(237, 243, 251));

        btnCancelar1.setBackground(new java.awt.Color(234, 242, 251));
        btnCancelar1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnCancelar1.setForeground(new java.awt.Color(67, 113, 177));
        btnCancelar1.setText("Cancelar");
        btnCancelar1.setBorderPainted(false);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTipoAveriaGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDescripcionActualizar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTiempoActualizar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtIdActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addComponent(spnNumeroAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(94, 94, 94))
            .addComponent(jSeparator2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnNumeroAveriaActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(txtTiempoActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(txtDescripcionActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTipoAveriaGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTipoAveriaGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoAveriaGuardarActionPerformed
        // TODO add your handling code here:
        try {
            // El ID lo sacamos pero no dejamos que lo editen
            int id = Integer.parseInt(txtIdActualizar.getText().trim());
            // El spinner solo refleja visualmente el numero del ID
            // pero no se utiliza para modificarlo
            int numeroSpinner = (int) spnNumeroAveriaActualizar.getValue();

            float tiempo = Float.parseFloat(txtTiempoActualizar.getText().trim());
            String desc = txtDescripcionActualizar.getText().trim();

            boolean exito = controlador.actualizar(id, desc, tiempo);

            if (exito) {
                //javax.swing.JOptionPane.showMessageDialog(this, "Tipo de avería actualizado con éxito.");
                //cargarDatos(); // Refrescamos la tabla principal
                
                JOptionPane.showMessageDialog(this, "Tipo de avería actualizado con éxito.");
                dispose();
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "El tiempo debe ser un valor numérico (ej. 2.5).",
                    "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTipoAveriaGuardarActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas cancelar la operación? Los cambios no se guardarán.",
            "Cancelar operación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnTipoAveriaGuardar;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSpinner spnNumeroAveriaActualizar;
    private javax.swing.JTextField txtDescripcionActualizar;
    private javax.swing.JTextField txtIdActualizar;
    private javax.swing.JTextField txtTiempoActualizar;
    // End of variables declaration//GEN-END:variables
}
