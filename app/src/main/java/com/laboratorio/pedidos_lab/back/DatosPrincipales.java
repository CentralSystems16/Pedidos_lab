package com.laboratorio.pedidos_lab.back;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.laboratorio.pedidos_lab.controler.ActualizarCliente;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.controler.MiPersona;
import com.laboratorio.pedidos_lab.front.Lugar;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.main.ObtenerClientes;
import com.laboratorio.pedidos_lab.main.ObtenerReportes;
import com.laboratory.views.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.laboratorio.pedidos_lab.main.ObtenerCategorias.MY_DEFAULT_TIMEOUT;

public class DatosPrincipales extends AppCompatActivity implements View.OnClickListener {

    TextView tvUsuario;
    Button btnParaMi, btnParaOtra, misPedidos;
    public static String nombre = Login.nombre;
    public static String URL_USERS = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_principales);
        //TODO: Bloquear orientación de pantalla.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvUsuario = findViewById(R.id.tvUsuarioId);
        tvUsuario.setText(nombre);
        btnParaMi = findViewById(R.id.btnParaMi);
        btnParaOtra = findViewById(R.id.btnParaOtraPersona);
        misPedidos = findViewById(R.id.btnPedidos);
        btnParaMi.setOnClickListener(this);
        btnParaOtra.setOnClickListener(this);
        misPedidos.setOnClickListener(this);

        ImageSlider imageSlider = findViewById(R.id.slider);

        //TODO: Lista de imagenes que pasan en el slider.
        List<SlideModel> slideModels  = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.sliderww));
        slideModels.add(new SlideModel(R.drawable.labslider2));
        slideModels.add(new SlideModel(R.drawable.labslider3));
        imageSlider.setImageList(slideModels, true);
    }

    @Override
    public void onClick(View v) {
        if (btnParaMi.isPressed()) {
            Login.gIdPedido = 0;
            ContadorProductos.GetDataFromServerIntoTextView.gCount = 0.0;
            if(Login.gIdCliente == 0){
                try {
                    // Se agrega el método "get()" para obtener el resultado de la ejecución e impedir el proceso
                    // de la ejecución hasta obtener un resultado.
                    new MiPersona.InsertarClienteMiPersona(this).execute().get();

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (MiPersona.exito) {
                    new ActualizarCliente.Actualizar(this).execute();

                }
            }

            Intent i = new Intent(this, Lugar.class);
            startActivity(i);
        }

        System.out.println("Id de cliente: " + Login.gIdCliente);
        Toast.makeText(this, "Id de cliente: " + Login.gIdCliente, Toast.LENGTH_SHORT).show();

        if (btnParaOtra.isPressed()) {

                Login.gIdPedido = 0;
                Intent in = new Intent(this, ObtenerClientes.class);
                startActivity(in);


        }

        if (misPedidos.isPressed()){
            Intent i = new Intent(this, ObtenerReportes.class);
            startActivity(i);
        }
    }

    public void obtenerUsuarioPrincipal() {

        URL_USERS = "http://pedidoslab.6te.net/consultas/obtenerLoginUsuarios.php" + "?id_cliente=" + Login.gIdCliente;
        Toast.makeText(this, URL_USERS, Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_USERS,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Users");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                            jsonObject1.getString("nombre_usuario");
                                            jsonObject1.getString("sexo_usuario");
                                            jsonObject1.getString("nacimiento_usuario");
                                            jsonObject1.getInt("edad_usuario");
                                            jsonObject1.getInt("dui_usuario");
                                            jsonObject1.getInt("meses_usuario");
                                            jsonObject1.getString("email_usuario");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {

    }
}