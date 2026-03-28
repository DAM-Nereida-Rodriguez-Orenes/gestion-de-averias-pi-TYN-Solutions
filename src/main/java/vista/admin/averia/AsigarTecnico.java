/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista.admin.averia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.AveriaControlador;
import controlador.GestionMaquinasControlador;
import controlador.TipoAveriaControlador;
import modelo.Averia;
import modelo.Maquinaria;
import modelo.TipoAveria;
import modelo.Usuario;
import utils.PanelImgFondo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 *
 * @author Thanya
 */
public class AsigarTecnico extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AsigarTecnico.class.getName());
    //Controladores 
    private final AveriaControlador controlador;
    private final GestionMaquinasControlador controladorMaquina;
    private TipoAveriaControlador controladorTipoAveria;
    //Listas y modelos 
    private DefaultListModel<Usuario> modelTecnicos;
    private List<Usuario> todosLosTecnicos;
    //variables globales 
    private final Averia averiaSeleccionada;

    public AsigarTecnico(java.awt.Frame parent, boolean modal, Averia averiaSeleccionada) {
        super(parent, modal);
        initComponents();

        this.averiaSeleccionada = averiaSeleccionada;
        this.controlador = new AveriaControlador();
        this.controladorMaquina = new GestionMaquinasControlador();
        this.controladorTipoAveria = new TipoAveriaControlador();

        configurarListas();
        cargarDatos();
        activarFiltros();
        activarSeleccionTecnico();
        mostrarImagenes();
    }

    // DISEÑO 
    public void mostrarImagenes() {
        //icno de la app
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        this.setLocationRelativeTo(null);
        FlatSVGIcon iconoTipoAveria = new FlatSVGIcon("recursos/iconos/llave_exact.svg", 16, 16);

        //iconos de los campos del formulario
        //campo de ID 
        FlatSVGIcon iconoIdAveria = new FlatSVGIcon("recursos/iconos/icnNumerico.svg", 16, 16);
        txtAveriaId.putClientProperty("JTextField.leadingIcon", iconoIdAveria);
        txtAveriaId.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtAveriaId.putClientProperty("JTextField.placeholderText", "Código de avería: ");
        //Campo Nombre de Maquina 
        FlatSVGIcon iconoMaquina = new FlatSVGIcon("recursos/iconos/engranajes_exact.svg", 16, 16);
        txtNombreMaquina.putClientProperty("JTextField.leadingIcon", iconoMaquina);
        txtNombreMaquina.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtNombreMaquina.putClientProperty("JTextField.placeholderText", "Máquina: ");
        // Campo Tipo de averia txtTipoAveria
        FlatSVGIcon iconoTipAveria = new FlatSVGIcon("recursos/iconos/llave_exact.svg", 16, 16);
        txtTipoAveria.putClientProperty("JTextField.leadingIcon", iconoMaquina);
        txtTipoAveria.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTipoAveria.putClientProperty("JTextField.placeholderText", "Tipo de avería:: ");
        //Campo usuario que informa txtUsuarioBuscar
        FlatSVGIcon iconoUsuariotecnico = new FlatSVGIcon("recursos/iconos/icnUsuario.svg", 15, 15);
        txtTecnicoBuscar.putClientProperty("JTextField.leadingIcon", iconoUsuariotecnico);
        txtTecnicoBuscar.putClientProperty("JComponent.padding", new Insets(5, 8, 5, 8));
        txtTecnicoBuscar.putClientProperty("JTextField.placeholderText", "Tecnico asingado: ");
    }

    private void configurarListas() {
        modelTecnicos = new DefaultListModel<>();
        listaTecnicos.setModel(modelTecnicos);
    }

    private void cargarDatos() {
        try {
            int tipo = averiaSeleccionada.getTipoAveriaFK();

            // Cargar lista de tecnicos ordenados por carga
            todosLosTecnicos = controlador.buscarTecnicosOrdenadorPorCarga(tipo);

            modelTecnicos.clear();
            if (todosLosTecnicos != null) {
                modelTecnicos.addAll(todosLosTecnicos);
            }

            // ID averia
            txtAveriaId.setText(String.valueOf(averiaSeleccionada.getCodigoAveria()));

            // Maquina
            Optional<Maquinaria> optMaquina = controladorMaquina.buscarMaquinaPorID(averiaSeleccionada.getMaquinariaFK());
            if (optMaquina.isPresent()) {
                Maquinaria maquina = optMaquina.get();
                txtNombreMaquina.setText(maquina.getNombre());
            } else {
                txtNombreMaquina.setText("");
            }

            // Tipo de averia
            List<TipoAveria> tipos = controlador.obtenerTiposAveria();
            txtTipoAveria.setText("");

            if (tipos != null) {
                for (int i = 0; i < tipos.size(); i++) {
                    TipoAveria tipoActual = tipos.get(i);

                    if (tipoActual.getCodigoTipoAveria() == averiaSeleccionada.getTipoAveriaFK()) {
                        txtTipoAveria.setText(tipoActual.getDescripcionTipoAv());
                        break;
                    }
                }
            }

            // El buscador SIEMPRE vacio
            txtTecnicoBuscar.setText("");

            // Marcar en la lista el tecnico actual de la averia, pero sin poner su nombre en el buscador
            if (averiaSeleccionada.getUsuarioTecnicoFK() != null && averiaSeleccionada.getUsuarioTecnicoFK() > 0) {
                for (int i = 0; i < modelTecnicos.size(); i++) {
                    Usuario tecnico = modelTecnicos.getElementAt(i);

                    if (tecnico.getCodigoUsuario() == averiaSeleccionada.getUsuarioTecnicoFK()) {
                        listaTecnicos.setSelectedIndex(i);
                        listaTecnicos.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar datos base en AsigarTecnico", e);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos de la base de datos.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
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

        txtTecnicoBuscar.getDocument().addDocumentListener(listenerUnificado);
    }

    private void aplicarFiltros() {
        String textoTecnico = txtTecnicoBuscar.getText().trim();

        modelTecnicos.clear();

        if (todosLosTecnicos == null) {
            return;
        }

        if (textoTecnico.isEmpty()) {
            modelTecnicos.addAll(todosLosTecnicos);
            return;
        }

        for (int i = 0; i < todosLosTecnicos.size(); i++) {
            Usuario tecnico = todosLosTecnicos.get(i);

            String nombreCompleto = tecnico.getNombre() + " " + tecnico.getApellido();

            if (nombreCompleto.toLowerCase().contains(textoTecnico.toLowerCase())) {
                modelTecnicos.addElement(tecnico);
            }
        }
    }

    private void activarSeleccionTecnico() {
        listaTecnicos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarMotivosTecnico();
            }
        });
    }

    private void mostrarMotivosTecnico() {

        Usuario tecnicoSeleccionado = listaTecnicos.getSelectedValue();

        if (tecnicoSeleccionado == null) {
            txtMotivosTecnico.setText("");
            return;
        }

        int codigoTecnico = tecnicoSeleccionado.getCodigoUsuario();
        int codigoTipoAveria = averiaSeleccionada.getTipoAveriaFK();

        Object[] datos = controlador.obtenerMotivosTecnico(codigoTecnico, codigoTipoAveria);

        if (datos != null) {

            int totalActivas = (int) datos[0];
            int totalFinalizadas = (int) datos[1];
            double tiempoMedio = (double) datos[2];

            String texto = "Averias activas: " + totalActivas + "\n"
                    + "Averias finalizadas de este tipo: " + totalFinalizadas + "\n"
                    + "Tiempo medio de resolucion: " + String.format("%.2f", tiempoMedio) + " horas";

            txtMotivosTecnico.setText(texto);

        } else {
            txtMotivosTecnico.setText("No hay datos disponibles.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new PanelImgFondo("/recursos/fondoFormularios.png");
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        txtAveriaId = new javax.swing.JTextField();
        txtTipoAveria = new javax.swing.JTextField();
        txtNombreMaquina = new javax.swing.JTextField();
        jScrollPane15 = new javax.swing.JScrollPane();
        listaTecnicos = new javax.swing.JList<>();
        txtTecnicoBuscar = new javax.swing.JTextField();
        btnGuadarTecnico = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMotivosTecnico = new javax.swing.JTextArea();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asiganar técnico");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Asignar técnico ");

        txtAveriaId.setEditable(false);
        txtAveriaId.setBackground(new java.awt.Color(237, 243, 251));
        txtAveriaId.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtAveriaId.setForeground(new java.awt.Color(67, 113, 177));
        txtAveriaId.setText("id");
        txtAveriaId.setEnabled(false);
        txtAveriaId.setPreferredSize(new java.awt.Dimension(64, 30));
        txtAveriaId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAveriaIdActionPerformed(evt);
            }
        });

        txtTipoAveria.setEditable(false);
        txtTipoAveria.setBackground(new java.awt.Color(237, 243, 251));
        txtTipoAveria.setForeground(new java.awt.Color(67, 113, 177));

        txtNombreMaquina.setEditable(false);
        txtNombreMaquina.setBackground(new java.awt.Color(237, 243, 251));
        txtNombreMaquina.setForeground(new java.awt.Color(67, 113, 177));

        jScrollPane15.setViewportView(listaTecnicos);

        txtTecnicoBuscar.setBackground(new java.awt.Color(237, 243, 251));
        txtTecnicoBuscar.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 12)); // NOI18N
        txtTecnicoBuscar.setForeground(new java.awt.Color(67, 113, 177));
        txtTecnicoBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTecnicoBuscarActionPerformed(evt);
            }
        });

        btnGuadarTecnico.setBackground(new java.awt.Color(58, 181, 235));
        btnGuadarTecnico.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnGuadarTecnico.setForeground(new java.awt.Color(255, 255, 255));
        btnGuadarTecnico.setText("Asiganar");
        btnGuadarTecnico.setBorderPainted(false);
        btnGuadarTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuadarTecnicoActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtMotivosTecnico.setColumns(20);
        txtMotivosTecnico.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        txtMotivosTecnico.setForeground(new java.awt.Color(67, 113, 177));
        txtMotivosTecnico.setRows(5);
        jScrollPane1.setViewportView(txtMotivosTecnico);

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
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuadarTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(txtNombreMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTipoAveria, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane15)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 124, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAveriaId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(txtTipoAveria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(txtTecnicoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuadarTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
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

    private void txtAveriaIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAveriaIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAveriaIdActionPerformed

    private void txtTecnicoBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTecnicoBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTecnicoBuscarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar la operación? Los cambios no se guardarán.", "Cancelar operación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuadarTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuadarTecnicoActionPerformed
        try {

            Usuario tecnicoSeleccionado = listaTecnicos.getSelectedValue();

            if (tecnicoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes seleccionar un tecnico.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Deseas asignar este tecnico a la averia?",
                    "Confirmar asignacion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            Maquinaria maquinaSeleccionada = new Maquinaria();
            maquinaSeleccionada.setCodigoMaquinaria(averiaSeleccionada.getMaquinariaFK());

            Usuario usuarioReporta = new Usuario();
            usuarioReporta.setCodigoUsuario(averiaSeleccionada.getUsuarioReportaFK());

            TipoAveria tipoSeleccionado = new TipoAveria();
            tipoSeleccionado.setCodigoTipoAveria(averiaSeleccionada.getTipoAveriaFK());

            LocalDateTime fechaAsignacion = averiaSeleccionada.getFechaAsigTecnico();

            if (fechaAsignacion == null) {
                fechaAsignacion = LocalDateTime.now();
            }

            boolean exito = controlador.actualizarAveria(
                    averiaSeleccionada.getCodigoAveria(),
                    averiaSeleccionada.getDescInicAveria(),
                    averiaSeleccionada.getProcRealizadoTecnico(),
                    maquinaSeleccionada,
                    usuarioReporta,
                    tecnicoSeleccionado,
                    tipoSeleccionado,
                    averiaSeleccionada.getFechaInicioAver(),
                    fechaAsignacion,
                    averiaSeleccionada.getFechaAcepTecnico(),
                    averiaSeleccionada.getFechaFinalizTecnico()
            );

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Tecnico asignado correctamente.",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo asignar el tecnico.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al asignar tecnico", e);
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al asignar tecnico.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuadarTecnicoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuadarTecnico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<Usuario> listaTecnicos;
    private javax.swing.JTextField txtAveriaId;
    private javax.swing.JTextArea txtMotivosTecnico;
    private javax.swing.JTextField txtNombreMaquina;
    private javax.swing.JTextField txtTecnicoBuscar;
    private javax.swing.JTextField txtTipoAveria;
    // End of variables declaration//GEN-END:variables
}
