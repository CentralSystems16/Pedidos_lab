package com.laboratorio.pedidos_lab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.model.Correos;
import com.laboratorio.pedidos_lab.model.Opciones;
import com.laboratory.views.R;

import java.util.List;

public class AdaptadorCorreos extends RecyclerView.Adapter<AdaptadorCorreos.OpcionesViewHolder>{

    Context context;
    List<Correos> listaCorreos;

    public AdaptadorCorreos(Context context, List<Correos> listaCorreos) {
        this.context = context;
        this.listaCorreos = listaCorreos;

    }
    @NonNull
    @Override
    public OpcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_opciones, parent, false);
        return new OpcionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OpcionesViewHolder holder, int position) {

        TicketDatos.envCorreo = listaCorreos.get(position).getDireccion();
    }

    @Override
    public int getItemCount() {
       return listaCorreos.size();

    }

    public static class OpcionesViewHolder extends RecyclerView.ViewHolder{

        public OpcionesViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
