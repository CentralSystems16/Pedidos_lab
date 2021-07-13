package com.laboratorio.pedidos_lab.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.adapters.AdaptadorClientes;
import com.laboratorio.pedidos_lab.adapters.AdaptadorReportes;
import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.OtraPersona;
import com.laboratorio.pedidos_lab.model.Clientes;
import com.laboratorio.pedidos_lab.model.Reportes;
import com.laboratory.views.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.laboratorio.pedidos_lab.main.ObtenerCategorias.MY_DEFAULT_TIMEOUT;

public class ObtenerClientes extends AppCompatActivity {

    RecyclerView rvLista;
    ArrayList<Clientes> clientes;
    AdaptadorClientes adaptador;
    Button agregar;
    public static int gIdClienteConsulta;
    String URL_CLIENTES = "";
    public static String nombreCliente, nacimientoCliente, direccionCliente, emailCliente;
    public static int idCliente, edadCliente, mesesCliente;
    ImageButton flecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtener_clientes);

        flecha = findViewById(R.id.flecha232);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DatosPrincipales.class);
                startActivity(i);
            }
        });

        agregar = findViewById(R.id.agregarCliente);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OtraPersona.class);
                startActivity(i);
            }
        });

        clientes = new ArrayList<>();
        rvLista = findViewById(R.id.rvListaClientes);

        rvLista.setLayoutManager(new LinearLayoutManager(this));

        obtenerClientes();

    }

    public void obtenerClientes() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        URL_CLIENTES = "http://pedidoslab.6te.net/consultas/obtenerClientes.php" + "?id_usuario=" + Login.gIdUsuario;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CLIENTES,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Clientes");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            clientes.add(
                                    new Clientes(

                                         nombreCliente = jsonObject1.getString("nombre_cliente"),
                                         nacimientoCliente = jsonObject1.getString("nacimiento_cliente"),
                                         direccionCliente = jsonObject1.getString("direccion_cliente"),
                                         emailCliente = jsonObject1.getString("email_cliente"),
                                         idCliente = jsonObject1.getInt("id_cliente"),
                                         edadCliente = jsonObject1.getInt("edad_cliente"),
                                         mesesCliente = jsonObject1.getInt("meses_cliente")));

                        }

                        adaptador = new AdaptadorClientes(this, clientes);
                        rvLista.setAdapter(adaptador);

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
}