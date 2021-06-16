package com.laboratorio.pedidos_lab.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.adapters.AdaptadorProductos;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.model.Productos;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class ObtenerProductos extends AppCompatActivity {

    LottieAnimationView botonContinuar, botonRegresar;
    EditText etBuscador;
    RecyclerView rvLista = null;
    @SuppressLint("StaticFieldLeak")
    public static AdaptadorProductos adaptador = null;
    public static List<Productos> listaProductos;
    RequestQueue requestQueue11;
    public static final String URL_PRODUCTOS = "http://pedidoslab.6te.net/consultas/obtenerProductos.php";
    public static int gIdProducto;
    public static double gPrecio, gDetMonto, gDetMontoIva;
    public static int gOpciones = 0;
    public static String gNombreProd;
    List<Productos> carroCompras = new ArrayList<>();
    public static TextView tvCantProductos;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_productos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvCantProductos = findViewById(R.id.tvCantProductos);

        botonRegresar = findViewById(R.id.flecha2);
        botonRegresar.setOnClickListener(v -> {

            Intent i = new Intent(this, ObtenerCategorias.class);
            startActivity(i);
            new ContadorProductos.GetDataFromServerIntoTextView(this).execute();
        });

        requestQueue11 = Volley.newRequestQueue(this);

        botonContinuar = findViewById(R.id.carrito_compra);
        botonContinuar.setOnClickListener(v -> {

            if (gCount == 0){
                Toast.makeText(this, "Porfavor, agrege como mínimo un producto para continuar", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, TicketDatos.class);
                startActivity(i);
                new ContadorProductos.GetDataFromServerIntoTextView(this).execute();
            }

        });

        etBuscador = findViewById(R.id.etBuscador);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });

        rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new GridLayoutManager(this, 3));

        listaProductos = new ArrayList<>();

        adaptador = new AdaptadorProductos(this, listaProductos, carroCompras);
        rvLista.setAdapter(adaptador);

        obtenerProductos();

    }

    public void obtenerProductos() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        String url = URL_PRODUCTOS + "?id_categoria=" + ObtenerCategorias.gIdCategoria;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Productos");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            listaProductos.add(
                                    new Productos(
                                            jsonObject1.getInt("id_producto"),
                                            jsonObject1.getString("nombre_producto"),
                                            jsonObject1.getDouble("precio_producto"),
                                            jsonObject1.getInt("opciones")));
                        }

                        adaptador = new AdaptadorProductos(this, listaProductos, carroCompras);
                        rvLista.setAdapter(adaptador);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, Throwable::printStackTrace
        ) {
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    public void filtrar (String texto) {
        ArrayList<Productos> filtrarLista = new ArrayList<>();

        for (Productos usuario : listaProductos) {
            if (usuario.getNombreProducto().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(usuario);
            }
        }

        adaptador.filtrar(filtrarLista);
    }

    public void onBackPressed(){

    }
}