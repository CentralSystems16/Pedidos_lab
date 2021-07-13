package com.laboratorio.pedidos_lab.model;

public class Clientes {

    String nombre, nacimiento, direccion, email;
    int idCliente, edad, meses;

    public Clientes(String nombre, String nacimiento, String direccion, String email, int idCliente, int edad, int meses) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.direccion = direccion;
        this.email = email;
        this.idCliente = idCliente;
        this.edad = edad;
        this.meses = meses;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getEdad() {
        return edad;
    }

    public int getMeses() {
        return meses;
    }
}
