/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.admin.usuario;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.StringUtils;
import config.DataSourceFactory;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.sql.DataSource;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import modelo.Rol;
import modelo.Usuario;
import vista.PanelImgFondo;
import vista.admin.maquinas.GestionMaquinas;
import vista.vHomeAdmin;

/**
 *
 * @author Netri
 */
public class GestionUsuario extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GestionUsuario.class.getName());
    private final GestionUsuarioControlador gestionUsuarioControlador;
    private final List<Rol> listaRoles;

    /**
     * Creates new form GestionUsuario
     */
    public GestionUsuario(GestionUsuarioControlador gestionUsuarioControlador) {
        initComponents();
        this.gestionUsuarioControlador = gestionUsuarioControlador;
        mostrarTabla(null);
        mostrarImagenes();

        /**
         * Hemos recuperado los roles de la base de datos y relleno el cbb con
         * esos valores. Luego los obtengo como string para poder pasarselos a
         * mi metopdo gestionUsuarioControlador.crearUsuario(nombre, apellido,
         * rol, telefono, email, password);
         */
        listaRoles = gestionUsuarioControlador.recuperarListadoRoles();
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        for (int i = 0; i < listaRoles.size(); i++) {
            modelo.addElement(listaRoles.get(i).getDescripcionRol());
        }
        cbbRol.setModel(modelo);
    }

    public void mostrarImagenes() {
        //Ajustes del deisño del JFrame
        Image icono = new ImageIcon(getClass().getResource("/recursos/isotipo.png")).getImage();
        this.setIconImage(icono);
        setLocationRelativeTo(null);

        URL urlLogo = getClass().getClassLoader().getResource("recursos/logos/fixora_logo_140x70.svg");
        System.out.println("urlLogo = " + urlLogo);

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 140, 70);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);
        
         //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 32, 32);
        jLabel2.setIcon(iconUsuarioAdmin);
        jLabel2.setText("Hola, Admin");
        jLabel2.setHorizontalTextPosition(SwingConstants.LEFT);
        jLabel2.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel2.setIconTextGap(8);
    }

    //Metodos auxiliares
    public void mostrarTabla(List<Usuario> listaUsuarios) {
        String[] columnas = {"ID", "Nombre", "Apellido", "Rol", "Telefono", "Email", "Estatus"};

        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            /**
             * Sobrescribimos isCellEditable para que el usuario no pueda
             * modificar manualmente las celdas de la tabla.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (listaUsuarios == null) {
            listaUsuarios = this.gestionUsuarioControlador.recuperarUsuarios();
            tbUsuarios.setRowHeight(36); // este valor aumenta el tamaño de las tuplas
            tbUsuarios.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); //esto aumneta el tamaño de la fuente de la tabla
            tbUsuarios.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); // esto aumenta el tamaño de la fuente del header
        }

        Object[] fila = new Object[7];
        for (Usuario usuario : listaUsuarios) {
            fila[0] = usuario.getCodigoUsuario();
            fila[1] = usuario.getNombre();
            fila[2] = usuario.getApellido();
            fila[3] = (usuario.getRol() != null && usuario.getRol().getDescripcionRol() != null) ? usuario.getRol().getDescripcionRol() : "";
            fila[4] = usuario.getTelefono();
            fila[5] = usuario.getEmail();
            fila[6] = usuario.isActivo() == true ? "Activo" : "Inactivo";

            modelo.addRow(fila);
        }
        tbUsuarios.setModel(modelo);

    }

    /**
     * este metodo lee lo escrito en la barra de busqueda permite buscar por
     * nombre solo o por nombre + apellido (uno o mas apellidos) la primera
     * palabra se usa como nombre y el resto como apellido
     */
    private List<Usuario> filtrarPorBarraBusqueda() {

        String texto = txtBarraBusqueda.getText();

        if (texto == null) {
            texto = "";
        }

        texto = texto.trim();
        texto = texto.replaceAll("\\s+", " ");

        if (texto.isEmpty()) {
            return gestionUsuarioControlador.buscarUsuario(null, null, null, null, null, null);
            // o recuperarUsuarios() si lo tienes
        }

        String[] partes = texto.split(" ");

        if (partes.length == 1) {
            String filtro = partes[0] + "%"; // Solo los que empiezan por esa letra
            return gestionUsuarioControlador.buscarPorTexto(filtro);
        }

        String nombre = partes[0] + "%";
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < partes.length; i++) {
            sb.append(partes[i]);

            if (i < partes.length - 1) {
                sb.append(" ");
            }
        }

        String apellido = sb.toString();
        return gestionUsuarioControlador.buscarUsuario(null, nombre, apellido, null, null, null);
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
        txtBarraBusqueda = new javax.swing.JTextField();
        cbbRol = new javax.swing.JComboBox<>();
        cbbEstatus = new javax.swing.JComboBox<>();
        btnAddUsuario = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnActualizarUsuario = new javax.swing.JButton();
        btnEliminarUsuario = new javax.swing.JButton();
        btnAplicarFiltros = new javax.swing.JButton();
        btnLimpiarFiltros = new javax.swing.JButton();
        jpCabecera = new PanelImgFondo("/recursos/fondoFormularios2.png");
        jlLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        miInicio = new javax.swing.JMenu();
        miMenuPrincipal = new javax.swing.JMenuItem();
        miCerrarSesion = new javax.swing.JMenuItem();
        miSalirApp = new javax.swing.JMenuItem();
        miGestion = new javax.swing.JMenu();
        miAveria = new javax.swing.JMenuItem();
        miUsuario = new javax.swing.JMenuItem();
        miMaquinaria = new javax.swing.JMenuItem();
        miTipoMaquinaria = new javax.swing.JMenuItem();
        miEstadoMaquinaria = new javax.swing.JMenuItem();
        miTipoAveria = new javax.swing.JMenuItem();
        miRoles = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de Usuario");

        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 800));

        txtBarraBusqueda.setBackground(new java.awt.Color(234, 242, 251));
        txtBarraBusqueda.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        txtBarraBusqueda.setForeground(new java.awt.Color(67, 113, 177));
        txtBarraBusqueda.setText("Buscar");
        txtBarraBusqueda.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(234, 242, 251)));
        txtBarraBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarraBusquedaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBarraBusquedaFocusLost(evt);
            }
        });
        txtBarraBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarraBusquedaActionPerformed(evt);
            }
        });

        cbbRol.setBackground(new java.awt.Color(234, 242, 251));
        cbbRol.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        cbbRol.setForeground(new java.awt.Color(67, 113, 177));
        cbbRol.setBorder(null);
        cbbRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbRolActionPerformed(evt);
            }
        });

        cbbEstatus.setBackground(new java.awt.Color(234, 242, 251));
        cbbEstatus.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        cbbEstatus.setForeground(new java.awt.Color(67, 113, 177));
        cbbEstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estatus", "Activo", "Inactivo" }));
        cbbEstatus.setBorder(null);
        cbbEstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbEstatusActionPerformed(evt);
            }
        });

        btnAddUsuario.setBackground(new java.awt.Color(58, 181, 235));
        btnAddUsuario.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnAddUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnAddUsuario.setText(" + Nuevo Usuario");
        btnAddUsuario.setBorderPainted(false);
        btnAddUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUsuarioActionPerformed(evt);
            }
        });

        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbUsuarios);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setText("Gestión de Usuario");

        btnActualizarUsuario.setBackground(new java.awt.Color(234, 242, 251));
        btnActualizarUsuario.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnActualizarUsuario.setForeground(new java.awt.Color(67, 113, 177));
        btnActualizarUsuario.setText("Editar");
        btnActualizarUsuario.setBorderPainted(false);
        btnActualizarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarUsuarioActionPerformed(evt);
            }
        });

        btnEliminarUsuario.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminarUsuario.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnEliminarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarUsuario.setText("Eliminar");
        btnEliminarUsuario.setBorderPainted(false);
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });

        btnAplicarFiltros.setBackground(new java.awt.Color(234, 242, 251));
        btnAplicarFiltros.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnAplicarFiltros.setForeground(new java.awt.Color(67, 113, 177));
        btnAplicarFiltros.setText("Aplicar filtros");
        btnAplicarFiltros.setBorderPainted(false);
        btnAplicarFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarFiltrosActionPerformed(evt);
            }
        });

        btnLimpiarFiltros.setBackground(new java.awt.Color(234, 242, 251));
        btnLimpiarFiltros.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnLimpiarFiltros.setForeground(new java.awt.Color(67, 113, 177));
        btnLimpiarFiltros.setText("Limpiar filtros");
        btnLimpiarFiltros.setBorderPainted(false);
        btnLimpiarFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFiltrosActionPerformed(evt);
            }
        });

        jpCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        jlLogo.setText("jLabel2");

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(67, 113, 177));
        jLabel2.setText("Hola, Admin");

        javax.swing.GroupLayout jpCabeceraLayout = new javax.swing.GroupLayout(jpCabecera);
        jpCabecera.setLayout(jpCabeceraLayout);
        jpCabeceraLayout.setHorizontalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(86, 86, 86))
        );
        jpCabeceraLayout.setVerticalGroup(
            jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlLogo)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnActualizarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnEliminarUsuario))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(txtBarraBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cbbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cbbEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnAplicarFiltros)
                            .addGap(18, 18, 18)
                            .addComponent(btnLimpiarFiltros)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddUsuario))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(67, 67, 67))
            .addComponent(jpCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(440, 440, 440))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jpCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbRol)
                    .addComponent(txtBarraBusqueda)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbbEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAplicarFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLimpiarFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        miInicio.setText("Inicio");

        miMenuPrincipal.setText("Menú principal");
        miMenuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMenuPrincipalActionPerformed(evt);
            }
        });
        miInicio.add(miMenuPrincipal);

        miCerrarSesion.setText("Cerrar sesión");
        miCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCerrarSesionActionPerformed(evt);
            }
        });
        miInicio.add(miCerrarSesion);

        miSalirApp.setText("Cerrar Fixora");
        miInicio.add(miSalirApp);

        jMenuBar1.add(miInicio);

        miGestion.setText("Gestión");

        miAveria.setText("Avería");
        miAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAveriaActionPerformed(evt);
            }
        });
        miGestion.add(miAveria);

        miUsuario.setText("Usuario");
        miUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miUsuarioActionPerformed(evt);
            }
        });
        miGestion.add(miUsuario);

        miMaquinaria.setText("Maquinaria");
        miMaquinaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMaquinariaActionPerformed(evt);
            }
        });
        miGestion.add(miMaquinaria);

        miTipoMaquinaria.setText("Tipo de maquinaria");
        miTipoMaquinaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTipoMaquinariaActionPerformed(evt);
            }
        });
        miGestion.add(miTipoMaquinaria);

        miEstadoMaquinaria.setText("Estado de maquinaria");
        miGestion.add(miEstadoMaquinaria);

        miTipoAveria.setText("Tipos de avería");
        miGestion.add(miTipoAveria);

        miRoles.setText("Roles");
        miRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRolesActionPerformed(evt);
            }
        });
        miGestion.add(miRoles);

        jMenuBar1.add(miGestion);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBarraBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarraBusquedaActionPerformed

    }//GEN-LAST:event_txtBarraBusquedaActionPerformed

    private void btnAddUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUsuarioActionPerformed
        CrearUsuario cu = new CrearUsuario(this, rootPaneCheckingEnabled, gestionUsuarioControlador, this);
        cu.setSize(1000, 600);
        cu.setLocationRelativeTo(this);
        cu.setVisible(true);
    }//GEN-LAST:event_btnAddUsuarioActionPerformed

    private void btnActualizarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarUsuarioActionPerformed
        List<Usuario> usuarioFiltrado;
        int filaSelecionada = tbUsuarios.getSelectedRow();
        if (filaSelecionada != -1) {

            //filtramos por el email ya que tiene constraint unica
            String emailUsuario = (String) tbUsuarios.getValueAt(filaSelecionada, 5);
            //llamamos al metodo para que me filtre el email y me devulva el codigo del usuario
            usuarioFiltrado = gestionUsuarioControlador.buscarUsuario(null, null, null, null, emailUsuario, null);
            gestionUsuarioControlador.setUsuario(usuarioFiltrado.get(0));

            ActualizarUsuario au = new ActualizarUsuario(this, rootPaneCheckingEnabled, gestionUsuarioControlador, this);
            au.setSize(1000, 600);
            au.setLocationRelativeTo(this);
            au.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Debes selecionar un usuario", "Actualizar usuario", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnActualizarUsuarioActionPerformed

    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        int filaSelecionada = tbUsuarios.getSelectedRow();
        //Nos aseguramos de que el usuario este dado de alta o de baja
        String usuarioActivo = (String) tbUsuarios.getValueAt(filaSelecionada, 6);
        //Nos aseguramos de selecionar la fila, si se seleciona continuamos con el proceso
        if (filaSelecionada != -1 && usuarioActivo.equals("Activo")) {
            //filtramos por el email ya que tiene constraint unica
            String emailUsuario = (String) tbUsuarios.getValueAt(filaSelecionada, 5);
            //llamamos al metodo paraq ue me filtre el email y me devulva el codigo del usuario
            List<Usuario> usuarioFiltrado = gestionUsuarioControlador.buscarUsuario(null, null, null, null, emailUsuario, null);

            //Lanzamos mensaje de confirmacion
            int opcion;
            opcion = JOptionPane.showConfirmDialog(this, "¿Estas seguro de que quieres dar de baja a este usuario?",
                    "Eliminar usuario", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "El usuario se ha dado de baja con exito",
                        "Eliminar usuario", JOptionPane.INFORMATION_MESSAGE);
                gestionUsuarioControlador.eliminarUsuario(usuarioFiltrado.get(0).getCodigoUsuario());
            } else if (opcion == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Proceso cancelado",
                        "Eliminar usuario", JOptionPane.INFORMATION_MESSAGE);
            }
            //si no se ha selecionado nigun usuario de la tabla nos
        } else if (filaSelecionada != -1 && usuarioActivo.equals("Inactivo")) {
            JOptionPane.showMessageDialog(this, "Este usuario ya esta dado de baja",
                    "Eliminar usuario", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se ha seleccionado un usuario", "Eliminar usuario", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    /**
     * OPCIONES DEL MENU.
     */
    private void miAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAveriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miAveriaActionPerformed

    private void miUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miUsuarioActionPerformed
        GestionUsuarioControlador gestionUsuarioControlador = new GestionUsuarioControlador();
        GestionUsuario gestionUsuario = new GestionUsuario(gestionUsuarioControlador);
        gestionUsuario.setLocationRelativeTo(null);
        gestionUsuario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miUsuarioActionPerformed

    private void miTipoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoMaquinariaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miTipoMaquinariaActionPerformed

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miCerrarSesionActionPerformed

    private void miMenuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMenuPrincipalActionPerformed
        vHomeAdmin homeAdmin = new vHomeAdmin();
        homeAdmin.setLocationRelativeTo(null);
        homeAdmin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMenuPrincipalActionPerformed

    private void btnAplicarFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarFiltrosActionPerformed
        List<Usuario> listaUsuario;

        // 1) filtro estatus -> lo convertimos a Boolean
        int selectedItemEstatus = cbbEstatus.getSelectedIndex();
        String estatusSeleccionado = cbbEstatus.getItemAt(selectedItemEstatus);

        Boolean activo = null;

        if (estatusSeleccionado != null) {

            if (estatusSeleccionado.equalsIgnoreCase("Activo")) {
                activo = true;
            } else if (estatusSeleccionado.equalsIgnoreCase("Inactivo")) {
                activo = false;
            } else {
                activo = null; // para "Todos"
            }
        }

        // 2) filtro rol -> buscamos el objeto rol en listaRoles
        int selectedItemRol = cbbRol.getSelectedIndex();
        String rolSeleccionado = cbbRol.getItemAt(selectedItemRol);

        Rol rol = null;

        if (rolSeleccionado != null && !rolSeleccionado.equalsIgnoreCase("Trabajador")) {

            for (int i = 0; i < listaRoles.size(); i++) {

                Rol rolAux = listaRoles.get(i);

                if (rolAux.getDescripcionRol() != null && rolAux.getDescripcionRol().equalsIgnoreCase(rolSeleccionado)) {
                    rol = rolAux;
                    break;
                }
            }
        }

        // 3) filtro barra busqueda -> nombre y apellido
        String texto = txtBarraBusqueda.getText();

        if (texto == null) {
            texto = "";
        }

        texto = texto.trim();
        texto = texto.replaceAll("\\s+", " ");

        String nombre = null;
        String apellido = null;

        if (!texto.isEmpty()) {

            String[] partes = texto.split(" ");

            nombre = partes[0];

            if (partes.length > 1) {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i < partes.length; i++) {

                    sb.append(partes[i]);

                    if (i < partes.length - 1) {
                        sb.append(" ");
                    }
                }

                apellido = sb.toString();
            }
        }

        // 4) una sola llamada con los 3 filtros juntos
        listaUsuario = gestionUsuarioControlador.buscarUsuario(null, nombre, apellido, rol, null, activo);

        mostrarTabla(listaUsuario);

    }//GEN-LAST:event_btnAplicarFiltrosActionPerformed

    private void btnLimpiarFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFiltrosActionPerformed

        // reiniciamos combos
        cbbEstatus.setSelectedIndex(0);
        cbbRol.setSelectedIndex(0);

        // limpiamos barra de busqueda
        txtBarraBusqueda.setText("");

        // recargamos todos los usuarios
        mostrarTabla(null);
    }//GEN-LAST:event_btnLimpiarFiltrosActionPerformed

    /**
     * FILTOS DE BUSQUEDA.
     */
    private void cbbEstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbEstatusActionPerformed

    }//GEN-LAST:event_cbbEstatusActionPerformed

    private void cbbRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbRolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbRolActionPerformed

    // Metodo que se ejecuta cuando el usuario entra en la barra de busqueda
    // Si el texto actual es "Buscar", lo borra para que el usuario pueda escribir
    private void txtBarraBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarraBusquedaFocusGained
        String texto = txtBarraBusqueda.getText().trim();

        if (texto.equalsIgnoreCase("Buscar")) {
            txtBarraBusqueda.setText("");
        }
    }//GEN-LAST:event_txtBarraBusquedaFocusGained

    // Metodo que se ejecuta cuando el usuario sale de la barra de busqueda
    // Si el usuario no ha escrito nada, vuelve a colocar el texto "Buscar"
    private void txtBarraBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarraBusquedaFocusLost
        String texto = txtBarraBusqueda.getText().trim();

        if (texto.isEmpty()) {
            txtBarraBusqueda.setText("Buscar");
        }
    }//GEN-LAST:event_txtBarraBusquedaFocusLost

    private void miRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRolesActionPerformed
        GestionRolControlador gestionRolControlador = new GestionRolControlador();
        GestionRol gestionRol = new GestionRol(gestionRolControlador);
        gestionRol.setLocationRelativeTo(null);
        gestionRol.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miRolesActionPerformed

    private void miMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMaquinariaActionPerformed
        GestionMaquinas gestionMaquina = new GestionMaquinas();
        gestionMaquina.setLocationRelativeTo(null);
        gestionMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miMaquinariaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarUsuario;
    private javax.swing.JButton btnAddUsuario;
    private javax.swing.JButton btnAplicarFiltros;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnLimpiarFiltros;
    private javax.swing.JComboBox<String> cbbEstatus;
    private javax.swing.JComboBox<String> cbbRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JPanel jpCabecera;
    private javax.swing.JMenuItem miAveria;
    private javax.swing.JMenuItem miCerrarSesion;
    private javax.swing.JMenuItem miEstadoMaquinaria;
    private javax.swing.JMenu miGestion;
    private javax.swing.JMenu miInicio;
    private javax.swing.JMenuItem miMaquinaria;
    private javax.swing.JMenuItem miMenuPrincipal;
    private javax.swing.JMenuItem miRoles;
    private javax.swing.JMenuItem miSalirApp;
    private javax.swing.JMenuItem miTipoAveria;
    private javax.swing.JMenuItem miTipoMaquinaria;
    private javax.swing.JMenuItem miUsuario;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtBarraBusqueda;
    // End of variables declaration//GEN-END:variables
}
