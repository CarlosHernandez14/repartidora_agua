/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain;

import java.io.Serializable;

/**
 *
 * @author carlo
 */
public class Operador extends Usuario implements Serializable{
    
    
    private int idOperador;
    private String nombre_completo;
    private int idUsuario;
    private String horario;
    
    

    public Operador() {
    }
    
    // CONSTRUCTOR PARA LEER DATOS
    public Operador(int idOperador, int idUsuario, String horario, int id, String nombre, String correo, String contrasena, Rol rol, boolean activo, String nombreCompleto) {
        super(id, nombre, correo, contrasena, rol, activo);
        this.idOperador = idOperador;
        this.idUsuario = idUsuario;
        this.horario = horario;
        this.nombre_completo = nombreCompleto;
    }
    
    
    public Operador(String horario, String nombre, String correo, String contrasena, Rol rol, boolean activo, String nombreCompleto) {
        super(nombre, correo, contrasena, rol, activo);
        this.horario = horario;
        this.nombre_completo = nombreCompleto;
    }
    
    // CONSTRUCTOR PARA CREAR OBJETOS NUEVOS

    public Operador(String nombre_completo, String horario, String nombre, String correo, String contrasena, Rol rol) {
        super(nombre, correo, contrasena, rol);
        this.nombre_completo = nombre_completo;
        this.horario = horario;
    }
    

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
    
    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    @Override
    public String toString() {
        return "Operador{" + "idOperador=" + idOperador + ", idUsuario=" + idUsuario + ", horario=" + horario + '}' + "\n" + super.toString();
    }

    
    
}
