/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain;

/**
 *
 * @author carlo
 */
public class Calle {
    
    private int idCalle;
    private int idColonia;
    private String nombre;
    private String descripcion;
    
    public Calle() {
        
    }

    // CONSTRUCTOR LECTURA
    public Calle(int idCalle, int idColonia, String nombre, String descripcion) {
        this.idCalle = idCalle;
        this.idColonia = idColonia;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // CONSTRUCTOR CREACION
    public Calle(int idColonia, String nombre, String descripcion) {
        this.idColonia = idColonia;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdCalle() {
        return idCalle;
    }

    public void setIdCalle(int idCalle) {
        this.idCalle = idCalle;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Calle{" + "idCalle=" + idCalle + ", idColonia=" + idColonia + ", nombre=" + nombre + ", descripcion=" + descripcion + '}';
    }
    
}
