package com.laboratorio.pedidos_lab.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.main.ObtenerProductos;
import com.laboratorio.pedidos_lab.model.Categorias;
import com.laboratory.views.R;

import java.text.DecimalFormat;
import java.util.List;

import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class AdaptadorCategorias  extends RecyclerView.Adapter<AdaptadorCategorias.CategoriaViewHolder> {

    Context cContext;
    List<Categorias> listaCategorias;

    public AdaptadorCategorias(Context cContext, List<Categorias> listaCategorias) {

        this.cContext = cContext;
        this.listaCategorias = listaCategorias;

    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_categorias, viewGroup, false);
        return new CategoriaViewHolder(v);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder, int posicion) {

        Categorias categorias = listaCategorias.get(posicion);

        categoriaViewHolder.tvCategorias.setText(listaCategorias.get(posicion).getNombreCategoria());
        Glide.with(cContext).load(categorias.getImgCategoria()).into(categoriaViewHolder.imageView);

        //Bacteriologia
        if (posicion == 0){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }
        //Combos
        if (posicion == 1){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0, 150,136)));
        }
        //Coprologia
        if (posicion == 2){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(139, 195,74)));
        }
        //Hematologia
        if (posicion == 3){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(121, 85,72)));
        }
        //Inmunologia
        if (posicion == 4){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0, 0,0)));
        }
        //Electrolitos
        if (posicion == 5){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(3, 169,244)));
        }
        //Especiales
        if (posicion == 6){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(96, 125,139)));
        }
        //Quimica
        if (posicion == 7){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 235,59)));
        }
        //Uroanalisis
        if (posicion == 8){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }
        //Tumoriales
        if (posicion == 9){
            categoriaViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(187, 134,252)));
        }

        categoriaViewHolder.itemView.setOnClickListener(v -> {

            ObtenerCategorias.gIdCategoria = listaCategorias.get(posicion).getIdCategoria();
            Intent i = new Intent(cContext, ObtenerProductos.class);
            cContext.startActivity(i);
            new ContadorProductos.GetDataFromServerIntoTextView(categoriaViewHolder.itemView.getContext()).execute();

        });
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategorias;
        ImageView imageView;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategorias = itemView.findViewById(R.id.tvNombreCat);
            imageView = itemView.findViewById(R.id.imgItem);

        }
    }
}