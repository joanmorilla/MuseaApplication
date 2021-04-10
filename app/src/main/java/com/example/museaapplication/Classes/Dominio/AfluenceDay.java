package com.example.museaapplication.Classes.Dominio;

import java.io.Serializable;

public class AfluenceDay implements Serializable {
    private String dayName;
    private int[] Hours;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public int[] getHours() {
        return Hours;
    }

    public void setHours(int[] hours) {
        Hours = hours;
    }
}
