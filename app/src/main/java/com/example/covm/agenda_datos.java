package com.example.covm;

public class agenda_datos {

    private  String nombre;
    private String codigo;

    public agenda_datos() {
    }

    public agenda_datos(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }



}
