package com.example.museaapplication.Classes;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.museaapplication.Classes.Dominio.AfluenceDay;
import com.example.museaapplication.Classes.Dominio.Info;
import com.example.museaapplication.Classes.Dominio.Restriction;
import com.example.museaapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.argb;
import static android.graphics.Color.red;
import static androidx.core.content.ContextCompat.getSystemService;

public class CustomDialog extends AppCompatDialogFragment {
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

    public CustomDialog(Info info, int openHour, Restriction[] res, Context c){
        afluenceWeek = info.getAfluence();
        this.info = info;
        openingHour = openHour;
        context = c;
        restrictionsObj = res;
        createNotificationChannel();
        //curColor = colors[random.nextInt(3)];
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.covid_info_dialog, null);

        TextView horari = view.findViewById(R.id.text_horari_value_covid_dialog);
        //if (TimeClass.getCurHour() < openingHour)
          //  horari.setText("Closed");

        horari.setText(cleanTimeStamp(info.getHorari()[TimeClass.getCurDay()]));

        // Restrictions
        TextView restrictions = view.findViewById(R.id.restrictions_covid_dialog);
        String text = "";
        for (Restriction res : restrictionsObj){
            text = text + "-" + res.getText().getText() + "\n\n";
        }
        restrictions.setText(text);

        // Bar chart
        if (afluenceWeek != null) {
            barChart = view.findViewById(R.id.bar_graph_dialog);
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            ArrayList<String> theHours = new ArrayList<>();
            int j = 0;
            for (int i = 0; i < 24; i++) {
                if (afluenceWeek[TimeClass.getInstance().getToday()].getHours()[i] != 0) {
                    if (start == 0) start = i;
                    barEntries.add(new BarEntry(afluenceWeek[TimeClass.getInstance().getToday()].getHours()[i], j));
                    int temp = j + openingHour;
                    theHours.add("" + temp);
                    j++;
                }
            }

            AfluenceDay day = afluenceWeek[TimeClass.getCurDay()];
            int index = TimeClass.getCurHour() - start - 1;
            if (index < 0) index = 0;
            int perc = day.getHours()[index];
            if (perc <= 40) curColor = colors[2];
            else if(perc > 40 && perc <= 70) curColor = colors[1];
            else curColor = colors[0];

            TextView icon = view.findViewById(R.id.color_indicator_dialog_covid);
            Drawable wrappedDrawable = DrawableCompat.wrap(icon.getBackground());
            DrawableCompat.setTint(wrappedDrawable, getResources().getColor(curColor));
            icon.setBackgroundDrawable(wrappedDrawable);

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


            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getActivity().getTheme();
            theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
            TypedArray arr = getActivity().obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorPrimary});
            int primaryColor = arr.getColor(0, -1);

            barChart.getLegend().setTextColor(primaryColor);
            barChart.getAxisLeft().setTextColor(primaryColor);
            barChart.getAxisRight().setTextColor(primaryColor);
            barChart.getXAxis().setTextColor(primaryColor);
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
    private String cleanTimeStamp(String time) {
        String res = "";
        String[] both = time.split(": ");
        res = both[1].trim();
        return res;
    }
}
