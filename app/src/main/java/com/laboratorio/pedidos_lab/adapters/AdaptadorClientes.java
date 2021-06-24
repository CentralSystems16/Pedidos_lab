package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.main.ObtenerClientes;
import com.laboratorio.pedidos_lab.main.ObtenerDetReporte;
import com.laboratorio.pedidos_lab.model.Clientes;
import com.laboratorio.pedidos_lab.model.Reportes;
import com.laboratory.views.R;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

        if (posicion == 0){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 1){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0, 150,136)));
        }

        if (posicion == 2){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(139, 195,74)));
        }

        if (posicion == 3){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(121, 85,72)));
        }

        if (posicion == 4){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0, 0,0)));
        }

        if (posicion == 5){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(3, 169,244)));
        }

        if (posicion == 6){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(96, 125,139)));
        }

        if (posicion == 7){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 235,59)));
        }

        if (posicion == 8){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 9){
            clientesViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(187, 134,252)));
        }

        clientesViewHolder.seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(cContext, ObtenerCategorias.class);
                cContext.startActivity(i);
            }
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