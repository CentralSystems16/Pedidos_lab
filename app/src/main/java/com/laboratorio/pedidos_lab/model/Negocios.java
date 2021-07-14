package com.laboratorio.pedidos_lab.model;

public class Negocios {

    String nombre, imagen;
    int idNegocio;

    public Negocios(String nombre, String imagen, int idNegocio) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.idNegocio = idNegocio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public int getIdNegocio() {
        return idNegocio;
    }
}
