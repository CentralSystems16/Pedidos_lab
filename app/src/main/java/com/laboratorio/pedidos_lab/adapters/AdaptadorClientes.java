package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.front.Lugar;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.main.ObtenerClientes;
import com.laboratorio.pedidos_lab.model.Clientes;
import com.laboratory.views.R;
import java.util.List;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ClientesViewHolder> {

    Context cContext;
   public static List<Clientes> listaClientes;

    public AdaptadorClientes(Context cContext, List<Clientes> listaClientes) {

        this.cContext = cContext;
        AdaptadorClientes.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ClientesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_clientes, viewGroup, false);
        return new ClientesViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ClientesViewHolder clientesViewHolder, int posicion) {

        clientesViewHolder.tvNombre.setText(listaClientes.get(posicion).getNombre());

        clientesViewHolder.seleccionar.setOnClickListener(v -> {

            Login.gIdCliente = listaClientes.get(posicion).getIdCliente();

            Intent i = new Intent(cContext, Lugar.class);
            cContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public static class ClientesViewHolder extends RecyclerView.ViewHolder {

   TextView tvNombre;
   Button seleccionar, editar;

        public ClientesViewHolder(@NonNull View itemView) {
            super(itemView);

        tvNombre = itemView.findViewById(R.id.nombreClienteConsulta);
        seleccionar = itemView.findViewById(R.id.seleccionarCliente);
        editar = itemView.findViewById(R.id.editarCliente);

        }
    }
}