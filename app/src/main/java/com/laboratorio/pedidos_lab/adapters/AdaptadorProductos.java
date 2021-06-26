package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.controler.InsertarDetPedido;
import com.laboratorio.pedidos_lab.controler.InsertarPedido;
import com.laboratorio.pedidos_lab.main.ObtenerProductos;
import com.laboratorio.pedidos_lab.model.Productos;
import com.laboratory.views.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ProductosViewHolder> {
    Context context;
    public static List<Productos> listaProductos;
    public static List<Productos> carroCompra;

    public AdaptadorProductos(Context context, List<Productos> listaUsuarios, List<Productos> carroCompra) {
        this.context = context;
        listaProductos = listaUsuarios;
        AdaptadorProductos.carroCompra = carroCompra;

    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_productos, viewGroup, false);
        return new ProductosViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder productosViewHolder, int i) {


        //final Productos user = listaProductos.get(i);

        //TODO: Obtiene el nombre y el precio del modelado (Getter y Setter) En este caso solo usamos el getter.
        productosViewHolder.tvNombre.setText(listaProductos.get(i).getNombreProducto());
        productosViewHolder.tvPrecio.setText(Double.toString(listaProductos.get(i).getPrecioProducto()));

        //TODO: Se le asigna un color al cardview cuando posteriormente sea seleccionado
        //productosViewHolder.view.setBackgroundTintList(ColorStateList.valueOf(user.isSelect() ? Color.GRAY : Color.rgb(0, 151, 167)));

        productosViewHolder.view.setOnClickListener(v -> {

            //TODO: Si el boton es seleccionado que cambie de color
            //user.setSelect(!user.isSelect());
            //productosViewHolder.view.setBackgroundTintList(ColorStateList.valueOf(user.isSelect() ? Color.GRAY : Color.rgb(0,151,167)));

            //TODO: Guardar el estado del carrito

            //TODO: Cantidad de productos que se actualizan graficamente en el carrito

            /*if (user.isSelect){
                tvCantProductos.setText(""+(Integer.parseInt(tvCantProductos.getText().toString().trim()) - 1));
                carroCompra.add(listaProductos.get(i));
            }*/

            //TODO: Obtiene elos productos para posteriormente mostrarlos en cada cardView (En este caso solo muestra el nombre y el precio.
            ObtenerProductos.gNombreProd = listaProductos.get(i).getNombreProducto();
            ObtenerProductos.gIdProducto = listaProductos.get(i).getIdProducto();
            ObtenerProductos.gPrecio = listaProductos.get(i).getPrecioProducto();
            ObtenerProductos.gDetMonto = ObtenerProductos.gPrecio / 1.13;
            ObtenerProductos.gDetMontoIva = ObtenerProductos.gDetMonto * 0.13;

            ObtenerProductos.gOpciones = listaProductos.get(i).getOpciones();

            /*if(ObtenerProductos.gOpciones == 1){
                Intent intent = new Intent(productosViewHolder.itemView.getContext(), ObtenerOpciones.class);
                productosViewHolder.itemView.getContext().startActivity(intent);
            }*/

            if (ObtenerProductos.gPrecio == 0.0){
                productosViewHolder.itemView.setEnabled(false);
            }
            if(Login.gIdPedido == 0) {
                new InsertarPedido(productosViewHolder.itemView.getContext()).execute();
            }

            try {
                new InsertarDetPedido(productosViewHolder.itemView.getContext()).execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if (InsertarDetPedido.exitoInsertProd) {
                AgregarProducto(v);
                new ContadorProductos.GetDataFromServerIntoTextView(context).execute();

            }

        });
    }

    public void AgregarProducto (View view){

        Snackbar snack = Snackbar.make(view, "Producto agregado correctamente!",1000);
        view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.height = 120;
        view.setLayoutParams(params);
        snack.setBackgroundTint(Color.rgb(52,140,55));
        snack.show();
    }

    @Override
    public int getItemCount() {
        return listaProductos == null ? 0 : listaProductos.size();
    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio;
        public View view;

        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }

    public void filtrar(ArrayList<Productos> filtroUsuarios) {
        listaProductos = filtroUsuarios;
        notifyDataSetChanged();
    }
}
