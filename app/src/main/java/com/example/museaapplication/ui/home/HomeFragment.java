package com.example.museaapplication.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Permissions;
import com.example.museaapplication.Classes.TimeClass;
import com.example.museaapplication.Classes.Vector2;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MainActivity;
import com.example.museaapplication.ui.MuseuActivity;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements Permissions {

    private HomeViewModel homeViewModel;
    boolean interactable = true;
    boolean created = false;
    String country = Locale.getDefault().getDisplayCountry();
    Museo[] museums;
    Geocoder geocoder;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        ProgressBar pb = (ProgressBar) root.findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        homeViewModel.getMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(Museo[] museos) {
                museums = museos;
                GenerarBotones(museos);
                pb.setVisibility(View.GONE);
            }

        });
        homeViewModel.getFavouriteMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @Override
            public void onChanged(Museo[] museos) {
                GenerateFavourites(museos);
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        interactable = true;
    }

    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, root.getResources().getDisplayMetrics()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void GenerarBotones(@NotNull Museo[] m) {
        LinearLayout scrollPais = root.findViewById(R.id.layout_pais);
        LinearLayout scrollPropers = root.findViewById(R.id.layout_close);
        boolean gpsEnabled = false;

        //country = Locale.getDefault().getDisplayCountry(Locale.forLanguageTag("en-EN"));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager manager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
            gpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 25, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.ENGLISH);
                    try {
                        List<Address> adreesses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //if (country != null && !country.equals(adreesses.get(0).getCountryName())){
                            country = adreesses.get(0).getCountryName();
                            scrollPais.removeAllViews();
                            scrollPropers.removeAllViews();
                            created = false;
                        //}

                        if (!created) {
                            for (int i = m.length - 1; i >= 0; i--) {
                                // For the complex button we use relative layout
                                RelativeLayout holder = new RelativeLayout(getContext());
                                View v = View.inflate(getContext(), R.layout.custom_button_layout, holder);
                                // Adding enter animation
                                YoYo.with(Techniques.ZoomIn)/*.delay((museums.length - i) * 200)*/.duration(700).playOn(v);
                                TextView txt = v.findViewById(R.id.title_text);
                                // Setting the texts in custom button
                                txt.setText(m[i].getName());
                                txt = v.findViewById(R.id.text_horari);
                                if (m[i].getCovidInformation() != null) {
                                    txt.setText(timeStringValidation(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                                    m[i].setOpeningHour(parseOpeningHour(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                                }
                                txt = v.findViewById(R.id.text_pais);
                                txt.setText(m[i].getCity());
                                ImageButton ib = v.findViewById(R.id.image_view);
                                ib.setOnClickListener(clickFunc(m[i]));
                                if (!m[i].getImage().equals(""))
                                    Picasso.get().load(m[i].getImage()).fit().centerCrop().into(ib);
                                // Size the relative layout
                                RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixToDp(155));
                                newParams.setMargins(pixToDp(5), 0, pixToDp(5), pixToDp(0));
                                holder.setLayoutParams(newParams);

                                if (m[i].getLocation() != null && m[i].getLocation().length != 0) {
                                    List<Address> addressList = geocoder.getFromLocation(m[i].getLocation()[0].getNumberDecimal(), m[i].getLocation()[1].getNumberDecimal(), 1);
                                    double musLat = addressList.get(0).getLatitude();
                                    double musLong = addressList.get(0).getLongitude();
                                    double myLat = adreesses.get(0).getLatitude();
                                    double myLong = adreesses.get(0).getLongitude();
                                    float[] results = new float[1];
                                    if (country != null && country.equals(addressList.get(0).getCountryName())) {
                                        scrollPais.addView(holder);
                                    }
                                    android.location.Location.distanceBetween(musLat, musLong, myLat, myLong, results);
                                    if (results[0] / 1000 <= 50) GenerateClose(m[i], adreesses);
                                }
                            }
                            created = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        Log.e("Country", country);
        if (!gpsEnabled) {
            // We go through the museums
            for (int i = m.length - 1; i >= 0; i--) {

                // For the complex button we use relative layout
                RelativeLayout holder = new RelativeLayout(getContext());
                View v = View.inflate(getContext(), R.layout.custom_button_layout, holder);
                // Adding enter animation
                YoYo.with(Techniques.ZoomIn).duration(700).playOn(v);
                TextView txt = v.findViewById(R.id.title_text);
                // Setting the texts in custom button
                txt.setText(m[i].getName());
                txt = v.findViewById(R.id.text_horari);
                if (m[i].getCovidInformation() != null) {
                    txt.setText(timeStringValidation(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                    m[i].setOpeningHour(parseOpeningHour(m[i].getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                }
                txt = v.findViewById(R.id.text_pais);
                txt.setText(m[i].getCity());
                ImageButton ib = v.findViewById(R.id.image_view);
                ib.setOnClickListener(clickFunc(m[i]));
                if (!m[i].getImage().equals(""))
                    Picasso.get().load(m[i].getImage()).fit().centerCrop().into(ib);
                // Size the relative layout
                RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixToDp(155));
                newParams.setMargins(pixToDp(5), 0, pixToDp(5), pixToDp(0));
                holder.setLayoutParams(newParams);
                // Finally add it to the scroll layout
                if (!m[i].getCountry().equals(country)) scrollPropers.addView(holder);
                else scrollPais.addView(holder);
            }
        }
    }

    private void GenerateClose(Museo m, List<Address> adreesses) {
        LinearLayout scrollPropers = root.findViewById(R.id.layout_close);
            // For the complex button we use relative layout
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = View.inflate(getContext(), R.layout.custom_button_layout, holder);
            // Adding enter animation
            YoYo.with(Techniques.ZoomIn)/*.delay((museums.length - i) * 200)*/.duration(700).playOn(v);
            TextView txt = v.findViewById(R.id.title_text);
            // Setting the texts in custom button
            txt.setText(m.getName());
            txt = v.findViewById(R.id.text_horari);
            if (m.getCovidInformation() != null) {
                txt.setText(timeStringValidation(m.getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                m.setOpeningHour(parseOpeningHour(m.getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
            }
            txt = v.findViewById(R.id.text_pais);
            txt.setText(m.getCity());
            ImageButton ib = v.findViewById(R.id.image_view);
            ib.setOnClickListener(clickFunc(m));
            if (!m.getImage().equals(""))
                Picasso.get().load(m.getImage()).fit().centerCrop().into(ib);
            // Size the relative layout
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixToDp(155));
            newParams.setMargins(pixToDp(5), 0, pixToDp(5), pixToDp(0));
            holder.setLayoutParams(newParams);
            // Finally add it to the scroll layout
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocation(m.getLocation()[0].getNumberDecimal(), m.getLocation()[1].getNumberDecimal(), 1);
                double musLat = addressList.get(0).getLatitude();
                double musLong = addressList.get(0).getLongitude();
                double myLat = adreesses.get(0).getLatitude();
                double myLong = adreesses.get(0).getLongitude();
                float[] results = new float[1];
                android.location.Location.distanceBetween(musLat, musLong, myLat, myLong, results);
                if (results[0]/1000 <= 50) scrollPropers.addView(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private double distance(Vector2 loc1, Vector2 loc2){
        double distX = Math.abs(loc1.getX() - loc2.getX());
        double distY = Math.abs(loc1.getY() - loc2.getY());
        double result = Math.sqrt((distX*distX) + (distY*distY));
        return result;
     }
    private void GenerateFavourites(@NotNull Museo[] m){
        LinearLayout scrollFavourites = root.findViewById(R.id.layout_favourites);
        scrollFavourites.removeAllViews();
        // We go through the museums
        for (Museo museo : m) {
            // For the complex button we use relative layout
            RelativeLayout holder = new RelativeLayout(getContext());
            View v = View.inflate(getContext(), R.layout.custom_button_layout, holder);
            // Adding enter animation
            YoYo.with(Techniques.ZoomIn)/*.delay((museums.length - i) * 200)*/.duration(700).playOn(v);
            TextView txt = v.findViewById(R.id.title_text);
            // Setting the texts in custom button
            txt.setText(museo.getName());
            txt = v.findViewById(R.id.text_horari);
            if (museo.getCovidInformation() != null) {
                txt.setText(timeStringValidation(museo.getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
                museo.setOpeningHour(parseOpeningHour(museo.getCovidInformation().getHorari()[TimeClass.getInstance().getToday()]));
            }
            txt = v.findViewById(R.id.text_pais);
            txt.setText(museo.getCity());
            ImageButton ib = v.findViewById(R.id.image_view);
            ib.setOnClickListener(clickFunc(museo));
            if (!museo.getImage().equals(""))
                Picasso.get().load(museo.getImage()).fit().centerCrop().into(ib);
            // Size the relative layout
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, pixToDp(155));
            newParams.setMargins(pixToDp(5), 0, pixToDp(5), pixToDp(0));
            holder.setLayoutParams(newParams);
            // Finally add it to the scroll layout
            scrollFavourites.addView(holder);
        }
    }
    private View.OnClickListener clickFunc(Museo m) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interactable) {
                    //String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(TimeClass.getNow());
                    Intent i = new Intent(getActivity(), MuseuActivity.class);
                    Uri uri = Uri.parse("/museums/" + m.get_id());
                    i.setData(uri);
                    MuseuActivity.curMuseum = m;
                    startActivityForResult(i, 1);
                    //startActivity(i);
                    getActivity().overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
                    interactable = false;
                }
            }
        };
    }
    private String timeStringValidation(String time){
        if (time.contains("Closed")) return "Closed";
        String myOpeningHour="", myClosingHour="", result;
        int index = time.indexOf(':');
        String myTime = time.substring(index).replace(": ", "").replace(" ", ""); // Now we have 10:30AM-8:00PM
        // We need to separate them into the two different hours
        String[] hours = myTime.split("–");
        // Validating both of them
        for (String hour : hours){
            if (hour.contains("PM")) {
                // We get the PM one and subdivide it in hours and minutes
                myClosingHour = hour.replace("PM", "");
                String[] h = myClosingHour.split(":");
                // We now have the numeric value of our hours
                int hs = Integer.parseInt(h[0].trim());
                hs+=12;
                myClosingHour = String.valueOf(hs) + ":" + h[1];
            }else myOpeningHour = hour.replace("AM", "");
        }
        // We then combine them
        result = myOpeningHour+"-"+myClosingHour;
        return result;
    }
    private int parseOpeningHour(String time){
        if (time.contains("Closed")) return -1;
        int index = time.indexOf(':');
        String myTime = time.substring(index).replace(": ", "").replace(" ", ""); // Now we have 10:30AM-8:00PM
        String[] hours = myTime.split("–");
        String temp = hours[0].replace("AM", ""); // 10:30
        String[] h = temp.split(":"); // [10, 30]
        return Integer.parseInt(h[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Entra", "wacho");
                    GenerarBotones(museums);
                }else Toast.makeText(getContext(), R.string.perm_denegado, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void Granted() {
        GenerarBotones(museums);
    }
}
