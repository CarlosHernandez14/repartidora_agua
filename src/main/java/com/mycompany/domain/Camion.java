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
public class Camion implements Serializable{
    
    private int idCamion;
    private int capacidad;

    public Camion() {
    }

    // constructor lectura
    public Camion(int idCamion, int capacidad) {
        this.idCamion = idCamion;
        this.capacidad = capacidad;
    }
    
    // CONSTRUCTOR ESCRITURA/CREACION

    public Camion(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getIdCamion() {
        return idCamion;
    }

    public void setIdCamion(int idCamion) {
        this.idCamion = idCamion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return "Camion{" + "idCamion=" + idCamion + ", capacidad=" + capacidad + '}';
    }
}
