package com.example.museaapplication.Classes;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class TimeClass {
    private static TimeClass _instance;
    private static int today;

    public static TimeClass getInstance() {
        if (_instance == null) {
            _instance = new TimeClass();
            Calendar calendar = Calendar.getInstance();
            today = reformatDay(calendar.get(Calendar.DAY_OF_WEEK));
        }
        return _instance;
    }

    public int getToday() {
        return today;
    }

    public static int getCurDay() {
        return reformatDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }

    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    public static int getCurHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    private static int reformatDay(int day){
        int temp = day - 2;
        if (temp < 0) temp = 6;
        return temp;
    }
}
