package com.example.covm;

public class VerComprobante_data {

    private String ccNombre;
    private String ccCodigo;
    private Long ccMonto;
    private Long ccCantidad;

    public void setccNombre(String ccNombre){this.ccNombre=ccNombre;}
    public void setccCodigo(String ccCodigo){this.ccCodigo=ccCodigo;}
    public void setccMonto(Long ccMonto){this.ccMonto=ccMonto;}
    public void setccCantidad(Long ccCantidad){this.ccCantidad=ccCantidad;}

    public String getccNombre(){return ccNombre;}
    public String getccCodigo(){return ccCodigo;}
    public long getccMonto(){return ccMonto;}
    public long getccCantidad(){return ccCantidad;}
}
