package com.example.museaapplication.ui.login;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.ui.MainActivity;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.signup.SignupFragment;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Set;

public class LoginFragment extends Fragment {

    private View root;
    private FragmentManager fm;
    private LoginViewModel loginViewModel;
    private Integer r;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        root = inflater.inflate(R.layout.fragment_login, container, false);
        fm = getParentFragmentManager();

        // Carga de preferencias compartidas
        SharedPreferences sharedPref = getActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE);

        // Imagen de portada
        ImageView imageView = root.findViewById(R.id.image_holder);
        String url = getContext().getResources().getString(R.string.url_logo);
        Picasso.get().load(url).fit().into(imageView);

        // Get text del username y de la password
        final EditText username = root.findViewById(R.id.enter_username);
        final EditText password = root.findViewById(R.id.enter_password);

        final CheckBox checkBoxRememberMe = root.findViewById(R.id.checkbox_remember_me);

        final TextView textWarnings = root.findViewById(R.id.text_warnigs);

        final View loadingPanel = root.findViewById(R.id.loadingPanel);

        loginViewModel.getRes().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                r = integer;
                loadingPanel.setVisibility(View.GONE);
                if (r.equals(1)) {
                    Log.d("Response state","Succesfuly Login");

                    SharedPreferences.Editor editor = sharedPref.edit();
                    if (checkBoxRememberMe.isChecked()) {
                        String stringKey = username.getText().toString().concat("#").concat(password.getText().toString());
                        editor.putString(getString(R.string.auto_signin_key), stringKey);
                    }
                    else {
                        editor.putString(getString(R.string.auto_signin_key), "");
                    }
                    editor.apply();


                    Toast.makeText(getContext(), "Logged", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                else if (r.equals(2)) {
                    Log.d("Response state","Wrong username or password");
                    textWarnings.setText(getContext().getResources().getString(R.string.warning_login));
                }
                else if (r.equals(-1)){
                    Log.d("Response state","algo ocurrio");
                    Toast.makeText(getContext(), "Something went wrong try again later", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Response state","esperando respuesta...");
                    loadingPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        // Implementación del botton 'login'
        final Button loginButton = root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginViewModel.validUsernamePassword(username.getText().toString(),password.getText().toString())) {
                    if (textWarnings.getText().toString().isEmpty()) textWarnings.setText(getContext().getResources().getString(R.string.warning_login2));
                }
            }
        });

        // Inicio de sesión automatico
        String defaultValue = "";
        String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), defaultValue);
        if (!sharedValue.isEmpty()) {
            int index = sharedValue.lastIndexOf('#');
            Log.d("SharedPreferences","Login automatico: " + sharedValue);
            Log.d("SharedPreferences",sharedValue.substring(0,index));
            Log.d("SharedPreferences",sharedValue.substring(index + 1));
            if (index != -1) {
                username.setText(sharedValue.substring(0,index));
                password.setText(sharedValue.substring(index + 1));
                loginButton.performClick();
            }
        }
        else {
            Log.d("SharedPreferences","Logeate tu mismo crack " + sharedValue);
            loadingPanel.setVisibility(View.GONE);
        }


        // Implementación de los intentos de inicio de sesión
        loginViewModel.getnAttempts().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer n) {
                System.out.println(loginViewModel.getRemainingTime());
                if (n > 10) {
                    loginViewModel.disableLogin();
                    textWarnings.setText(getContext().getResources().getString(R.string.warning_attempts_exceeded));
                    textWarnings.setText(textWarnings.getText() + "\n" + loginViewModel.getRemainingTime() + " seconds left to try again");
                }
                if (n == 0)
                    textWarnings.setText("");
            }
        });

        // Implementación del comportamiento del texto 'sign up'
        final TextView signupText = root.findViewById(R.id.text_signup);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change frag
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME", username.getText().toString());
                Fragment fragment = new SignupFragment();
                fragment.setArguments(bundle);
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.InitialActivity,fragment)
                        .addToBackStack("tag")
                        .commit();
            }
        });
        // inflate the layout using the cloned inflater, not default inflater
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        final EditText username = root.findViewById(R.id.enter_username);
        final EditText password = root.findViewById(R.id.enter_password);
        final CheckBox checkBoxRememberMe = root.findViewById(R.id.checkbox_remember_me);
        final TextView textWarnings = root.findViewById(R.id.text_warnigs);
        textWarnings.setText("");
        //password.setText("");
        //username.setText("");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("infoUser", Context.MODE_PRIVATE);
        String defaultValue = "";
        String sharedValue = sharedPref.getString(getString(R.string.auto_signin_key), defaultValue);
        if (checkBoxRememberMe.isChecked() || !sharedValue.isEmpty()) {
            checkBoxRememberMe.setChecked(true);
            int index = sharedValue.lastIndexOf('#');
            Log.d("SharedPreferences2","Login automatico: " + sharedValue);
            Log.d("SharedPreferences2",sharedValue.substring(0,index));
            Log.d("SharedPreferences2",sharedValue.substring(index + 1));
            username.setText(sharedValue.substring(0,index));
            password.setText(sharedValue.substring(index+1));
        }
        else {
            password.setText("");
            username.setText("");
        }
    }

    String imageToString(int id){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    Bitmap stringToImage(String codeImage){
        byte[] imageBytes = Base64.decode(codeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
