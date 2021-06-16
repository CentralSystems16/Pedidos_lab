package com.laboratorio.pedidos_lab.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.laboratorio.pedidos_lab.model.Opciones;
import com.laboratory.views.R;
import java.util.List;

public class AdaptadorOpciones extends RecyclerView.Adapter<AdaptadorOpciones.OpcionesViewHolder>{

    Context context;
    List<Opciones> listaUsuarios;

    public AdaptadorOpciones(Context context, List<Opciones> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;

    }
    @NonNull
    @Override
    public OpcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_opciones, parent, false);
        return new OpcionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OpcionesViewHolder holder, int position) {

        holder.nombreOpcion.setText(listaUsuarios.get(position).getNombreOpcione());
    }

    @Override
    public int getItemCount() {
       return listaUsuarios.size();

    }

    public static class OpcionesViewHolder extends RecyclerView.ViewHolder{

        TextView nombreOpcion;

        public OpcionesViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreOpcion = itemView.findViewById(R.id.tvOpcion);
        }
    }
}
