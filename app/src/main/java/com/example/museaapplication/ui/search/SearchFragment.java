package com.example.museaapplication.ui.search;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.museaapplication.Classes.Adapters.RecentSearchMuseosAdapter;
import com.example.museaapplication.Classes.Adapters.SearchMuseosAdapter;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MuseuActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchMuseosAdapter.RecyclerItemClick {

    private SearchViewModel searchViewModel;
    View root;
    Museo[] museoArr;

    List<Museo> museoList;
    RecyclerView recyclerMuseos;

    List<Museo> museoListRecent;
    RecyclerView recyclerMuseosRecent;
    int numberOfRecent = 4;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.search_fragment, container, false);
        searchViewModel = new SearchViewModel();

        museoList = new ArrayList<>();
        museoListRecent = new ArrayList<>();

        recyclerMuseosRecent= (RecyclerView) root.findViewById(R.id.recyclerRecentId);
        recyclerMuseosRecent.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        recyclerMuseos= (RecyclerView) root.findViewById(R.id.recyclerId);
        recyclerMuseos.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressBar pb = (ProgressBar) root.findViewById(R.id.progress_bar_search);
        pb.setVisibility(View.VISIBLE);

        searchViewModel.getMuseums().observe(getViewLifecycleOwner(), new Observer<Museo[]>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(Museo[] museos) {
                museoArr = new Museo[museos.length];
                museoArr = museos;
                rellenarLista("");
                rellenarListaRecientes();
                pb.setVisibility(View.GONE);
            }
        });
        museoArr = new Museo[0];
        rellenarLista("");
        rellenarListaRecientes();

        final SearchView searchView = root.findViewById(R.id.svSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rellenarLista(newText);
                return true;
            }
        });

        setHasOptionsMenu(false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        rellenarListaRecientes();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    private void rellenarLista(String text) {
        String lowerText = text.toLowerCase();
        museoList = new ArrayList<>();
        for (Museo m : museoArr) {
            if (m.getName().toLowerCase().contains(lowerText)) {
                museoList.add(m);
            }
            else if (m.getCity().toLowerCase().contains(lowerText)) {
                museoList.add(m);
            }
            else if (m.getCountry().toLowerCase().contains(lowerText)) {
                museoList.add(m);
            }
        }
        SearchMuseosAdapter adapter = new SearchMuseosAdapter(museoList, this);
        recyclerMuseos.setAdapter(adapter);
    }

    private void rellenarListaRecientes() {
        RecentSearchMuseosAdapter adapter = new RecentSearchMuseosAdapter(museoListRecent);
        recyclerMuseosRecent.setAdapter(adapter);
    }

    @Override
    public void itemClick(Museo museo) {

        // TODO: guardar esta lista en memoria para cuando se cierre la app

        museoListRecent.remove(museo);
        museoListRecent.add(0,museo);

        if (museoListRecent.size() >= numberOfRecent + 1)
            museoListRecent.remove(numberOfRecent);

        Intent i = new Intent(getContext(), MuseuActivity.class);
        i.putExtra("Museu", (Serializable) museo);
        getContext().startActivity(i);
    }
}