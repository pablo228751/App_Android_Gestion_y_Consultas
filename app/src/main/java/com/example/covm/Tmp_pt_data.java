package com.example.covm;
public class Tmp_pt_data {

    private String tmpNombre;
    private String tmpCodigo;
    private long tmpCantidad;
    private double tmpMonto;
    private double tmpTotal;


    public void settmpNombre(String tmpNombre){this.tmpNombre=tmpNombre;}
    public void settmpCodigo(String tmpCodigo){this.tmpCodigo=tmpCodigo;}
    public void settmpCantidad(Long tmpCantidad){this.tmpCantidad=tmpCantidad;}
    public void settmpMonto(Double tmpMonto){this.tmpMonto=tmpMonto;}
    public void settmpTotal(Double tmpTotal){this.tmpTotal=tmpTotal;}

    public String gettmpNombre(){return tmpNombre;}
    public String gettmpCodigo(){return tmpCodigo;}
    public long gettmpCantidad(){return tmpCantidad;}
    public double gettmpMonto(){return tmpMonto;}
    public double gettmpTotal(){return tmpTotal;}
}
