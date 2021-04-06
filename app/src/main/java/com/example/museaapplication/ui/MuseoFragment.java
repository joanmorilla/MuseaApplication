package com.example.museaapplication.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MuseoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuseoFragment extends Fragment implements OnBackPressed {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_museo, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        TextView txtV = root.findViewById(R.id.text_view);
        Museo museum = sharedViewModel.getCurMuseo();
        super.getActivity().setTitle(museum.getName());
        setHasOptionsMenu(true);

        // Set the image we get from previous activity
        ImageView imageView = root.findViewById(R.id.image_holder);
        String url = museum.getImage();
        Picasso.get().load(url).fit().into(imageView);

        LinearLayout layout = root.findViewById(R.id.layout_view);

        // Generating the exposition buttons
        for (Exhibition e : museum.getExhibitionObjects()){
            // Layout for the whole button image
            RelativeLayout exhibitionLayout = new RelativeLayout(getContext());
            ImageButton imageButton = new ImageButton(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixToDp(180));
            // Defining margins and the click listener
            params.setMargins(pixToDp(10), 0, pixToDp(10), pixToDp(10));
            imageButton.setLayoutParams(params);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedViewModel.setCurExposition(e);
                    sharedViewModel.setActive(sharedViewModel.getmExpositionFragment());
                    FragmentManager fm = getParentFragmentManager();
                    fm.beginTransaction().hide(sharedViewModel.getmMuseoFragment()).show(sharedViewModel.getmExpositionFragment()).commit();
                }
            });
            // Setting the background image to that of the museum
            url = validateUrl(e.getImage());
            Picasso.get().load(url).fit().centerCrop().into(imageButton);
            // Bottom whit rectangle
            Button whiteRectangle = new Button(getContext());
            whiteRectangle.setText(e.getName());
            whiteRectangle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            whiteRectangle.setTextColor(Color.BLACK);
            whiteRectangle.setClickable(false);
            whiteRectangle.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            whiteRectangle.setBackgroundColor(Color.WHITE);
            whiteRectangle.setElevation(4);
            whiteRectangle.setPadding(0,0,0,0);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixToDp(45));
            p.setMargins(pixToDp(10), 0, pixToDp(10), pixToDp(20));
            p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imageButton.getId());
            whiteRectangle.setLayoutParams(p);
            // Adding all the views to the relative layout
            exhibitionLayout.addView(imageButton);
            exhibitionLayout.addView(whiteRectangle);
            // Setting margins of end result
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixToDp(225));
            newParams.setMargins(0,0,0,pixToDp(10));
            exhibitionLayout.setLayoutParams(newParams);

            layout.addView(exhibitionLayout);
        }

        TextView title = root.findViewById(R.id.museum_title);
        title.setText(museum.getName());
        TextView desc = root.findViewById(R.id.museum_description);
        desc.setText(museum.getDescriptions().getEn() + " " + museum.getDescriptions().getCa());
        // COVID 19 information
        ImageView img = root.findViewById(R.id.info_image);
        TextView covidText = root.findViewById(R.id.covid_text);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(root.getContext(), "La covid 19 es una merda", Toast.LENGTH_SHORT).show();
            }
        };

        img.setOnClickListener(clickListener);
        covidText.setOnClickListener(clickListener);




        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    void love() {
        loved = !loved;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                super.getActivity().finish();
                return true;
            case R.id.mybutton:
                love();
                if (loved) {
                    item.setIcon(R.drawable.filled_heart);
                }else {
                    item.setIcon(R.drawable.heart_empty);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private String validateUrl(String url){
        if (!url.contains("https")) url = url.replace("http", "https");
        return url;
    }
    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, root.getResources().getDisplayMetrics()));
    }

    @Override
    public boolean OnBack() {
        super.getActivity().finish();
        return false;
    }

}