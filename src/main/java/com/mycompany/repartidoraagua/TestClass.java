/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repartidoraagua;

import com.mycompany.dao.WSManager;
import java.util.List;

import com.mycompany.domain.Operador;
import com.mycompany.domain.Repartidor;
import com.mycompany.domain.Rol;
import com.mycompany.domain.Usuario;

/**
 *
 * @author carlo
 */
public class TestClass {
    public static void main(String[] args) {

        // Test the WSManager class
        // List<Usuario> usuarios = WSManager.getUsuarios();

        // System.out.println("USUARIOS:");
        // usuarios.forEach(System.out::println);

        // System.out.println("USUARIO CON ID 1:");
        // System.out.println(WSManager.getUsuarioById(2));

        // Prueba login

        // Usuario usuario = WSManager.iniciarSesion("prueba@gmail.com", "prueba");
        
        // if (usuario == null) {
        //     System.out.println("Usuario y/o contrasena incorrectos");
        // }

        // // Verificamos si el usuario es instancia de Operador o Repartidor
        // if (usuario instanceof Operador) {
        //     System.out.println("Es un operador:" + ((Operador) usuario));
        // } else if (usuario instanceof Repartidor) {
        //     System.out.println("Es un repartidor:" + ((Repartidor) usuario));
        // } else {
        //     System.out.println("Es un usuario:" + usuario);
        // }

        // Prueba de registro
//        Usuario usuario = new Usuario("maria", "maria@gmail.com", "maria", Rol.OPERADOR, true);
//
//        int id = WSManager.guardarUsuario(usuario);
//
//        if (id == -1) {
//            System.out.println("No se pudo guardar el usuario");
//        } else {
//            System.out.println("Usuario guardado con id: " + id);
//        }

    }
}
