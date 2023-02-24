package com.example.covm;

public class CtaCte_data {
    private String ccNombre;
    private String ccObs;
    private String ccCodigo;
    private Double ccMonto;
    private Long ccApl;
    private Long ccNro;
    private Double ccSaldo;
    private String ccFecha;
    private Long ccOper;

    public void setccNombre(String ccNombre){this.ccNombre=ccNombre;}
    public void setccCodigo(String ccCodigo){this.ccCodigo=ccCodigo;}
    public void setccObs(String ccObs){this.ccObs=ccObs;}
    public void setccMonto(Double ccMonto){this.ccMonto=ccMonto;}
    public void setccApl(Long ccApl){this.ccApl=ccApl;}
    public void setccNro(Long ccNro){this.ccNro=ccNro;}
    public void setccSaldo(Double ccSaldo){this.ccSaldo=ccSaldo;}
    public void setccFecha(String ccFecha){this.ccFecha=ccFecha;}
    public void setccOper(Long ccOper){this.ccOper=ccOper;}

    public String getccNombre(){return ccNombre;}
    public String getccCodigo(){return ccCodigo;}
    public String getccObs(){return ccObs;}
    public long getccNro(){return ccNro;}
    public double getccMonto(){return ccMonto;}
    public long getccApl(){return ccApl;}
    public double getccSaldo(){return ccSaldo;}
    public String getccFecha(){return ccFecha;}
    public long getccOper(){return ccOper;}
}
