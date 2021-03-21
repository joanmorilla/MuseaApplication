package com.example.museaapplication.Classes.Persitencia;

public class CtrlPersistencia {
        private static CtrlPersistencia _instance;

        public static CtrlPersistencia getInstance(){
            if(_instance == null) _instance = new CtrlPersistencia();
            return _instance;
        }


}
