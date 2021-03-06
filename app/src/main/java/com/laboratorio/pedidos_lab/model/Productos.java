package com.laboratorio.pedidos_lab.model;

import java.io.Serializable;

public class Productos implements Serializable {

    int idProducto, opciones;
    String nombreProducto, imgProducto, barcode;
    Double precioProducto;
    boolean isSelect = false;

    public Productos(int idProducto, String nombreProducto, Double precioProducto, int opciones, String imgProducto, String barCode) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.opciones = opciones;
        this.imgProducto = imgProducto;
        this.barcode = barCode;

    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getOpciones() {
        return opciones;
    }

    public void setOpciones(int opciones) {
        this.opciones = opciones;
    }

    public String getImgProducto() {
        return imgProducto;
    }

    public String getBarCode() {
        return barcode;
    }
}