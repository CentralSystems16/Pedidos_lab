package com.laboratorio.pedidos_lab.model;

public class Reportes {

    public String nombre;
    String fecha;
    int pedido;
    Double total;

    public Reportes(String nombre, String fecha, int pedido, Double total) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.pedido = pedido;
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    public Double getTotal() {
        return total;
    }

}
