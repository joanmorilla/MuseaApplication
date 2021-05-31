package com.example.museaapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setTitle("Add payment method");
        UserInfo loggedUserInfo = SingletonDataHolder.getInstance().getLoggedUser();

        TextView name = findViewById(R.id.pay_name);
        name.setText(loggedUserInfo.getName());

        Button payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<Void> call = RetrofitClient.getInstance().getMyApi().updatePremium(loggedUserInfo.getUserId(),"365");
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case 200:
                                Log.d("Update Premium:","Premium updated");
                                break;
                            case 404:
                                Log.d("Update Premium:","There is no user for such id");
                                break;
                            default:
                                Log.d("Update Premium:","Something unexpected happened");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TAG1", t.getLocalizedMessage());
                        Log.e("TAG2", t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        });

        ImageView logo = findViewById(R.id.pay_logo);

        CardView visa = findViewById(R.id.pay_method_visa);
        visa.setClickable(true);
        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setBackgroundResource(R.drawable.image_pay_visa);
            }
        });

        CardView master = findViewById(R.id.pay_method_master);
        master.setClickable(true);
        master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setBackgroundResource(R.drawable.image_pay_mastercard);
            }
        });

        CardView paypal = findViewById(R.id.pay_method_paypal);
        paypal.setClickable(true);
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.setBackgroundResource(R.drawable.image_pay_paypal);
            }
        });

    }
}