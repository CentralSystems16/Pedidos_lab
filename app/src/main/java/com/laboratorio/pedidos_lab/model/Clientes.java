package com.laboratorio.pedidos_lab.model;

public class Clientes {

    String nombre;
    int idCliente;

    public Clientes(String nombre, int idCliente) {
        this.nombre = nombre;
        this.idCliente = idCliente;

    }

    public String getNombre() {
        return nombre;
    }

    public int getIdCliente() {
        return idCliente;
    }
}
