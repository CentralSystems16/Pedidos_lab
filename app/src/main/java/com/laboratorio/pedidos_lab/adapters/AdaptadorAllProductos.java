package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.laboratorio.pedidos_lab.controler.ContadorProductos2;
import com.laboratorio.pedidos_lab.main.ObtenerAllProductos;
import com.laboratorio.pedidos_lab.model.AllProductos;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.controler.ContadorDetPedidos;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.controler.InsertarDetPedido;
import com.laboratorio.pedidos_lab.controler.InsertarPedido;
import com.laboratorio.pedidos_lab.main.ObtenerProductos;
import com.laboratory.views.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class AdaptadorAllProductos extends RecyclerView.Adapter<AdaptadorAllProductos.ProductosViewHolder> {
    Context context;
    public static List<AllProductos> listaProductos;
    DecimalFormat formatoDecimal = new DecimalFormat("#");

    public AdaptadorAllProductos(Context context, List<AllProductos> listaUsuarios) {
        this.context = context;
        listaProductos = listaUsuarios;

    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_all_productos, viewGroup, false);
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

            new ContadorProductos.GetDataFromServerIntoTextView(productosViewHolder.itemView.getContext()).execute();

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
                //TODO: Hace un recuento de los pedidos que se selecciona y los inserta en la base de datos.
                new ContadorProductos2.GetDataFromServerIntoTextView(context).execute();
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
        return Math.min(listaProductos.size(), 1);

    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio;
        public View view;

        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

            tvNombre = itemView.findViewById(R.id.tvNombre2);
            tvPrecio = itemView.findViewById(R.id.tvPrecio2);
        }
    }

    public void filtrar(ArrayList<AllProductos> filtroUsuarios) {
        listaProductos = filtroUsuarios;
        notifyDataSetChanged();
    }
}
