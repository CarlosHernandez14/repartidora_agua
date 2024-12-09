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
    private String placas;
    private String modelo;
    private String marca;

    public Camion() {
    }

    // constructor lectura

    public Camion(int idCamion, int capacidad, String placas, String modelo, String marca) {
        this.idCamion = idCamion;
        this.capacidad = capacidad;
        this.placas = placas;
        this.modelo = modelo;
        this.marca = marca;
    }
    
    
    // CONSTRUCTOR ESCRITURA/CREACION

    public Camion(int capacidad, String placas, String modelo, String marca) {
        this.capacidad = capacidad;
        this.placas = placas;
        this.modelo = modelo;
        this.marca = marca;
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

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Camion{" + "idCamion=" + idCamion + ", capacidad=" + capacidad + ", placas=" + placas + ", modelo=" + modelo + ", marca=" + marca + '}';
    }
}
