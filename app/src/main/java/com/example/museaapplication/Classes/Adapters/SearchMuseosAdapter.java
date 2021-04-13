package com.example.museaapplication.Classes.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museaapplication.Classes.Dominio.Museo;
import com.example.museaapplication.R;
import com.example.museaapplication.ui.MuseuActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class SearchMuseosAdapter extends RecyclerView.Adapter<SearchMuseosAdapter.MuseoViewHolder>{
    List<Museo> listaMuseos;

    public SearchMuseosAdapter(List<Museo> listaMuseos) {
        this.listaMuseos =listaMuseos;
    }

    @NonNull
    @Override
    public MuseoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new MuseoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MuseoViewHolder holder, int position) {
        if (!listaMuseos.isEmpty()) {
            holder.txtName.setText(listaMuseos.get(position).getName());
            holder.txtCountry.setText(listaMuseos.get(position).getCountry());
            holder.txtCity.setText(listaMuseos.get(position).getCity());
            Picasso.get().load(listaMuseos.get(position).getImage()).fit().centerCrop().into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(holder.itemView.getContext(), MuseuActivity.class);
                    i.putExtra("Museu", (Serializable) listaMuseos.get(position));
                    holder.itemView.getContext().startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaMuseos.size();
    }

    public class MuseoViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCountry, txtCity;
        ImageView img;

        public MuseoViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.idNombre);
            txtCountry = (TextView) itemView.findViewById(R.id.idPais);
            txtCity = (TextView) itemView.findViewById(R.id.idCiudad);
            img = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
