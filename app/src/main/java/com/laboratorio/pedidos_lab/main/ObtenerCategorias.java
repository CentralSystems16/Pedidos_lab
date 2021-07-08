package com.laboratorio.pedidos_lab.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
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
import com.laboratorio.pedidos_lab.adapters.AdaptadorCategorias;
import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.model.Categorias;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class ObtenerCategorias extends AppCompatActivity {

    public static final int MY_DEFAULT_TIMEOUT = 15000;
    RecyclerView rvListaCategorias;
    AdaptadorCategorias adaptadorCat;
    List<Categorias> listaCategorias;
    Button etBuscador;
    LottieAnimationView botonContinuar, botonRegresar;
    public static final String URL_CATEGORIAS = "http://pedidoslab.6te.net/consultas/obtenerCategorias.php"+"?estado_categoria=1";
    public static int gIdCategoria;
    public static TextView tvCantProd3;
    DecimalFormat formatoDecimal = new DecimalFormat("#");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_categoria);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvCantProd3 = findViewById(R.id.tvCantProductos3);
        tvCantProd3.setText(String.valueOf(formatoDecimal.format(gCount)));

        botonRegresar = findViewById(R.id.flecha);
        botonRegresar.setOnClickListener(v -> {

            new FancyGifDialog.Builder(this)
                    .setTitle("Solo regresa si la orden la quieres realizar para otra persona, de lo contrario se borrara la que hayas hecho anteriormente")
                    .setNegativeBtnText("Cancelar")
                    .setPositiveBtnBackground(R.color.rosado)
                    .setPositiveBtnText("Regresar")
                    .setNegativeBtnBackground(R.color.rojo)
                    .setGifResource(R.drawable.regresarflecha)
                    .isCancellable(false)
                    .OnPositiveClicked(() -> {

                        Intent i = new Intent(this, DatosPrincipales.class);
                        startActivity(i);

                    })
                    .OnNegativeClicked(() -> Toast.makeText(this,"",Toast.LENGTH_SHORT))
                    .build();


        });

        botonContinuar = findViewById(R.id.carrito_compra3);
        botonContinuar.setOnClickListener(v -> {
            if (gCount == 0){
                Toast.makeText(this, "Porfavor, agrege como mínimo un producto para continuar", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, TicketDatos.class);
                startActivity(i);
            }
        });

        etBuscador = findViewById(R.id.etBuscadorCategorias);
        etBuscador.setOnClickListener(v -> {
            Intent i = new Intent(this, ObtenerAllProductos.class);
            startActivity(i);

        });

        rvListaCategorias = findViewById(R.id.rvListaCategorias);
        rvListaCategorias.setLayoutManager(new GridLayoutManager(this, 2));

        listaCategorias = new ArrayList<>();

        adaptadorCat = new AdaptadorCategorias(this, listaCategorias);
        rvListaCategorias.setAdapter(adaptadorCat);

        obtenerCategorias();

    }

    public void obtenerCategorias() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CATEGORIAS,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Categorias");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            listaCategorias.add(
                                    new Categorias(
                                            jsonObject1.getInt("id_categoria"),
                                            jsonObject1.getString("nombre_categoria"),
                                            jsonObject1.getString("img_categoria")
                                    )
                            );
                        }

                        adaptadorCat = new AdaptadorCategorias(this, listaCategorias);
                        rvListaCategorias.setAdapter(adaptadorCat);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, Throwable::printStackTrace
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    public void onBackPressed(){

    }

}