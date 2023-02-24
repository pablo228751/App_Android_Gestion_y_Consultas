package com.example.covm;

public class aranceles_datos {
    String cod,obser;
    double precio,coseguro;

    public aranceles_datos() {
    }

    public aranceles_datos(String cod, String obser, double precio, double coseguro) {
        this.cod = cod;
        this.obser = obser;
        this.precio = precio;
        this.coseguro = coseguro;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getObser() {
        return obser;
    }

    public void setObser(String obser) {
        this.obser = obser;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCoseguro() {
        return coseguro;
    }

    public void setCoseguro(double coseguro) {
        this.coseguro = coseguro;
    }
}
