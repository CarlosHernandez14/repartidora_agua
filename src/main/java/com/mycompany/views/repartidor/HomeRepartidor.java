/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.views.repartidor;

import com.mycompany.dao.WSManager;
import com.mycompany.domain.Pedido;
import com.mycompany.domain.Repartidor;
import com.mycompany.domain.Zona;
import com.mycompany.views.operador.PanelPedido;
import com.mycompany.views.operador.PanelZona;
import com.mycompany.vistas.Login;
import com.mycompany.vistas.PanelImageRedondeado;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;



public class HomeRepartidor extends javax.swing.JFrame {

    private Repartidor repartidor;
    private ArrayList<Zona> zonas;
    private ArrayList<Pedido> pedidos;
    
    /**
     * Creates new form HomeOperador
     */
    public HomeRepartidor() {
        initComponents();
        
        this.setLocationRelativeTo(null);
    }
    
    public HomeRepartidor(Repartidor repartidor) {
        initComponents();
        this.repartidor = repartidor;
        this.setLocationRelativeTo(null);
        
        this.containerListZonas.setLayout(new BoxLayout(this.containerListZonas, BoxLayout.Y_AXIS));
        this.containerListPedidos.setLayout(new BoxLayout(this.containerListPedidos, BoxLayout.Y_AXIS));
        initData();
    }
    
    private void initData() {
        loadZonas();
        loadPedidos();
        
        //System.out.println("ZONAS");
        //this.zonas.forEach(System.out::println);
        //this.pedidos.forEach(System.out::println);
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
    
    public void loadPedidos() {
        this.containerListPedidos.removeAll();

        // Set pedidos
        this.pedidos = (ArrayList<Pedido>) WSManager.getPedidosByRepartidor(this.repartidor.getIdRepartidor());

        for (Pedido pedido : pedidos) {
            PanelPedido panelPedido = new PanelPedido(pedido);

            this.containerListPedidos.add(panelPedido);
            this.containerListPedidos.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        this.containerListPedidos.revalidate();
        this.containerListPedidos.repaint();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerForm = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        btnPedidos = new javax.swing.JButton();
        panelImageLogo7 = new PanelImageRedondeado();
        btnZonas = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        labelUsername = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        containerPedidos = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        scrollPedidos = new javax.swing.JScrollPane();
        containerListPedidos = new javax.swing.JPanel();
        containerZonas = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        scrollZonas = new javax.swing.JScrollPane();
        containerListZonas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        containerForm.setBackground(new java.awt.Color(255, 255, 255));
        containerForm.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidebar.setBackground(new java.awt.Color(0, 153, 255));

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

        javax.swing.GroupLayout sidebarLayout = new javax.swing.GroupLayout(sidebar);
        sidebar.setLayout(sidebarLayout);
        sidebarLayout.setHorizontalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addGroup(sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(sidebarLayout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(panelImageLogo7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(sidebarLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnZonas, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        sidebarLayout.setVerticalGroup(
            sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidebarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(panelImageLogo7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelUsername)
                .addGap(28, 28, 28)
                .addComponent(btnPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnZonas, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        containerForm.add(sidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 552));

        containerPedidos.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Pedidos");

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
                .addGap(56, 56, 56)
                .addComponent(scrollPedidos)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pedidos", containerPedidos);

        containerZonas.setBackground(new java.awt.Color(255, 255, 255));
        containerZonas.setMaximumSize(new java.awt.Dimension(32767, 32767));
        containerZonas.setMinimumSize(new java.awt.Dimension(0, 0));
        containerZonas.setPreferredSize(new java.awt.Dimension(674, 555));
        containerZonas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ZONAS DE REPARTO");
        containerZonas.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, 662, -1));
        containerZonas.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 55, 662, 10));

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

        containerForm.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, -38, -1, 590));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_btnLogoutActionPerformed

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
            java.util.logging.Logger.getLogger(HomeRepartidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeRepartidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeRepartidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeRepartidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeRepartidor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPedidos;
    private javax.swing.JButton btnZonas;
    private javax.swing.JPanel containerForm;
    private javax.swing.JPanel containerListPedidos;
    private javax.swing.JPanel containerListZonas;
    private javax.swing.JPanel containerPedidos;
    private javax.swing.JPanel containerZonas;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelUsername;
    private org.edisoncor.gui.panel.PanelImage panelImageLogo7;
    private javax.swing.JScrollPane scrollPedidos;
    private javax.swing.JScrollPane scrollZonas;
    private javax.swing.JPanel sidebar;
    // End of variables declaration//GEN-END:variables
}
