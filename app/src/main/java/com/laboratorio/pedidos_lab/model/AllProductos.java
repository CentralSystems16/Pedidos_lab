package com.laboratorio.pedidos_lab.model;

import java.io.Serializable;

public class AllProductos implements Serializable {

    int idProducto, opciones;
    String nombreProducto, barcode;
    Double precioProducto;
    boolean isSelect = false;

    public AllProductos(int idProducto, String nombreProducto, Double precioProducto, int opciones, String barcode) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.opciones = opciones;
        this.barcode = barcode;

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

    public String getBarcode() {
        return barcode;
    }
}