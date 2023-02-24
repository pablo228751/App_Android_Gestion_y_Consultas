package com.example.covm;

public class periodos {

    private String periodo;
    private String anio;

    public  periodos(){


    }
    public periodos(String periodo, String anio) {
        this.periodo = periodo;
        this.anio = anio;
    }
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getAnio() {
        return anio;
    }

    @Override
    public String toString() {
        return periodo;
    }

    public String toString2() {
        return anio;
    }
}
