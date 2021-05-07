package com.example.museaapplication.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.museaapplication.Classes.CustomDialog;
import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MuseoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuseoFragment extends Fragment implements OnBackPressed {

    Museo museum;

    View root;
    boolean loved = false;
    SharedViewModel sharedViewModel;

    public MuseoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuseoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuseoFragment newInstance(String param1, String param2) {
        MuseoFragment fragment = new MuseoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_museo, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        //museum = sharedViewModel.getCurMuseo();

        ImageButton backArrow = root.findViewById(R.id.back_arrow);
        ImageButton heartButton = root.findViewById(R.id.heart_bttn);
        ImageButton shareButton = root.findViewById(R.id.share_bttn);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "http://www.musea.com/museums/"+ museum.get_id();
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.BounceInUp).duration(700).playOn(v);
                love();
                if (!loved) {
                    heartButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
                }else {
                    heartButton.setBackground(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
                }
            }
        });
        View.OnApplyWindowInsetsListener Listener = new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = insets.getSystemWindowInsetTop();
                return insets;
            }
        };

        View view = root.findViewById(R.id.toolbar_layout);
        view.setOnApplyWindowInsetsListener(Listener);



        TextView txtV = root.findViewById(R.id.text_view);

        // Set the image we get from previous activity
        ImageView imageView = root.findViewById(R.id.image_holder);

        // Creating the view
        sharedViewModel.getMuseum().observe(getViewLifecycleOwner(), new Observer<Museo>() {
            @Override
            public void onChanged(Museo museo) {
                museum = museo;
                String url = museum.getImage();
                if (!url.equals(""))
                    Picasso.get().load(url).placeholder(R.drawable.catalonia).fit().into(imageView);

                LinearLayout layout = root.findViewById(R.id.layout_view);
                layout.removeAllViews();

                // Generating the exposition buttons
                for (Exhibition e : museum.getExhibitionObjects()){
                    RelativeLayout holder = new RelativeLayout(getContext());
                    View v = RelativeLayout.inflate(getContext(), R.layout.custom_exposition_button, holder);
                    ImageButton ib = v.findViewById(R.id.image_button_expo);
                    Picasso.get().load(validateUrl(e.getImage())).fit().centerCrop().into(ib);
                    TextView expoTitle = v.findViewById(R.id.white_rectangle_expo);
                    expoTitle.setText(e.getName());
                    Log.d("Setting Stuff", "it works");

                    View.OnClickListener clickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharedViewModel.setCurExposition(e);
                            sharedViewModel.setActive(sharedViewModel.getmExpositionFragment());
                            FragmentManager fm = getParentFragmentManager();
                            fm.beginTransaction().hide(sharedViewModel.getmMuseoFragment()).show(sharedViewModel.getmExpositionFragment()).commit();
                        }
                    };

                    ib.setOnClickListener(clickListener);
                    expoTitle.setOnClickListener(clickListener);

                    layout.addView(holder);
                }

                TextView title = root.findViewById(R.id.museum_title);
                title.setText(museum.getName());
                TextView desc = root.findViewById(R.id.museum_description);
                desc.setText(museum.getDescriptions().getText());
                // COVID 19 information
                ImageView img = root.findViewById(R.id.info_image);
                TextView covidText = root.findViewById(R.id.covid_text);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        openDialog();
                    }
                };

                img.setOnClickListener(clickListener);
                covidText.setOnClickListener(clickListener);
            }
        });


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openDialog() {
        //CustomDialog dialog = new CustomDialog(museum.getCovidInformation().getAfluence(), museum.getOpeningHour());
        CustomDialog dialog = new CustomDialog(museum.getCovidInformation(), museum.getOpeningHour(), museum.getRestrictions(), getContext());
        //CustomDialog dialog = new CustomDialog(null, 0, museum.getRestrictions(), getContext()); // For testing
        dialog.show(getChildFragmentManager(), "Informacio");
        Calendar calendar = Calendar.getInstance();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    void love() {
        loved = !loved;
    }
    private String validateUrl(String url){
        if (!url.contains("https")) url = url.replace("http", "https");
        return url;
    }
    @Override
    public boolean OnBack() {
        super.getActivity().finish();
        return false;
    }

}