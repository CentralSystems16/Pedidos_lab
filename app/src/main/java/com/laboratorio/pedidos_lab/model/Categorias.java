package com.laboratorio.pedidos_lab.model;

public class Categorias {

    String nombreCategoria;
    int idCategoria;
    private final String imgCategoria;

    public Categorias(int idCategoria, String nombreCategoria, String imgCategoria) {

        this.nombreCategoria = nombreCategoria;
        this.imgCategoria = imgCategoria;
        this.idCategoria = idCategoria;

    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getImgCategoria() {
        return imgCategoria;
    }


}
