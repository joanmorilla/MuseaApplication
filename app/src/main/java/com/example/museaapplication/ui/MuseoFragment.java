package com.example.museaapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

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

        for (Exhibition e : museum.getExhibitionObjects()){
            ImageButton imageButton = new ImageButton(getContext());
            url = validateUrl(e.getImage());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixToDp(300));
            params.setMargins(pixToDp(10), 0, pixToDp(10), pixToDp(10));
            imageButton.setLayoutParams(params);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Expo", "" + e);
                    sharedViewModel.setCurExposition(e);
                    sharedViewModel.setActive(sharedViewModel.getmExpositionFragment());
                    FragmentManager fm = getParentFragmentManager();
                    fm.beginTransaction().hide(sharedViewModel.getmMuseoFragment()).show(sharedViewModel.getmExpositionFragment()).commit();
                }
            });
            layout.addView(imageButton);
            Picasso.get().load(url).fit().centerCrop().into(imageButton);
        }
        txtV.setText(museum.getCountry() + "\n"
                +    museum.getCity() + "\n"
                +    museum.getAddress() + "\n"
                +    museum.getDescriptions().getEn());

        /*txtV.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas mattis velit felis, lobortis euismod sem iaculis in. Aenean imperdiet congue consectetur. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eleifend ipsum a enim porta, et mattis metus bibendum. Quisque eu ullamcorper ligula. Nunc dictum velit sit amet nunc suscipit, id sagittis dolor mattis. Proin in eros sodales, tincidunt justo vitae, consectetur ante. In quis libero leo. Cras consectetur sit amet eros eu sagittis. Duis metus sem, tempus id nulla vitae, convallis pretium tortor. Quisque varius tincidunt nisi, vel pulvinar nisl posuere sit amet. Quisque tortor neque, pretium ut varius quis, volutpat nec ipsum. Nam ultrices commodo ultricies. Aliquam rutrum id tellus eu placerat. Cras vehicula orci in facilisis condimentum.\n" +
                "\n" +
                "Proin ultrices augue molestie velit tincidunt, vitae bibendum metus vestibulum. Nullam ac odio congue, eleifend ex quis, ultricies purus. Vivamus ornare libero vitae facilisis sagittis. Sed gravida maximus libero, ut eleifend ante iaculis et. Phasellus accumsan id augue ac rhoncus. Integer non porttitor odio. Praesent rhoncus et sapien sed mollis.\n" +
                "\n" +
                "Vestibulum diam massa, dictum nec vehicula sit amet, tincidunt ut purus. Suspendisse vestibulum arcu velit, vel mattis velit cursus vitae. Donec varius viverra risus, congue dictum diam iaculis sed. Nulla tempus rhoncus ex, eget venenatis justo accumsan vestibulum. Donec tristique mi elit, nec mattis elit ultrices quis. Integer tempor tellus nec justo congue, in sollicitudin neque viverra. Duis blandit dui arcu, in rhoncus mauris maximus vitae. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.\n" +
                "\n" +
                "Cras dictum nulla sed dui mattis, eu interdum nisi ultrices. Pellentesque a purus nibh. Nunc ultricies erat at nibh eleifend placerat. Maecenas laoreet sodales arcu quis sagittis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum eu mauris vitae dui euismod hendrerit sed eu orci. Cras libero libero, pharetra vitae imperdiet id, iaculis sit amet odio. Pellentesque ornare convallis leo. Morbi hendrerit dolor non diam euismod, vel facilisis nisl euismod. Curabitur viverra dolor at cursus efficitur. Cras et maximus risus. Praesent eu felis nisi. In ligula nisi, mollis in nunc ut, scelerisque volutpat arcu. Vivamus malesuada molestie dui, eu commodo est.\n" +
                "\n" +
                "Maecenas bibendum diam et tellus facilisis semper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Etiam ut vulputate lorem. Mauris eu massa at elit consequat pellentesque. Ut sit amet faucibus dolor. Phasellus faucibus finibus diam, vel porttitor justo. Proin in justo sagittis dolor ullamcorper dictum in sit amet elit. Nulla justo nibh, dapibus id commodo vel, accumsan non nisl. Donec sollicitudin sapien justo, eu facilisis velit bibendum nec.");
*/
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