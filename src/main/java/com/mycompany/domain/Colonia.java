/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain;

/**
 *
 * @author carlo
 */
public class Colonia {
    
    private int idColonia;
    private String nombre;
    private Calle[] calles;
    private int idZona;

    public Colonia() {
    }
    
    // CONSTRUCTOR LECTURA
    public Colonia(int idColonia, String nombre, Calle[] calles, int idZona) {
        this.idColonia = idColonia;
        this.nombre = nombre;
        this.calles = calles;
        this.idZona = idZona;
    }

    // CONSTRUCTOR CREACION
    public Colonia(String nombre, Calle[] calles, int idZona) {
        this.nombre = nombre;
        this.calles = calles;
        this.idZona = idZona;
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

    public Calle[] getCalles() {
        return calles;
    }

    public void setCalles(Calle[] calles) {
        this.calles = calles;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    @Override
    public String toString() {
        return "Colonia{" + "idColonia=" + idColonia + ", nombre=" + nombre + ", calles=" + calles + ", idZona=" + idZona + '}';
    }
    
    
    
}
