package com.laboratorio.pedidos_lab.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.adapters.AdaptadorNegocios;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;
import com.laboratorio.pedidos_lab.model.Negocios;
import com.laboratory.views.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import static com.laboratorio.pedidos_lab.main.ObtenerCategorias.MY_DEFAULT_TIMEOUT;

public class ObtenerNegocios extends AppCompatActivity {

    RecyclerView rvLista;
    ArrayList<Negocios> negocios;
    AdaptadorNegocios adaptador;
    String URL_NEGOCIOS = "";
    public static int idNegocio;
    public static String baseDatos;
    int idNegocioShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtener_negocios);

        /*SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        idNegocioShared = sharedPref.getInt("idNegocio", 0);*/

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            obtenerNegocios();
        } else{
            startActivity(new Intent(ObtenerNegocios.this, SplashPrincipal.class));
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();

        negocios = new ArrayList<>();
        rvLista = findViewById(R.id.rvListaNegocios);
        rvLista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        obtenerNegocios();

    }

    public void obtenerNegocios() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        URL_NEGOCIOS = "http://pedidoslab.6te.net/consultas3/obtenerNegocios.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_NEGOCIOS,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Negocios");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            negocios.add(
                                    new Negocios(

                                            jsonObject1.getString("nombre_negocio"),
                                            jsonObject1.getString("img_negocio"),
                                            idNegocio = jsonObject1.getInt("id_negocio"),
                                            baseDatos = jsonObject1.getString("base_datos")));

                        }

                        adaptador = new AdaptadorNegocios(this, negocios);
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