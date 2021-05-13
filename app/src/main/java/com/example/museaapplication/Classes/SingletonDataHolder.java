package com.example.museaapplication.Classes;

import androidx.fragment.app.Fragment;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.ui.user.UserViewModel;

import java.util.ArrayList;
import java.util.Stack;

public class SingletonDataHolder {
    private static SingletonDataHolder _instance;
    public static Fragment active;

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
    private ArrayList<String> modifiedMuseums;

    public int main_initial_frag = 0;
    public static Stack<Integer> backStack;


    public boolean isModified(String _id) {
        if (modifiedMuseums == null) return false;
        boolean res = modifiedMuseums.contains(_id);
        // Already reloaded.
        modifiedMuseums.remove(_id);
        return res;
    }

    public void addModified(String _id){
        if (modifiedMuseums == null) modifiedMuseums = new ArrayList<>();
        modifiedMuseums.add(_id);
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
