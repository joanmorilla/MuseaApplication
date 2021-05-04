package com.example.museaapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Comentaris_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Comentaris_Fragment extends Fragment implements OnBackPressed {
    SharedViewModel sharedViewModel;

    public Comentaris_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comentaris_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Comentaris_Fragment newInstance(String param1, String param2) {
        Comentaris_Fragment fragment = new Comentaris_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comentaris, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        ImageButton backArrow = root.findViewById(R.id.back_arrow_comments);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBack();
            }
        });
        return root;
    }

    @Override
    public boolean OnBack() {
        getParentFragmentManager().beginTransaction().hide(sharedViewModel.getmCommentsFragment()).show(sharedViewModel.getmExpositionFragment()).commit();
        sharedViewModel.setActive(sharedViewModel.getmExpositionFragment());
        return true;
    }
}