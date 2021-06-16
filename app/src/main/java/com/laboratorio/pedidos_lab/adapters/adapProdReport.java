package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.controler.ActualizarDetPedido;
import com.laboratorio.pedidos_lab.controler.ActualizarPedido;
import com.laboratorio.pedidos_lab.controler.EliminarDetPedido;
import com.laboratorio.pedidos_lab.model.DetReporte;
import com.laboratory.views.R;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class adapProdReport extends RecyclerView.Adapter<adapProdReport.ProdReportViewHolder>{

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static List<DetReporte> listaProdReport;
    public static int lidDetPedido = 0;
    public static double lCantProducto = 0, lNewCantProducto = 0;
    public static Double lPrecioVta = 0.00, lDetMonto = 0.00, lDetMontoIva = 0.00, lDetMontoFinal = 0.00, lNewDetMonto = 0.00, lNewDetMontoIva = 0.00, lNewDetMontoFinal = 0.00,
            monto = 0.00, montoIva = 0.00;
    public static String lNombreProducto;
    DecimalFormat formatoDecimal = new DecimalFormat("#.00");

    public adapProdReport(Context context, List<DetReporte> listaProdReport) {
        adapProdReport.context = context;
        adapProdReport.listaProdReport = listaProdReport;
    }

    @NonNull
    @Override
    public ProdReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_productos_reporte, parent, false);
        return new ProdReportViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProdReportViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        holder.tvNombre.setText(listaProdReport.get(position).getNombreProducto());
        lDetMonto = listaProdReport.get(position).getMonto();
        lDetMontoIva = listaProdReport.get(position).getMontoIva();
        lPrecioVta = listaProdReport.get(position).getPrecioVenta();
        lNombreProducto = listaProdReport.get(position).getNombreProducto();

        holder.tvPrecio.setText(formatoDecimal.format(lPrecioVta));

        lCantProducto = listaProdReport.get(position).getCantiProd();
        holder.tvCantidad.setText(Double.toString(lCantProducto));
        lDetMontoFinal = lDetMonto + lDetMontoIva;
        holder.totalItem.setText(formatoDecimal.format(lDetMontoFinal));

        if(lCantProducto == 1.0){
            holder.btnMenos.setEnabled(false);
        }

        holder.btnMas.setOnClickListener(v -> {
            holder.btnMenos.setEnabled(true);
            double counter = Double.parseDouble(holder.tvCantidad.getText().toString());
            ++counter;
            holder.tvCantidad.setText(String.valueOf(counter));

            lidDetPedido = listaProdReport.get(position).getIdDetPedido();
            lDetMonto = listaProdReport.get(position).getMonto();
            lDetMontoIva = listaProdReport.get(position).getMontoIva();
            lPrecioVta = listaProdReport.get(position).getPrecioVenta();

            lNewCantProducto = counter;

            lNewDetMontoFinal = lPrecioVta * lNewCantProducto;

            lNewDetMonto = lNewDetMontoFinal / 1.13;

            lNewDetMontoIva = lNewDetMonto * 0.13;

            holder.totalItem.setText(formatoDecimal.format(lNewDetMontoFinal));

            Double newSubTotal = TicketDatos.gTotal + lPrecioVta;
            TicketDatos.gTotal = TicketDatos.gTotal + lPrecioVta;

            TicketDatos.subTotalReporte.setText(formatoDecimal.format(newSubTotal));
            TicketDatos.totalFinal.setText(formatoDecimal.format(newSubTotal));

            monto = newSubTotal / 1.13;
            montoIva = monto * 0.13;

            try {
                new ActualizarDetPedido(holder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            try {
                new ActualizarPedido(holder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(context, TicketDatos.class);
            context.startActivity(i);
            TicketDatos.gTotal = 0.00;

        });

        holder.btnMenos.setOnClickListener(v -> {

            double counter = Double.parseDouble(holder.tvCantidad.getText().toString());
            if (counter > 1.0) {
                --counter;
                holder.tvCantidad.setText(String.valueOf(counter));
            }
            if (counter == 1.0){
                holder.btnMenos.setEnabled(false);
            }

            lidDetPedido = listaProdReport.get(position).getIdDetPedido();
            lDetMonto = listaProdReport.get(position).getMonto();
            lDetMontoIva = listaProdReport.get(position).getMontoIva();
            lPrecioVta = listaProdReport.get(position).getPrecioVenta();

            lNewCantProducto = counter;

            lNewDetMontoFinal = lPrecioVta * lNewCantProducto;

            lNewDetMonto = lNewDetMontoFinal / 1.13;

            lNewDetMontoIva = lNewDetMonto * 0.13;

            holder.totalItem.setText(formatoDecimal.format(lNewDetMontoFinal));

            Double newSubTotal = TicketDatos.gTotal - lPrecioVta;
            TicketDatos.gTotal = TicketDatos.gTotal - lPrecioVta;

            TicketDatos.subTotalReporte.setText(formatoDecimal.format(newSubTotal));
            TicketDatos.totalFinal.setText(formatoDecimal.format(newSubTotal));

            monto = newSubTotal / 1.13;
            montoIva = monto * 0.13;

            try {
                new ActualizarDetPedido(holder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            try {
                new ActualizarPedido(holder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(context, TicketDatos.class);
            context.startActivity(i);
            TicketDatos.gTotal = 0.00;

        });

        holder.btnEliminar.setOnClickListener(v -> {

            lidDetPedido = listaProdReport.get(position).getIdDetPedido();
            lDetMontoFinal = listaProdReport.get(position).getMonto() + listaProdReport.get(position).getMontoIva();

            try {
                new EliminarDetPedido(holder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            //if(EliminarDetPedido.exitoDeleteProd == true) {

            Double newSubTotal = TicketDatos.gTotal - lDetMontoFinal;
            TicketDatos.gTotal = TicketDatos.gTotal - lDetMontoFinal;

            TicketDatos.subTotalReporte.setText(formatoDecimal.format(newSubTotal));
            TicketDatos.totalFinal.setText(formatoDecimal.format(newSubTotal));

            removeAt(position);

            //}

            monto = newSubTotal / 1.13;
            montoIva = monto * 0.13;

            try {
                new ActualizarPedido(holder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    public void removeAt(int position) {
        listaProdReport.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaProdReport.size());
    }

    @Override
    public int getItemCount() {

        return listaProdReport.size();
    }

    public static class ProdReportViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio, tvCantidad, totalItem;
        Button btnMas, btnMenos, btnEliminar;

        public ProdReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvProducto);
            tvPrecio = itemView.findViewById(R.id.precioBase);
            tvCantidad = itemView.findViewById(R.id.cantBase);
            btnMas = itemView.findViewById(R.id.btnMas);
            btnMenos = itemView.findViewById(R.id.btnMenos);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            totalItem = itemView.findViewById(R.id.totalItem);

        }
    }
}