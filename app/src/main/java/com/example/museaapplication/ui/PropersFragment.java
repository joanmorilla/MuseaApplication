package com.example.museaapplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.Classes.Json.MuseoValue;
import com.example.museaapplication.Classes.RetrofitClient;
import com.example.museaapplication.Classes.SingletonDataHolder;
import com.example.museaapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropersFragment extends Fragment {

    private boolean interactable = true;
    View root;

    String[] strings;
    Museo[] museus;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PropersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PropersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PropersFragment newInstance(String param1, String param2) {
        PropersFragment fragment = new PropersFragment();
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
    public void onResume() {
        super.onResume();
        interactable = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_propers, container, false);
        getMuseums();
        /*APIRequests.getInstance().getAllMuseums(new Delegate() {
            @Override
            public void Execute() {
                GenerarBotones();
            }
        });*/
        return root;
    }


    int pixToDp(int value){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, root.getResources().getDisplayMetrics()));
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void getMuseums(){
        Call<MuseoValue> call = RetrofitClient.getInstance().getMyApi().getMuseums();
        call.enqueue(new Callback<MuseoValue>() {
            @Override
            public void onResponse(Call<MuseoValue> call, Response<MuseoValue> response) {
                MuseoValue mymuseumList = response.body();
                Museo[] museums = mymuseumList.getMuseums();
                strings = new String[museums.length];
                museus = new Museo[museums.length];
                for (int i = 0; i < museums.length; i++){
                    strings[i] = museums[i].getName();
                    museus[i] = museums[i];
                    Log.d("museos",museums[i].getName());
                }
                GenerarBotones();
                //Toast.makeText(getContext(), strings[1], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MuseoValue> call, Throwable t) {
                //Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                Log.e("TAG1", t.getLocalizedMessage());
                Log.e("TAG2", t.getMessage());
                t.printStackTrace();
                getMuseums();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void GenerarBotones() {
        LinearLayout scrollPais = root.findViewById(R.id.scroll_layout);
        for(int i = strings.length - 1; i >= 0; i--){
            Button b = new Button(scrollPais.getContext());
            Button b2 = new Button(b.getContext());

            RelativeLayout relativeLayout = new RelativeLayout(scrollPais.getContext());
            b2.setBackground(root.getResources().getDrawable(R.drawable.heart_empty));

            RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(pixToDp(20), pixToDp(20));
            param2.alignWithParent = true;
            param2.leftMargin = pixToDp(200);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixToDp(200));
            param.setMargins(pixToDp(5), pixToDp(10), pixToDp(5), 0);
            b.setLayoutParams(param);
            b.setBackground(root.getResources().getDrawable(R.drawable.mnac_default));
            b.setElevation(pixToDp(4));

            b.setOnClickListener(clickFunc(i));
            relativeLayout.addView(b);
            relativeLayout.addView(b2);
            scrollPais.addView(relativeLayout);
        }
    }
    View.OnClickListener clickFunc(int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interactable) {
                    Intent i = new Intent(getContext(), MuseuActivity.class);
                    SingletonDataHolder.getInstance().setCodedImage(imageToString(R.drawable.mnac_default));
                    i.putExtra("Name", strings[index]);
                    Bundle bundle = new Bundle();
                    i.putExtra("Museu", (Serializable)museus[index]);
                    startActivity(i);
                    interactable = false;
                }
            }
        };
    }
}