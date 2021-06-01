package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Prize;

import java.io.Serializable;

public class PrizeValue implements Serializable {
    private Prize[] prizes;

    public Prize getPrize(int i){return prizes[i];}

    public Prize[] getPrizes(){return prizes;}

    public void setPrizes(Prize[] prizes) {this.prizes = prizes;}

}
