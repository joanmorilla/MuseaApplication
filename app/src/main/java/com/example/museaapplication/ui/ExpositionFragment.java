package com.example.museaapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.museaapplication.Classes.Dominio.Exhibition;
import com.example.museaapplication.Classes.OnBackPressed;
import com.example.museaapplication.Classes.ViewModels.SharedViewModel;
import com.example.museaapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpositionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpositionFragment extends Fragment implements OnBackPressed {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedViewModel sharedViewModel;

    public ExpositionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpositionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpositionFragment newInstance(String param1, String param2) {
        ExpositionFragment fragment = new ExpositionFragment();
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
        setHasOptionsMenu(true);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getCurExposition().observe(getViewLifecycleOwner(), new Observer<Exhibition>() {
            @Override
            public void onChanged(Exhibition exhibition) {
                getActivity().setTitle(exhibition.getName());
            }
        });

        /*if (sharedViewModel.getCurExposition() != null)
            super.getActivity().setTitle(sharedViewModel.getCurExposition().getName());*/
        View root = inflater.inflate(R.layout.fragment_exposition, container, false);
        return root;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                getParentFragmentManager().beginTransaction().hide(sharedViewModel.getmExpositionFragment()).show(sharedViewModel.getmMuseoFragment()).commit();
                getActivity().setTitle(sharedViewModel.getCurMuseo().getName());
                sharedViewModel.setActive(sharedViewModel.getmMuseoFragment());
                return true;
            case R.id.mybutton:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean OnBack() {
        getParentFragmentManager().beginTransaction().hide(sharedViewModel.getmExpositionFragment()).show(sharedViewModel.getmMuseoFragment()).commit();
        getActivity().setTitle(sharedViewModel.getCurMuseo().getName());
        sharedViewModel.setActive(sharedViewModel.getmMuseoFragment());
        return true;
    }
}