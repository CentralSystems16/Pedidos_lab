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
import com.laboratorio.pedidos_lab.adapters.AdaptadorAllProductos;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.controler.ContadorProductos2;
import com.laboratorio.pedidos_lab.model.AllProductos;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class ObtenerAllProductos extends AppCompatActivity {

    LottieAnimationView botonContinuar;
    ImageButton botonRegresar;
    EditText etBuscador;
    RecyclerView rvLista = null;
    AdaptadorAllProductos adaptador = null;
    List<AllProductos> listaProductos;
    RequestQueue requestQueue11;
    public static final String URL_PRODUCTOS = "http://pedidoslab.6te.net/consultas/ObtenerTodosProd.php";
    public static TextView tvCantidadAllProductos;
    DecimalFormat formatoDecimal = new DecimalFormat("#");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_all_productos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvCantidadAllProductos = findViewById(R.id.tvCantProductos2);
        tvCantidadAllProductos.setText(String.valueOf(formatoDecimal.format(gCount)));

        botonRegresar = findViewById(R.id.flecha3);
        botonRegresar.setOnClickListener(v -> {
            Intent i = new Intent(this, ObtenerCategorias.class);
            startActivity(i);
        });

        requestQueue11 = Volley.newRequestQueue(this);

        botonContinuar = findViewById(R.id.carrito_compra2);
        botonContinuar.setOnClickListener(v -> {
            if (gCount == 0) {
                Toast.makeText(this, "Por favor, agrege como mínimo un producto para continuar", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, TicketDatos.class);
                startActivity(i);
            }
        });

        etBuscador = findViewById(R.id.etBuscador2);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rvLista.setVisibility(View.VISIBLE);
                filtrar(s.toString());
            }
        });

        rvLista = findViewById(R.id.rvLista2);

        rvLista.setLayoutManager(new GridLayoutManager(this, 3));

        listaProductos = new ArrayList<>();

        adaptador = new AdaptadorAllProductos(this, listaProductos);
        rvLista.setAdapter(adaptador);

        rvLista.setVisibility(View.GONE);

        obtenerProductos();

    }

    public void obtenerProductos() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTOS,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Productos");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            listaProductos.add(
                                    new AllProductos(
                                            jsonObject1.getInt("id_producto"),
                                            jsonObject1.getString("nombre_producto"),
                                            jsonObject1.getDouble("precio_producto"),
                                            jsonObject1.getInt("opciones")));
                        }

                        adaptador = new AdaptadorAllProductos(this, listaProductos);
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

        ArrayList<AllProductos> filtrarLista = new ArrayList<>();

        for (AllProductos usuario : listaProductos) {
            if (usuario.getNombreProducto().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(usuario);
            }
        }

        adaptador.filtrar(filtrarLista);
    }

    public void onBackPressed(){

    }
}