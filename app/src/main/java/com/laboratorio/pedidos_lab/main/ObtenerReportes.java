package com.laboratorio.pedidos_lab.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.adapters.AdaptadorReportes;
import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.controler.MiPersona;
import com.laboratorio.pedidos_lab.model.Reportes;
import com.laboratory.views.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.laboratorio.pedidos_lab.main.ObtenerCategorias.MY_DEFAULT_TIMEOUT;

public class ObtenerReportes extends AppCompatActivity {

    RecyclerView rvLista;
    ArrayList<Reportes> reportes;
    AdaptadorReportes adaptador;
    public static String fechaCreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtener_reportes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final ImageButton regresa = findViewById(R.id.regresarDatos);
        regresa.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DatosPrincipales.class);
            startActivity(i);
        });

        reportes = new ArrayList<>();

        rvLista = findViewById(R.id.rvListaReportes);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        //rvLista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        obtenerReportes();

    }

    public void obtenerReportes() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        String URL_REPORTES = "http://pedidoslab.6te.net/consultas/obtenerReporteClientes.php" + "?id_estado_prefactura=2" + "&id_usuario=" + Login.gIdUsuario;
        System.out.println(URL_REPORTES);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_REPORTES,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Reporte");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            reportes.add(
                                    new Reportes(

                                            jsonObject1.getString("nombre_cliente"),
                                        fechaCreo = jsonObject1.getString("fecha_creo"),
                                            jsonObject1.getInt("id_prefactura")));
                        }

                        adaptador = new AdaptadorReportes(this, reportes);
                        rvLista.setAdapter(adaptador);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, Throwable::printStackTrace
        ) {
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}