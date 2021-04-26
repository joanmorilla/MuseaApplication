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

public class RecentSearchMuseosAdapter extends RecyclerView.Adapter<RecentSearchMuseosAdapter.RecentMuseoViewHolder> {
    List<Museo> listaMuseosRecientes;

    public RecentSearchMuseosAdapter(List<Museo> listaMuseosRecientes) {
        this.listaMuseosRecientes =listaMuseosRecientes;
    }


    @NonNull
    @Override
    public RecentMuseoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_recent_search,parent,false);
        return new RecentSearchMuseosAdapter.RecentMuseoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentMuseoViewHolder holder, int position) {
        if (!listaMuseosRecientes.isEmpty()) {
            holder.txtName.setText(listaMuseosRecientes.get(position).getName());
            Picasso.get().load(listaMuseosRecientes.get(position).getImage()).fit().centerCrop().into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(holder.itemView.getContext(), MuseuActivity.class);
                    i.putExtra("Museu", (Serializable) listaMuseosRecientes.get(position));
                    holder.itemView.getContext().startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaMuseosRecientes.size();
    }

    public class RecentMuseoViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView img;

        public RecentMuseoViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.idNombreReciente);
            img = (ImageView) itemView.findViewById(R.id.idImagenReciente);
        }
    }
}
