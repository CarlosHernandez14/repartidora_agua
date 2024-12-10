/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.domain;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author carlo
 */
public class Pedido implements Serializable{
    
    private int idPedido;
    private int idZona;
    private int cantidadGarrafones;
    
    private Date fecha;
    private EstadoPedido estado; // PENDIENTE O ENTREGADO
    private boolean prioridad; // TRUE EN CASO DE SER PRIORITARIO
    private int idRepartidor;
    private int idOperador;

    public Pedido() {
    }

    // CONSTRUCTOR LECTURA
    public Pedido(int idPedido, int idZona, int cantidadGarrafones, Date fecha, EstadoPedido estado, boolean prioridad, int idRepartidor, int idOperador) {
        this.idPedido = idPedido;
        this.idZona = idZona;
        this.cantidadGarrafones = cantidadGarrafones;
        this.fecha = fecha;
        this.estado = estado;
        this.prioridad = prioridad;
        this.idRepartidor = idRepartidor;
        this.idOperador = idOperador;
    }

    // CONSTRUCTOR CREACION

    public Pedido(int idZona, int cantidadGarrafones, int idOperador) {
        this.idZona = idZona;
        this.cantidadGarrafones = cantidadGarrafones;
        this.idOperador = idOperador;
    }

    // CONSTRUCTOR DE ACTUALIZACION
    

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getCantidadGarrafones() {
        return cantidadGarrafones;
    }

    public void setCantidadGarrafones(int cantidadGarrafones) {
        this.cantidadGarrafones = cantidadGarrafones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public boolean isPrioridad() {
        return prioridad;
    }

    public void setPrioridad(boolean prioridad) {
        this.prioridad = prioridad;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    @Override
    public String toString() {
        return "Pedido{" + "idPedido=" + idPedido + ", idZona=" + idZona + ", cantidadGarrafones=" + cantidadGarrafones + ", fecha=" + fecha + ", estado=" + estado + ", prioridad=" + prioridad + ", idRepartidor=" + idRepartidor + ", idOperador=" + idOperador + '}';
    }
    
}
