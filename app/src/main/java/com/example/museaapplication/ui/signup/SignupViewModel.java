package com.example.museaapplication.ui.signup;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupViewModel extends ViewModel {

    public boolean existsUsername(String username) {
        // TODO: implementar logica para determinar si ya existe un usuario con el mismo username
        return true;
    }

    public boolean isEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean existsEmail(String email) {
        // TODO: implementar logica para determinar si es un email ya existe
        return true;
    }

    public boolean isValidPassword(String password) {
        String UpperCaseRegex= ".*[A-Z].*";
        String LowerCaseRegex= ".*[a-z].*";
        String NumberRegex= ".*[0-9].*";
        //System.out.println(password.matches(NumberRegex) && password.matches(UpperCaseRegex) && password.matches(LowerCaseRegex));
        return password.matches(NumberRegex) && password.matches(UpperCaseRegex) && password.matches(LowerCaseRegex);

    }
}
