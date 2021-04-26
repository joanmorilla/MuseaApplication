package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class Info implements Serializable {
    private String name;
    private String[] horari;
    private Boolean isOpen;
    private AfluenceDay[] afluence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getHorari() {
        return horari;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public AfluenceDay[] getAfluence() {
        return afluence;
    }

    public void setAfluence(AfluenceDay[] afluence) {
        this.afluence = afluence;
    }

    public void setHorari(String[] horari) {
        this.horari = horari;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }
}
