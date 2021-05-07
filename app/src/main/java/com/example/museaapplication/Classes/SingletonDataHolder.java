package com.example.museaapplication.Classes;

import android.util.Log;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.ui.user.UserViewModel;

import java.util.Stack;

public class SingletonDataHolder {
    private static SingletonDataHolder _instance;

    public static UserViewModel userViewModel;


    public static SingletonDataHolder getInstance() {
        if (_instance == null) {
            _instance = new SingletonDataHolder();
            backStack = new Stack<>();
        }
        return _instance;
    }

    private String codedImage;

    private Museo[] museums;

    public int main_initial_frag = 0;
    public static Stack<Integer> backStack;

    public void UpdateLikes(){
        Log.e("Funciona", "UPDATELIKES SINGLETON");
        userViewModel.UpdateLikes();
    }



    public String getCodedImage(){
        return codedImage;
    }
    public void setCodedImage(String c){
        codedImage = c;
    }

    public Museo[] getMuseums() {
        return museums;
    }

    public void setMuseums(Museo[] museums) {
        this.museums = museums;
    }
}
