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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.museaapplication.Classes.Dominio.AfluenceDay;
import com.example.museaapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.argb;
import static android.graphics.Color.red;
import static androidx.core.content.ContextCompat.getSystemService;

public class CustomDialog extends AppCompatDialogFragment {
    BarChart barChart;
    AfluenceDay[] afluenceWeek;
    Context context;
    int openingHour;

    public CustomDialog(AfluenceDay[] ad, int openHour, Context c){
        afluenceWeek = ad;
        openingHour = openHour;
        context = c;
        createNotificationChannel();
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




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.covid_info_dialog, null);

        if (afluenceWeek != null) {
            barChart = view.findViewById(R.id.bar_graph_dialog);
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            ArrayList<String> theHours = new ArrayList<>();
            int j = 0;
            for (int i = 0; i < 24; i++) {
                if (afluenceWeek[TimeClass.getInstance().getToday()].getHours()[i] != 0) {
                    barEntries.add(new BarEntry(afluenceWeek[TimeClass.getInstance().getToday()].getHours()[i], j));
                    int temp = j + openingHour;
                    theHours.add("" + temp);
                    j++;
                }
            }
            BarDataSet dataSet = new BarDataSet(barEntries, "Afluence");
            dataSet.setBarSpacePercent(10);
            dataSet.setColors(new int[]{Color.argb(150, 16, 161, 201)});
            BarData theData = new BarData(theHours, dataSet);
            barChart.setData(theData);
            barChart.setTouchEnabled(false);
            barChart.setDragEnabled(false);
            barChart.setDrawGridBackground(false);
            barChart.setDrawValueAboveBar(false);
            barChart.setDescription("");
            barChart.setElevation(10);
        }

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
}
