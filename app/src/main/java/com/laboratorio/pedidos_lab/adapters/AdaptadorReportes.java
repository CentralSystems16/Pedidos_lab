package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.main.ObtenerDetReporte;
import com.laboratorio.pedidos_lab.model.Reportes;
import com.laboratory.views.R;

import java.util.List;

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ReportesViewHolder> {

    Context cContext;
   public static List<Reportes> listaReportes;

    public AdaptadorReportes(Context cContext, List<Reportes> listaReportes) {

        this.cContext = cContext;
        AdaptadorReportes.listaReportes = listaReportes;

    }

    @NonNull
    @Override
    public ReportesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_reportes, viewGroup, false);
        return new ReportesViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ReportesViewHolder reportesViewHolder, int posicion) {

        reportesViewHolder.tvNombre.setText(listaReportes.get(posicion).getNombre());
        reportesViewHolder.tvFecha.setText(listaReportes.get(posicion).getFecha());
        reportesViewHolder.tvPedido.setText(Integer.toString(listaReportes.get(posicion).getPedido()));

        reportesViewHolder.itemView.setOnClickListener(v -> {

            Login.gIdPedido = listaReportes.get(posicion).getPedido();

            Intent i = new Intent(cContext, ObtenerDetReporte.class);
            cContext.startActivity(i);

            TicketDatos.gTotal = 0.00;

        });
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public static class ReportesViewHolder extends RecyclerView.ViewHolder {

   TextView tvNombre, tvFecha, tvPedido;

        public ReportesViewHolder(@NonNull View itemView) {
            super(itemView);

        tvNombre = itemView.findViewById(R.id.nombreCliente);
        tvFecha = itemView.findViewById(R.id.tvFecha);
        tvPedido = itemView.findViewById(R.id.numeroPedido);
        }
    }
}