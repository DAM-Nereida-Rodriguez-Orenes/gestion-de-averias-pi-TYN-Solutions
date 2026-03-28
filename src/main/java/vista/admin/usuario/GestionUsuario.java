/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.admin.usuario;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controlador.GestionRolControlador;
import controlador.GestionUsuarioControlador;
import controlador.LoginControlador;
import modelo.Rol;
import modelo.Usuario;
import utils.PanelImgFondo;
import vista.admin.averia.GestionAveriaListar;
import vista.admin.averia.GestionTipoAveria;
import vista.admin.maquinas.GestionEstadoMaquina;
import vista.admin.maquinas.GestionMaquinas;
import vista.admin.maquinas.GestionTipoMaquina;
import vista.oper.usuario.GestionUsuarioPerfilOper;
import vista.vHomeAdmin;
import vista.vLogin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Thanya
 */
public class GestionUsuario extends javax.swing.JFrame {

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

        URL urlLogo = getClass().getClassLoader().getResource("recursos/logos/fixora_logo_140x70.svg");
        System.out.println("urlLogo = " + urlLogo);

        FlatSVGIcon iconop = new FlatSVGIcon("recursos/logos/fixora_logo_140x70.svg", 70, 34);
        jlLogo.setIcon(iconop);
        jlLogo.setText("");
        jlLogo.setOpaque(false);

        //icono de usuario 
        FlatSVGIcon iconUsuarioAdmin = new FlatSVGIcon("recursos/iconos/user_icon_exact.svg", 24, 24);
        jLabel2.setIcon(iconUsuarioAdmin);
        jLabel2.setText("Hola, " + gestionUsuarioControlador.obtenerNombreUsuarioLogueado());
        jLabel2.setHorizontalTextPosition(SwingConstants.LEFT);
        jLabel2.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel2.setIconTextGap(8);

        // Placeholder real de FlatLaf
        txtBarraBusqueda.putClientProperty("JTextField.placeholderText", "Buscar ");
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
            // este valor aumenta el tamaño de las tuplas
            tbUsuarios.setRowHeight(36);
            //esto aumneta el tamaño de la fuente de la tabla y cambia la fuente
            tbUsuarios.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
            // esto aumenta el tamaño de la fuente del header
            tbUsuarios.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
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
        configurarOrdenacionTabla();

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

    private void configurarOrdenacionTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tbUsuarios.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);

        // Desactivar el orden en columnas no deseadas
        sorter.setSortable(0, false); // ID
        sorter.setSortable(3, false); // Telefono
        sorter.setSortable(4, false); // Email
        sorter.setSortable(5, false); // Rol

        // Estatus con orden personalizado
        sorter.setComparator(6, (valor1, valor2) -> {
            String estado1 = valor1.toString();
            String estado2 = valor2.toString();

            int orden1 = estado1.equalsIgnoreCase("Activo") ? 0 : 1;
            int orden2 = estado2.equalsIgnoreCase("Activo") ? 0 : 1;

            return Integer.compare(orden1, orden2);
        });
        
        tbUsuarios.setRowSorter(sorter);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFondo = new PanelImgFondo("/recursos/fondoFormularios.png");
        panelCabecera = new PanelImgFondo("/recursos/fondoFormularios2.png");
        jlLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnEliminarUsuario = new javax.swing.JButton();
        btnActualizarUsuario = new javax.swing.JButton();
        panelFiltros = new javax.swing.JPanel();
        btnAddUsuario = new javax.swing.JButton();
        btnLimpiarFiltros = new javax.swing.JButton();
        btnAplicarFiltros = new javax.swing.JButton();
        cbbEstatus = new javax.swing.JComboBox<>();
        cbbRol = new javax.swing.JComboBox<>();
        txtBarraBusqueda = new javax.swing.JTextField();
        panelTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        panelTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        miInicio = new javax.swing.JMenu();
        miMenuPrincipal = new javax.swing.JMenuItem();
        miPerfil = new javax.swing.JMenuItem();
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
        setTitle("Gestión de Usuarios");

        panelFondo.setBackground(new java.awt.Color(204, 204, 204));
        panelFondo.setPreferredSize(new java.awt.Dimension(1200, 800));

        panelCabecera.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        panelCabecera.setPreferredSize(new java.awt.Dimension(397, 50));

        jlLogo.setText("jLabel2");

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(67, 113, 177));
        jLabel2.setText("Hola, Admin");

        javax.swing.GroupLayout panelCabeceraLayout = new javax.swing.GroupLayout(panelCabecera);
        panelCabecera.setLayout(panelCabeceraLayout);
        panelCabeceraLayout.setHorizontalGroup(
            panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCabeceraLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );
        panelCabeceraLayout.setVerticalGroup(
            panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCabeceraLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(panelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlLogo)
                    .addComponent(jLabel2))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        panelAcciones.setBackground(new java.awt.Color(153, 153, 153));
        panelAcciones.setOpaque(false);

        btnEliminarUsuario.setBackground(new java.awt.Color(204, 0, 0));
        btnEliminarUsuario.setFont(new java.awt.Font("Microsoft JhengHei", 1, 14)); // NOI18N
        btnEliminarUsuario.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarUsuario.setText("Eliminar usuario");
        btnEliminarUsuario.setBorderPainted(false);
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccionesLayout.createSequentialGroup()
                .addComponent(btnEliminarUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 788, Short.MAX_VALUE)
                .addComponent(btnActualizarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActualizarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        panelFiltros.setBackground(new java.awt.Color(153, 153, 153));
        panelFiltros.setOpaque(false);

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

        cbbEstatus.setBackground(new java.awt.Color(234, 242, 251));
        cbbEstatus.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        cbbEstatus.setForeground(new java.awt.Color(67, 113, 177));
        cbbEstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Estatus", "Activo", "Inactivo" }));
        cbbEstatus.setBorder(null);
        cbbEstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbEstatusActionPerformed(evt);
            }
        });

        cbbRol.setBackground(new java.awt.Color(234, 242, 251));
        cbbRol.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        cbbRol.setForeground(new java.awt.Color(67, 113, 177));
        cbbRol.setBorder(null);
        cbbRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbRolActionPerformed(evt);
            }
        });

        txtBarraBusqueda.setBackground(new java.awt.Color(234, 242, 251));
        txtBarraBusqueda.setFont(new java.awt.Font("Microsoft JhengHei Light", 0, 14)); // NOI18N
        txtBarraBusqueda.setForeground(new java.awt.Color(67, 113, 177));
        txtBarraBusqueda.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(234, 242, 251)));
        txtBarraBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarraBusquedaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFiltrosLayout = new javax.swing.GroupLayout(panelFiltros);
        panelFiltros.setLayout(panelFiltrosLayout);
        panelFiltrosLayout.setHorizontalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtBarraBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbbEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAplicarFiltros)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiarFiltros)
                .addGap(31, 31, 31)
                .addComponent(btnAddUsuario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFiltrosLayout.setVerticalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtBarraBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(cbbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(cbbEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(btnAplicarFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(btnLimpiarFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        panelTabla.setBackground(new java.awt.Color(153, 153, 153));
        panelTabla.setOpaque(false);

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

        javax.swing.GroupLayout panelTablaLayout = new javax.swing.GroupLayout(panelTabla);
        panelTabla.setLayout(panelTablaLayout);
        panelTablaLayout.setHorizontalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTablaLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );
        panelTablaLayout.setVerticalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTitulo.setBackground(new java.awt.Color(153, 153, 153));
        panelTitulo.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestión de Usuarios");

        javax.swing.GroupLayout panelTituloLayout = new javax.swing.GroupLayout(panelTitulo);
        panelTitulo.setLayout(panelTituloLayout);
        panelTituloLayout.setHorizontalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelTituloLayout.setVerticalGroup(
            panelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFondoLayout = new javax.swing.GroupLayout(panelFondo);
        panelFondo.setLayout(panelFondoLayout);
        panelFondoLayout.setHorizontalGroup(
            panelFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(panelFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFondoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFondoLayout.setVerticalGroup(
            panelFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFondoLayout.createSequentialGroup()
                .addComponent(panelCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(panelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        miInicio.setText("Inicio");

        miMenuPrincipal.setText("Menú principal");
        miMenuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMenuPrincipalActionPerformed(evt);
            }
        });
        miInicio.add(miMenuPrincipal);

        miPerfil.setText("Perfil");
        miPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miPerfilActionPerformed(evt);
            }
        });
        miInicio.add(miPerfil);

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

        miGestion.setText("Gestiones");

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
        miEstadoMaquinaria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEstadoMaquinariaActionPerformed(evt);
            }
        });
        miGestion.add(miEstadoMaquinaria);

        miTipoAveria.setText("Tipos de avería");
        miTipoAveria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTipoAveriaActionPerformed(evt);
            }
        });
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
            .addComponent(panelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBarraBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarraBusquedaActionPerformed

    }//GEN-LAST:event_txtBarraBusquedaActionPerformed

    private void btnAddUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUsuarioActionPerformed
        CrearUsuario cu = new CrearUsuario(this, rootPaneCheckingEnabled, gestionUsuarioControlador, this);
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
        GestionAveriaListar gestionAveria = new GestionAveriaListar();
        gestionAveria.setLocationRelativeTo(null);
        gestionAveria.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miAveriaActionPerformed

    private void miUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miUsuarioActionPerformed
        GestionUsuarioControlador gestionUsuarioControlador = new GestionUsuarioControlador();
        GestionUsuario gestionUsuario = new GestionUsuario(gestionUsuarioControlador);
        gestionUsuario.setLocationRelativeTo(null);
        gestionUsuario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miUsuarioActionPerformed

    private void miTipoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoMaquinariaActionPerformed
        GestionTipoMaquina gestionTipoMaquina = new GestionTipoMaquina();
        gestionTipoMaquina.setLocationRelativeTo(null);
        gestionTipoMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miTipoMaquinariaActionPerformed

    private void miCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCerrarSesionActionPerformed
        LoginControlador loginControlador = new LoginControlador();
        vLogin login = new vLogin(loginControlador);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        this.dispose();
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

    private void miTipoAveriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miTipoAveriaActionPerformed
        GestionTipoAveria gestionTipoAveria = new GestionTipoAveria();
        gestionTipoAveria.setLocationRelativeTo(null);
        gestionTipoAveria.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miTipoAveriaActionPerformed

    private void miEstadoMaquinariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstadoMaquinariaActionPerformed
        GestionEstadoMaquina gestionEstadoMaquina = new GestionEstadoMaquina();
        gestionEstadoMaquina.setLocationRelativeTo(null);
        gestionEstadoMaquina.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miEstadoMaquinariaActionPerformed

    private void miPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miPerfilActionPerformed
        GestionUsuarioPerfilOper gestionUsuarioPerfil = new GestionUsuarioPerfilOper();
        gestionUsuarioPerfil.setLocationRelativeTo(null);
        gestionUsuarioPerfil.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_miPerfilActionPerformed


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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlLogo;
    private javax.swing.JMenuItem miAveria;
    private javax.swing.JMenuItem miCerrarSesion;
    private javax.swing.JMenuItem miEstadoMaquinaria;
    private javax.swing.JMenu miGestion;
    private javax.swing.JMenu miInicio;
    private javax.swing.JMenuItem miMaquinaria;
    private javax.swing.JMenuItem miMenuPrincipal;
    private javax.swing.JMenuItem miPerfil;
    private javax.swing.JMenuItem miRoles;
    private javax.swing.JMenuItem miSalirApp;
    private javax.swing.JMenuItem miTipoAveria;
    private javax.swing.JMenuItem miTipoMaquinaria;
    private javax.swing.JMenuItem miUsuario;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel panelFondo;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtBarraBusqueda;
    // End of variables declaration//GEN-END:variables
}
