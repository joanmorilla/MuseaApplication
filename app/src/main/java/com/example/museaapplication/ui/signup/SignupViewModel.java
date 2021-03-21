package com.example.museaapplication.ui.signup;


import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignupViewModel extends ViewModel {
    /*
    public boolean existsUser(String username, String email) {
        // TODO: implementar logica para determinar si ya existe un usuario con el mismo username
        return true;
    }
    public boolean existsEmail(String email) {
        // TODO: implementar logica para determinar si es un email ya existe
        return true;
    }
    */

    public boolean isEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        String UpperCaseRegex= ".*[A-Z].*";
        String LowerCaseRegex= ".*[a-z].*";
        String NumberRegex= ".*[0-9].*";
        //System.out.println(password.matches(NumberRegex) && password.matches(UpperCaseRegex) && password.matches(LowerCaseRegex));
        return password.matches(NumberRegex) && password.matches(UpperCaseRegex) && password.matches(LowerCaseRegex);

    }

    public boolean newSignup(String username, String password, String email) {
        // TODO: json que tenga username: testUser, password: testPass, email: test@test.com

        final JSONObject root = new JSONObject();
        try {
            root.put("username", username);
            root.put("password", password);
            root.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* TODO: Conectar al servidor "https://musea-authorization-server.herokuapp.com/signup", mandarle el JSON y tratar el resultado
            Si retorna 200 se ha creado correctamente si es 404 ya existe el usuario
        */

        return true;
    }


}
