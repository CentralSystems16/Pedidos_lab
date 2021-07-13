package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.laboratorio.pedidos_lab.controler.ActualizarCliente;
import com.laboratorio.pedidos_lab.front.Lugar;
import com.laboratorio.pedidos_lab.main.ObtenerClientes;
import com.laboratorio.pedidos_lab.manage.ModificarCliente;
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
        ObtenerClientes.nacimientoCliente = listaClientes.get(posicion).getNacimiento();
        ObtenerClientes.direccionCliente = listaClientes.get(posicion).getDireccion();
        ObtenerClientes.edadCliente = listaClientes.get(posicion).getEdad();
        ObtenerClientes.mesesCliente = listaClientes.get(posicion).getMeses();
        ObtenerClientes.emailCliente = listaClientes.get(posicion).getEmail();

        clientesViewHolder.seleccionar.setOnClickListener(v -> {

            Login.gIdCliente = listaClientes.get(posicion).getIdCliente();
            cContext.startActivity(new Intent(cContext, Lugar.class));

        });

        clientesViewHolder.editar.setOnClickListener(v -> {

            Login.gIdCliente = listaClientes.get(posicion).getIdCliente();
            cContext.startActivity(new Intent(cContext, ModificarCliente.class));

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