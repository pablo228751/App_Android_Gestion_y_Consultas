package com.example.covm;

public class agenda_consultar_datos {
    private  String fecha;
    private String hora;
    private String nombre;

    public agenda_consultar_datos() {
    }

    public agenda_consultar_datos(String fecha, String hora, String nombre) {
        this.fecha = fecha;
        this.hora = hora;
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
