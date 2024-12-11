/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.repartidoraagua;

import com.formdev.flatlaf.FlatLightLaf;
import com.mycompany.vistas.Login;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author carlo
 */
public class RepartidoraAgua {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Error al cargar el look and feel flat light laf");
        }
        
        new Login().setVisible(true);
    }
}
