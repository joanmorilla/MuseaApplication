package com.example.museaapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.SingletonDataHolder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.museaapplication.Classes.SingletonDataHolder;

import java.io.ByteArrayOutputStream;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Imagen de portada
        SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.image_holder));
        String image = SingletonDataHolder.getInstance().getCodedImage();
        ImageView imageView = findViewById(R.id.image_holder);
        imageView.setImageBitmap(stringToImage(image));

        // Get text del username y de la password
        final EditText username = (EditText)findViewById(R.id.enter_username);
        final EditText password = (EditText)findViewById(R.id.enter_password);

        // Implementación del botton 'login'
        final Button loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Log In !!\n" + username.getText() + "\n" + password.getText());
            }
        });

        // Implementación del comportamiento del texto 'sign up'
        final TextView signupText = (TextView)findViewById(R.id.text_signup);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Sign Up !! " + username.getText());
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("USERNAME", username.getText().toString());
                startActivity(intent);
            }
        });
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
