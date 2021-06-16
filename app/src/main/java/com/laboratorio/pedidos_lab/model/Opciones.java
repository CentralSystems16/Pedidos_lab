package com.laboratorio.pedidos_lab.model;

public class Opciones {

    int idOpcion;
    String nombreOpcione;
    int idExamen;

    public Opciones(int idOpcion, String nombreOpcione, int idExamen) {
        this.idOpcion = idOpcion;
        this.nombreOpcione = nombreOpcione;
        this.idExamen = idExamen;
    }

    public int getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(int idOpcion) {
        this.idOpcion = idOpcion;
    }

    public String getNombreOpcione() {
        return nombreOpcione;
    }

    public void setNombreOpcione(String nombreOpcione) {
        this.nombreOpcione = nombreOpcione;
    }

    public int getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(int idExamen) {
        this.idExamen = idExamen;
    }
}
