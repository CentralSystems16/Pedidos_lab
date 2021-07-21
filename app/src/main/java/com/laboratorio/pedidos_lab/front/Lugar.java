package com.laboratorio.pedidos_lab.front;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.laboratorio.pedidos_lab.main.ObtenerCategorias.MY_DEFAULT_TIMEOUT;

public class Lugar extends AppCompatActivity {

    public static String tipoPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);

        Button miDomicilio = findViewById(R.id.domicilio);
        Button laboratorio = findViewById(R.id.laboratorio);

        miDomicilio.setOnClickListener(v -> {

                    if (Login.direccion.equals("") && Login.latitud.equals("0") && Login.longitud.equals("0")) {
                    startActivity(new Intent(getApplicationContext(), AgregarDireccion.class));
                    obtenerDomicilio();
                    ContadorProductos.GetDataFromServerIntoTextView.gCount = 0.0;

                } else {
                        ContadorProductos.GetDataFromServerIntoTextView.gCount = 0.0;
                        obtenerDomicilio();
                        startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
                    }
        });

        laboratorio.setOnClickListener(v -> {
            obtenerLaboratorio();
            ContadorProductos.GetDataFromServerIntoTextView.gCount = 0.0;
            startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
        });
    }

    public void obtenerDomicilio() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        String URL_REPORTES = "http://pedidoslab.6te.net/consultas/obtenerLugar.php" + "?id_tipo_pedido=1";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_REPORTES,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Lugar");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            tipoPedido = jsonObject1.getString("tipo_pedido");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, Throwable::printStackTrace
        ) {
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


    }

    public void obtenerLaboratorio(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        String URL_REPORTES = "http://pedidoslab.6te.net/consultas/obtenerLugar.php" +  "?id_tipo_pedido=2";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_REPORTES,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Lugar");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            tipoPedido = jsonObject1.getString("tipo_pedido");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, Throwable::printStackTrace
        ) {
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed(){

    }
}