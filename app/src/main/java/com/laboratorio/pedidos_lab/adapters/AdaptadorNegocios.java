package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;
import com.laboratorio.pedidos_lab.main.ObtenerNegocios;
import com.laboratorio.pedidos_lab.model.Negocios;
import com.laboratory.views.R;

import java.util.List;

public class AdaptadorNegocios extends RecyclerView.Adapter<AdaptadorNegocios.NegociosViewHolder> {

    Context cContext;
   public static List<Negocios> listaNegocios;
   ObtenerNegocios obtenerNegocios;

    public AdaptadorNegocios(Context cContext, List<Negocios> listaNegocios) {

        this.cContext = cContext;
        AdaptadorNegocios.listaNegocios = listaNegocios;
    }

    @NonNull
    @Override
    public NegociosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_negocios, viewGroup, false);
        return new NegociosViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull NegociosViewHolder negociosViewHolder, int posicion) {

        Negocios negocios = listaNegocios.get(posicion);

        negociosViewHolder.tvNegocio.setText(listaNegocios.get(posicion).getNombre());
        Glide.with(cContext).load(negocios.getImagen()).into(negociosViewHolder.imgNegocio);


        if (posicion == 0){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(121, 200,67)));
        }

        if (posicion == 1){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(226, 109,194)));
        }

        if (posicion == 2){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(82, 175,221)));
        }

        if (posicion == 3){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 4){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 5){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 6){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 7){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 8){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 9){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        negociosViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtener el id del negocio
                ObtenerNegocios.idNegocio = listaNegocios.get(posicion).getIdNegocio();

                //Intencion al splash dependiendo del id del negocio
                cContext.startActivity(new Intent(cContext, SplashPrincipal.class));

                System.out.println("El id de negocio es: " + ObtenerNegocios.idNegocio);

            }
        });
    }


    @Override
    public int getItemCount() {
        return listaNegocios.size();
    }

    public static class NegociosViewHolder extends RecyclerView.ViewHolder {

        ImageView imgNegocio;
        TextView tvNegocio;

        public NegociosViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNegocio = itemView.findViewById(R.id.imgNegocio);
            tvNegocio = itemView.findViewById(R.id.nombreNegocio);


        }
    }
}