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
public class Repartidor extends Usuario implements Serializable{
    
    
    private int idRepartidor;
    private String nombre_completo;
    private String telefono;
    private int idCamion;
    
    public Repartidor(){
        
    }
    
    // CONSTRUCTOR PARA LECTURA
    public Repartidor(int idRepartidor, String telefono, int idCamion, int id, String nombre, String correo, String contrasena, Rol rol, boolean activo, String nombreCompleto) {
        super(id, nombre, correo, contrasena, rol, activo);
        this.idRepartidor = idRepartidor;
        this.telefono = telefono;
        this.idCamion = idCamion;
        this.nombre_completo = nombreCompleto; 
    }
    
    // CONSTRUCTOR PARA CREACION
    public Repartidor(String telefono, int idCamion, String nombre, String correo, String contrasena, Rol rol, boolean activo, String nombreCompleto) {
        super(nombre, correo, contrasena, rol, activo);
        this.telefono = telefono;
        this.idCamion = idCamion;
        this.nombre_completo = nombreCompleto;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(int idCamion) {
        this.idCamion = idCamion;
    }
    
    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }
    

    @Override
    public String toString() {
        return "Repartidor{" + "idRepartidor=" + idRepartidor + ", telefono=" + telefono + ", idCamion=" + idCamion + '}' + "\n" + super.toString();
    }

    
    
}
