package com.example.covm;
public class VerPresta_data {

    private String ccNombre;
    private String ccCodigo;
    private String ccNos;
    private Long ccOper;
    private Long ccMonto;

    public void setccNombre(String ccNombre){this.ccNombre=ccNombre;}
    public void setccCodigo(String ccCodigo){this.ccCodigo=ccCodigo;}
    public void setccNos(String ccNos){this.ccNos=ccNos;}
    public void setccOper(Long ccOper){this.ccOper=ccOper;}
    public void setccMonto(Long ccMonto){this.ccMonto=ccMonto;}

    public String getccNombre(){return ccNombre;}
    public String getccCodigo(){return ccCodigo;}
    public String getccNos(){return ccNos;}
    public long getccOper(){return ccOper;}
    public long getccMonto(){return ccMonto;}
}
