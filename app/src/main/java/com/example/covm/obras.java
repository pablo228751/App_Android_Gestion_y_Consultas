package com.example.covm;

public class obras {

    private String obras;
    private String plan;

    public  obras(){


    }
    public obras(String obras, String plan) {
        this.obras = obras;
        this.plan = plan;
    }
    public void setObras(String obras) {
        this.obras = obras;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getObras() {
        return obras;
    }

    public String getPlan() {
        return plan;
    }

    @Override
    public String toString() {
        return obras;
    }

    public String toString2() {
        return plan;
    }
}
