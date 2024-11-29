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
public class Zona implements Serializable {
    
    private int idZona;
    private String nombre;
    private String coordenadas_x;
    private String coordenadas_y;

    public Zona() {
    }

    // CONSTRUCTOR LECTURA
    public Zona(int idZona, String nombre, String coordenadas_x, String coordenadas_y) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.coordenadas_x = coordenadas_x;
        this.coordenadas_y = coordenadas_y;
    }

    // CONSTRUCTOR PARA CREACION
    public Zona(String nombre, String coordenadas_x, String coordenadas_y) {
        this.nombre = nombre;
        this.coordenadas_x = coordenadas_x;
        this.coordenadas_y = coordenadas_y;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCoordenadas_x() {
        return coordenadas_x;
    }

    public void setCoordenadas_x(String coordenadas_x) {
        this.coordenadas_x = coordenadas_x;
    }

    public String getCoordenadas_y() {
        return coordenadas_y;
    }

    public void setCoordenadas_y(String coordenadas_y) {
        this.coordenadas_y = coordenadas_y;
    }

    @Override
    public String toString() {
        return "Zona{" + "idZona=" + idZona + ", nombre=" + nombre + ", coordenadas_x=" + coordenadas_x + ", coordenadas_y=" + coordenadas_y + '}';
    }
    
    
}
