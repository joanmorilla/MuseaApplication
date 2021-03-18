package com.example.museaapplication.ui.login;

import android.app.Application;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.museaapplication.R;

import java.util.Calendar;
import java.util.Date;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Integer> nAttempts;
    private int unlockTime;
    private boolean enabled;

    public LoginViewModel() {
        unlockTime = currentTime();
        enabled = true;
        nAttempts = new MutableLiveData<>();
        nAttempts.setValue(0);
    }

    public MutableLiveData<Integer> getnAttempts() {
        return nAttempts;
    }

    public boolean validUsernamePassword(String username, String password) {

        nAttempts.setValue(nAttempts.getValue()+1);

        // Prevenir más loggeos hasta que pasen N segundos
        if (!enabled) {
            if (currentTime() > unlockTime) {
                enabled = true;
                nAttempts.setValue(0);
            } else
                return false;
        }


        // TODO: Implementar logica de validacion de usuario y contraseña
        if (username.isEmpty() || password.isEmpty())
            return false;
        return true;

    }

    public void disableLogin() {
        if (enabled) {
            enabled = false;
            unlockTime = currentTime() + 30;
        }
    }

    private int currentTime() { return (int) (Calendar.getInstance().getTime().getTime()/1000);}

    public int getRemainingTime() { return (unlockTime - currentTime()); }
}
