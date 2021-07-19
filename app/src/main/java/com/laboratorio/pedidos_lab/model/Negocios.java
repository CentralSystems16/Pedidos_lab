package com.laboratorio.pedidos_lab.model;

public class Negocios {

    String nombre, imagen, baseDatos;
    int idNegocio;

    public Negocios(String nombre, String imagen, int idNegocio, String baseDatos) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.idNegocio = idNegocio;
        this.baseDatos = baseDatos;
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

    public String getBaseDatos() {
        return baseDatos;
    }
}
