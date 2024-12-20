/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.views.operador;

import com.mycompany.dao.DaoExportData;
import com.mycompany.dao.Sucursal;
import com.mycompany.dao.WSManager;
import com.mycompany.domain.EstadoPedido;
import com.mycompany.domain.Operador;
import com.mycompany.domain.Pedido;
import com.mycompany.domain.Repartidor;
import com.mycompany.domain.Zona;
import com.mycompany.vistas.Login;
import com.mycompany.vistas.PanelImageRedondeado;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.Box;

public class HomeOperador extends javax.swing.JFrame {

    private Operador operador;
    private ArrayList<Zona> zonas;
    private ArrayList<Repartidor> repartidores;
    private ArrayList<Pedido> pedidos;

    /**
     * Creates new form HomeOperador
     */
    public HomeOperador() {
        initComponents();

        this.setLocationRelativeTo(null);
    }

    public HomeOperador(Operador operador) {
        initComponents();
        this.operador = operador;
        this.setLocationRelativeTo(null);

        this.containerListZonas.setLayout(new BoxLayout(this.containerListZonas, BoxLayout.Y_AXIS));
        this.containerListRepartidores.setLayout(new BoxLayout(this.containerListRepartidores, BoxLayout.Y_AXIS));
        this.containerListPedidos.setLayout(new BoxLayout(this.containerListPedidos, BoxLayout.Y_AXIS));

        initData();
    }

    private void initData() {
        this.labelUsername.setText(this.operador.getNombre_completo());

        loadZonas();
        loadRepartidores();
        loadPedidos();
    }

    public void loadZonas() {
        // Cargar las zonas desde el WS
        this.zonas = (ArrayList<Zona>) WSManager.getZonas();
        this.containerListZonas.removeAll();

        for (Zona zona : zonas) {
            PanelZona panelZona = new PanelZona(zona);

            // Agregar el panel sin fijar tamaños manualmente
            this.containerListZonas.add(panelZona);
            this.containerListZonas.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
        }


        // Actualizar la interfaz
        this.containerListZonas.revalidate();
        this.containerListZonas.repaint();
    }
    

    public void loadRepartidores() {

        this.containerListRepartidores.removeAll();
        // Set repas
        this.repartidores = (ArrayList<Repartidor>) WSManager.getRepartidores();

        for (Repartidor repa : repartidores) {
            PanelRepartidor panelRepa = new PanelRepartidor(repa);

            this.containerListRepartidores.add(panelRepa);
            this.containerListRepartidores.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        this.containerListRepartidores.revalidate();
        this.containerListRepartidores.repaint();
    }

    public void loadPedidos() {
        this.containerListPedidos.removeAll();

        // Set pedidos
        this.pedidos = (ArrayList<Pedido>) WSManager.getPedidos();
        
        verificarEntregas();
        

        for (Pedido pedido : pedidos) {
            PanelPedido panelPedido = new PanelPedido(pedido);

            this.containerListPedidos.add(panelPedido);
            this.containerListPedidos.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        this.containerListPedidos.revalidate();
        this.containerListPedidos.repaint();

    }

    private void verificarEntregas() {
        // Verificamos si no hay pedidos vencidos
        for (Pedido pedido : this.pedidos) {
            //System.out.println("DENTRO DEL CICLO");
            if (pedido.getEstado() == EstadoPedido.PENDIENTE && pedido.getFecha_entrega().before(new java.util.Date())) {
                //System.out.println("DENTRO DEL IF");
                // Obtenemos la zona del pedido para verificar la distancia con la sucursal
                ArrayList<Zona> zonas = (ArrayList<Zona>) WSManager.getZonas();
                Zona zonaPedido = zonas.stream().filter(z -> z.getIdZona() == pedido.getIdZona()).findFirst().orElse(null);

                if (zonaPedido == null) {
                    System.out.println("No se encontro la zona del pedido");
                    continue;
                }

                double distancia = zonaPedido.calcularDistancia(Sucursal.COORDENADAS_X, Sucursal.COORDENADAS_Y);
                
                //System.out.println("DISTANCIA CON LA SUCRUSAL: " + distancia);

                if (distancia <= 10) {
                    // El pedido está vencido y no se ha entregado
                    pedido.setPrioridad(true);
                    // Actualizamos la fecha de entrega a mañana
                    java.util.Date nuevaFecha = new java.util.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

                    pedido.setFecha_entrega(new java.sql.Date(nuevaFecha.getTime()));

                    // Actualizamos el pedido en el WS
                    int idPedido = WSManager.actualizarPedido(pedido);

                    if (idPedido == -1) {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el pedido");
                        continue;
                    }

                    JOptionPane.showMessageDialog(this, "El pedido " + pedido.getIdPedido() 
                            + " ha sido marcado como prioritario y se ha actualizado la fecha de entrega a mañana");

                    // Actualizamos el pedido en la lista de pedidos
                    Pedido pedidoActualizado = this.pedidos.stream().filter(p -> p.getIdPedido() == idPedido).findFirst().orElse(null);

                    if (pedidoActualizado != null) {
                        pedidoActualizado.setFecha_entrega(pedido.getFecha_entrega());
                        pedidoActualizado.setPrioridad(pedido.isPrioridad());
                    }
                    
                }
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

        menuExportar = new javax.swing.JPopupMenu();
        itemExcelZonas = new javax.swing.JMenuItem();
        itemRepasPedidos = new javax.swing.JMenuItem();
        containerHome = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnPedidos = new javax.swing.JButton();
        panelImageLogo7 = new PanelImageRedondeado();
        btnZonas = new javax.swing.JButton();
        bntRepartidores = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        labelUsername = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        containerPedidos = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        btnAddPedido = new javax.swing.JButton();
        scrollPedidos = new javax.swing.JScrollPane();
        containerListPedidos = new javax.swing.JPanel();
        containerZonas = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnAddZona = new javax.swing.JButton();
        scrollZonas = new javax.swing.JScrollPane();
        containerListZonas = new javax.swing.JPanel();
        containerRepartidores = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnAddRepa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        containerListRepartidores = new javax.swing.JPanel();

        itemExcelZonas.setText("Excel de zonas con mas pedidos");
        itemExcelZonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemExcelZonasActionPerformed(evt);
            }
        });
        menuExportar.add(itemExcelZonas);

        itemRepasPedidos.setText("PDF de Repartidores");
        itemRepasPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemRepasPedidosActionPerformed(evt);
            }
        });
        menuExportar.add(itemRepasPedidos);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        containerHome.setBackground(new java.awt.Color(255, 255, 255));
        containerHome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));

        btnPedidos.setBackground(new java.awt.Color(0, 153, 255));
        btnPedidos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPedidos.setForeground(new java.awt.Color(255, 255, 255));
        btnPedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon-pedidos.png"))); // NOI18N
        btnPedidos.setText("Pedidos");
        btnPedidos.setBorder(null);
        btnPedidos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPedidosActionPerformed(evt);
            }
        });

        panelImageLogo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.jpg"))); // NOI18N

        javax.swing.GroupLayout panelImageLogo7Layout = new javax.swing.GroupLayout(panelImageLogo7);
        panelImageLogo7.setLayout(panelImageLogo7Layout);
        panelImageLogo7Layout.setHorizontalGroup(
            panelImageLogo7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 83, Short.MAX_VALUE)
        );
        panelImageLogo7Layout.setVerticalGroup(
            panelImageLogo7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        btnZonas.setBackground(new java.awt.Color(0, 153, 255));
        btnZonas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnZonas.setForeground(new java.awt.Color(255, 255, 255));
        btnZonas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon-zonas.png"))); // NOI18N
        btnZonas.setText("Zonas");
        btnZonas.setBorder(null);
        btnZonas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnZonas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZonasActionPerformed(evt);
            }
        });

        bntRepartidores.setBackground(new java.awt.Color(0, 153, 255));
        bntRepartidores.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bntRepartidores.setForeground(new java.awt.Color(255, 255, 255));
        bntRepartidores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon-repartidores.png"))); // NOI18N
        bntRepartidores.setText("Repartidores");
        bntRepartidores.setBorder(null);
        bntRepartidores.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bntRepartidores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntRepartidoresActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(0, 153, 255));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon-cerrar-sesion.png"))); // NOI18N
        btnLogout.setText("Cerrar sesion");
        btnLogout.setBorder(null);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        labelUsername.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUsername.setText("NombreUsuario");
        labelUsername.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnExport.setBackground(new java.awt.Color(0, 153, 255));
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon-export.png"))); // NOI18N
        btnExport.setText("Exportar");
        btnExport.setBorder(null);
        btnExport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(panelImageLogo7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnZonas, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bntRepartidores, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(panelImageLogo7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelUsername)
                .addGap(28, 28, 28)
                .addComponent(btnPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnZonas, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bntRepartidores, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        containerHome.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 552));

        containerPedidos.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Pedidos");

        btnAddPedido.setBackground(new java.awt.Color(0, 153, 255));
        btnAddPedido.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddPedido.setForeground(new java.awt.Color(255, 255, 255));
        btnAddPedido.setText("Crear nuevo pedido");
        btnAddPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPedidoActionPerformed(evt);
            }
        });

        scrollPedidos.setBackground(new java.awt.Color(255, 255, 255));
        scrollPedidos.setBorder(null);

        containerListPedidos.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout containerListPedidosLayout = new javax.swing.GroupLayout(containerListPedidos);
        containerListPedidos.setLayout(containerListPedidosLayout);
        containerListPedidosLayout.setHorizontalGroup(
            containerListPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
        );
        containerListPedidosLayout.setVerticalGroup(
            containerListPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        scrollPedidos.setViewportView(containerListPedidos);

        javax.swing.GroupLayout containerPedidosLayout = new javax.swing.GroupLayout(containerPedidos);
        containerPedidos.setLayout(containerPedidosLayout);
        containerPedidosLayout.setHorizontalGroup(
            containerPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPedidos)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerPedidosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(containerPedidosLayout.createSequentialGroup()
                        .addGroup(containerPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        containerPedidosLayout.setVerticalGroup(
            containerPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPedidosLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrollPedidos)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pedidos", containerPedidos);

        containerZonas.setBackground(new java.awt.Color(255, 255, 255));
        containerZonas.setMinimumSize(new java.awt.Dimension(0, 0));
        containerZonas.setPreferredSize(new java.awt.Dimension(674, 555));
        containerZonas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ZONAS DE REPARTO");
        containerZonas.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, 662, -1));
        containerZonas.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 55, 662, 10));

        btnAddZona.setBackground(new java.awt.Color(0, 153, 255));
        btnAddZona.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddZona.setForeground(new java.awt.Color(255, 255, 255));
        btnAddZona.setText("+ Agregar zona");
        btnAddZona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddZonaActionPerformed(evt);
            }
        });
        containerZonas.add(btnAddZona, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 71, 174, 32));

        scrollZonas.setBackground(new java.awt.Color(255, 255, 255));
        scrollZonas.setBorder(null);
        scrollZonas.setPreferredSize(new java.awt.Dimension(662, 430));

        containerListZonas.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout containerListZonasLayout = new javax.swing.GroupLayout(containerListZonas);
        containerListZonas.setLayout(containerListZonasLayout);
        containerListZonasLayout.setHorizontalGroup(
            containerListZonasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
        );
        containerListZonasLayout.setVerticalGroup(
            containerListZonasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        scrollZonas.setViewportView(containerListZonas);

        containerZonas.add(scrollZonas, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 121, -1, -1));

        jTabbedPane1.addTab("Zonas", containerZonas);

        containerRepartidores.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("REPARTIDORES");

        btnAddRepa.setBackground(new java.awt.Color(0, 153, 255));
        btnAddRepa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddRepa.setForeground(new java.awt.Color(255, 255, 255));
        btnAddRepa.setText("+ Agregar repartidor");
        btnAddRepa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRepaActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        containerListRepartidores.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout containerListRepartidoresLayout = new javax.swing.GroupLayout(containerListRepartidores);
        containerListRepartidores.setLayout(containerListRepartidoresLayout);
        containerListRepartidoresLayout.setHorizontalGroup(
            containerListRepartidoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
        );
        containerListRepartidoresLayout.setVerticalGroup(
            containerListRepartidoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(containerListRepartidores);

        javax.swing.GroupLayout containerRepartidoresLayout = new javax.swing.GroupLayout(containerRepartidores);
        containerRepartidores.setLayout(containerRepartidoresLayout);
        containerRepartidoresLayout.setHorizontalGroup(
            containerRepartidoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerRepartidoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerRepartidoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, containerRepartidoresLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddRepa, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(containerRepartidoresLayout.createSequentialGroup()
                        .addGroup(containerRepartidoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        containerRepartidoresLayout.setVerticalGroup(
            containerRepartidoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerRepartidoresLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddRepa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Repartidores", containerRepartidores);

        containerHome.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, -38, -1, 590));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPedidosActionPerformed
        // TODO add your handling code here:
        //Cambio a pestana de pedidos
        this.jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnPedidosActionPerformed

    private void btnZonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZonasActionPerformed
        // TODO add your handling code here:
        this.jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnZonasActionPerformed

    private void bntRepartidoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntRepartidoresActionPerformed
        // TODO add your handling code here:
        this.jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_bntRepartidoresActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnAddPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPedidoActionPerformed
        // TODO add your handling code here:
        new CreatePedidoForm(this.operador, this).setVisible(true);
    }//GEN-LAST:event_btnAddPedidoActionPerformed

    private void btnAddZonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddZonaActionPerformed
        // TODO add your handling code here:
        new CreateZonaForm(this).setVisible(true);
    }//GEN-LAST:event_btnAddZonaActionPerformed

    private void btnAddRepaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRepaActionPerformed
        // TODO add your handling code here:
        new CreateRepartidorForm(this).setVisible(true);
    }//GEN-LAST:event_btnAddRepaActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        this.menuExportar.show(this.btnExport, this.btnExport.getWidth(), 0);
    }//GEN-LAST:event_btnExportActionPerformed

    private void itemExcelZonasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemExcelZonasActionPerformed
        // TODO add your handling code here:
        
        DaoExportData.generarReporteExcelZonas(this.zonas, this.pedidos);
        
        
    }//GEN-LAST:event_itemExcelZonasActionPerformed

    private void itemRepasPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemRepasPedidosActionPerformed
        // TODO add your handling code here:
        
        DaoExportData.generarReportePDFRepartidores(this.repartidores, this.pedidos);
    }//GEN-LAST:event_itemRepasPedidosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeOperador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeOperador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeOperador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeOperador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeOperador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntRepartidores;
    private javax.swing.JButton btnAddPedido;
    private javax.swing.JButton btnAddRepa;
    private javax.swing.JButton btnAddZona;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPedidos;
    private javax.swing.JButton btnZonas;
    private javax.swing.JPanel containerHome;
    private javax.swing.JPanel containerListPedidos;
    private javax.swing.JPanel containerListRepartidores;
    private javax.swing.JPanel containerListZonas;
    private javax.swing.JPanel containerPedidos;
    private javax.swing.JPanel containerRepartidores;
    private javax.swing.JPanel containerZonas;
    private javax.swing.JMenuItem itemExcelZonas;
    private javax.swing.JMenuItem itemRepasPedidos;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JPopupMenu menuExportar;
    private org.edisoncor.gui.panel.PanelImage panelImageLogo7;
    private javax.swing.JScrollPane scrollPedidos;
    private javax.swing.JScrollPane scrollZonas;
    // End of variables declaration//GEN-END:variables
}
