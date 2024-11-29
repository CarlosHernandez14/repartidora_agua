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
    private int idUsuario;
    private String horario;
    
    

    public Operador() {
    }
    
    // CONSTRUCTOR PARA LEER DATOS
    public Operador(int idOperador, int idUsuario, String horario, int id, String nombre, String correo, String contrasena, Rol rol, boolean activo) {
        super(id, nombre, correo, contrasena, rol, activo);
        this.idOperador = idOperador;
        this.idUsuario = idUsuario;
        this.horario = horario;
    }
    
    // CONSTRUCTOR PARA CREAR OBJETOS NUEVOS
    public Operador(String horario, String nombre, String correo, String contrasena, Rol rol, boolean activo) {
        super(nombre, correo, contrasena, rol, activo);
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

    @Override
    public String toString() {
        return "Operador{" + "idOperador=" + idOperador + ", idUsuario=" + idUsuario + ", horario=" + horario + '}';
    }
    
    
    
}
