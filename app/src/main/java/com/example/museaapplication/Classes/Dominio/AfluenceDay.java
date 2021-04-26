package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class AfluenceDay implements Serializable {
    private String dayName;
    private int[] hours;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public int[] getHours() {
        return hours;
    }

    public void setHours(int[] Hours) {
        hours = hours;
    }
}
