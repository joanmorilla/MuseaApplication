package com.example.museaapplication.Classes;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import com.example.museaapplication.Classes.Dominio.AfluenceDay;
import com.example.museaapplication.Classes.Dominio.Info;
import com.example.museaapplication.Classes.Dominio.Restriction;
import com.example.museaapplication.Classes.Dominio.UserInfo;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.user.UserViewModel;
import com.github.mikephil.charting.charts.BarChart;

import java.text.SimpleDateFormat;
import java.util.Random;

import static android.graphics.Color.argb;
import static androidx.core.content.ContextCompat.getSystemService;

public class ShopDialog extends AppCompatDialogFragment {
    BarChart barChart;
    Info info;
    AfluenceDay[] afluenceWeek;
    Restriction[] restrictionsObj;
    Context context;
    int openingHour;
    Random random = new Random();
    int[] colors = {R.color.red_alert, R.color.yellow, R.color.green};
    int curColor = colors[0];
    private int start = 0;
    private UserViewModel uvm;

    public ShopDialog(Context c, UserViewModel uvm){
        context = c;
        this.uvm = uvm;
        createNotificationChannel();
        //curColor = colors[random.nextInt(3)];
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getActivity(), "MyChannel")
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.mnac_default))
                .setContentTitle("Test notification")
                .setColor(argb(150, 30, 50, 255))
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much much much much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, notBuilder.build());

        this.uvm.resetFinishBuy();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.shop_dialog, null);

        TextView points = view.findViewById(R.id.points_counter_number);
        Log.e("Shop",String.valueOf(uvm.getinfoUser().getValue().getPoints()));
        points.setText(String.valueOf(uvm.getinfoUser().getValue().getPoints()));

        TextView premiumDate = view.findViewById(R.id.premium_date_value);
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(uvm.getinfoUser().getValue().getPremiumDate());
        premiumDate.setText(date);

        ImageView product1 = view.findViewById(R.id.image_holder_shop);
        ImageView product2 = view.findViewById(R.id.image_holder_shop2);
        ImageView product3 = view.findViewById(R.id.image_holder_shop3);

        product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uvm.getinfoUser().getValue().getPoints() < 100) {
                    Toast.makeText(context, "You don't have enough points", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Set a title for alert dialog
                    builder.setTitle("MUSEA+ (1 day).");

                    // Ask the final question
                    builder.setMessage("Are you sure? (100)");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when user clicked the Yes button
                            // Set the TextView visibility GONE
                            uvm.spendPoints(100);
                            uvm.addPremiumMembership(1);
                            uvm.loadUsersinfo();
                            Log.e("User_Points", String.valueOf(uvm.getinfoUser().getValue().getPoints()));
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked

                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();

                }
            }
        });

        product2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uvm.getinfoUser().getValue().getPoints() < 1000) {
                    Toast.makeText(context, "You don't have enough points", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Set a title for alert dialog
                    builder.setTitle("MUSEA+ (1 month).");

                    // Ask the final question
                    builder.setMessage("Are you sure? (1000)");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when user clicked the Yes button
                            // Set the TextView visibility GONE
                            uvm.spendPoints(1000);
                            uvm.addPremiumMembership(30);
                            uvm.loadUsersinfo();
                            Log.e("User_Points", String.valueOf(uvm.getinfoUser().getValue().getPoints()));
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked

                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();

                }
            }
        });

        product3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uvm.getinfoUser().getValue().getPoints() < 10000) {
                    Toast.makeText(context, "You don't have enough points", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Set a title for alert dialog
                    builder.setTitle("MUSEA+ (1 year).");

                    // Ask the final question
                    builder.setMessage("Are you sure? (10000)");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when user clicked the Yes button
                            // Set the TextView visibility GONE
                            uvm.spendPoints(10000);
                            uvm.addPremiumMembership(365);
                            uvm.loadUsersinfo();
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked

                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                }
            }
        });

        uvm.getFinishBuy().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == "OK") {
                    Toast.makeText(context, "Compra Hecha!", Toast.LENGTH_SHORT).show();
                }
                else if (s == "NOT_OK"){
                    Toast.makeText(context, "Error en la compra", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uvm.getinfoUser().observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo ut) {
                points.setText(String.valueOf(uvm.getinfoUser().getValue().getPoints()));
                String pattern = "MM-dd-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(uvm.getinfoUser().getValue().getPremiumDate());
                premiumDate.setText(date);
            }
        });


        //if (TimeClass.getCurHour() < openingHour)
        //  horari.setText("Closed");




        // Bar chart

        builder.setView(view);

        Dialog d = builder.create();
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return d;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "A channel";
            String description = "Just a channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private String cleanTimeStamp(String time) {
        String res = "";
        String[] both = time.split(": ");
        res = both[1].trim();
        return res;
    }
}
