package com.example.museaapplication.Classes.Adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museaapplication.Classes.Dominio.Favourites;
import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Favourite_museosAdapter extends RecyclerView.Adapter<Favourite_museosAdapter.MuseoViewHolder>{
    List<Favourites> listafavourites;


    public Favourite_museosAdapter(List<Favourites> listafav) {
        this.listafavourites = listafav;
    }

    @NonNull
    @Override
    public MuseoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visited,parent,false);
        return new MuseoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuseoViewHolder holder, int position) {
        if (!listafavourites.isEmpty()) {
            final Favourites favourites = listafavourites.get(position);
            holder.txtName.setText(favourites.museumId().toString());
            holder.txtCountry.setText("");
            holder.txtCity.setText("");
            Picasso.get().load(favourites.image()).fit().centerCrop().into(holder.img);

        }
    }


     @Override
    public int getItemCount() {
        return listafavourites.size();
    }

    public class MuseoViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCountry, txtCity;
        ImageView img;

        public MuseoViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.idNombre);
            txtCountry = (TextView) itemView.findViewById(R.id.idPais);
            txtCity = (TextView) itemView.findViewById(R.id.idCiudad);
            img = (ImageView) itemView.findViewById(R.id.id_image);
        }
    }

    public interface RecyclerItemClick {
        void itemClick(Museo museo);
    }


}
