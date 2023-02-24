package com.example.covm;
public class Pt_data {

    private String npNombre;
    private String npTitular;
    private String npFecha;
    private Integer npNro;
    private Integer npCta;
    private Integer npOper;
    private long npMonto;


    public void setnpNombre(String npNombre){this.npNombre=npNombre;}
    public void setnpTitular(String npTitular){this.npTitular=npTitular;}
    public void setnpFecha(String npFecha){this.npFecha=npFecha;}
    public void setnpNro(Integer npNro){this.npNro=npNro;}
    public void setnpCta(Integer npCta){this.npCta=npCta;}
    public void setnpOper(Integer npOper){this.npOper=npOper;}
    public void setnpMonto(Long npMonto){this.npMonto=npMonto;}

    public String getnpNombre(){return npNombre;}
    public String getnpTitular(){return npTitular;}
    public String getnpFecha(){return npFecha;}
    public Integer getnpNro(){return npNro;}
    public Integer getnpCta(){return npCta;}
    public Integer getnpOper(){return npOper;}
    public long getnpMonto(){return npMonto;}
}
