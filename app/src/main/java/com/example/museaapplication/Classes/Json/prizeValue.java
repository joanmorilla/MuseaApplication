package com.example.museaapplication.Classes.Json;

import com.example.museaapplication.Classes.Dominio.Prize;

public class prizeValue {
    private Prize[] prize;

    public Prize getPrize(int i){return prize[i];}

    public Prize[] getPrizeList(){return prize;}

}
