package com.laboratorio.pedidos_lab.back;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.laboratorio.pedidos_lab.manage.ModificarUsuario;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;
import static com.laboratorio.pedidos_lab.main.ObtenerCategorias.MY_DEFAULT_TIMEOUT;

public class DatosPrincipales extends AppCompatActivity implements View.OnClickListener {

    TextView tvUsuario;
    Button btnParaMi, btnParaOtra, misPedidos;
    public static String nombre = Login.nombre, URL_USERS = "";
    ImageButton home, categorias, carrito;
    public static TextView tvCantProductos4;
    DecimalFormat formatoDecimal = new DecimalFormat("#");

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

        tvCantProductos4 = findViewById(R.id.tvCantProductos4);
        tvCantProductos4.setText(String.valueOf(formatoDecimal.format(gCount)));

        if (Login.gIdPedido != 0){
            btnParaMi.setEnabled(false);
            btnParaOtra.setEnabled(false);
        }

        home = findViewById(R.id.home);
        home.setOnClickListener(v -> Toast.makeText(this, "Estas aquí!", Toast.LENGTH_SHORT).show());

        categorias = findViewById(R.id.categorias);
        categorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.gIdPedido == 0){
                    Toast.makeText(DatosPrincipales.this, "Parece que aún no has iniciado un pedido!", Toast.LENGTH_SHORT).show();
                } else {
                    categorias.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(244, 67,54)));
                    startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
                }
            }
        });

        carrito = findViewById(R.id.carrito);
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.gIdPedido == 0){
                    Toast.makeText(DatosPrincipales.this, "Parece que aún no has iniciado un pedido!", Toast.LENGTH_SHORT).show();
                } else {
                    TicketDatos.gTotal = 0.0;
                    carrito.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(244, 67,54)));
                    startActivity(new Intent(getApplicationContext(), TicketDatos.class));
                }
            }
        });

        ImageButton editUser = findViewById(R.id.editPerfUs);
        editUser.setOnClickListener(v -> {
            editUser.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(244, 67,54)));
            startActivity(new Intent(getApplicationContext(), ModificarUsuario.class));
        });

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

            if (Login.gIdCliente == 0) {

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

                 else {
                    obtenerUsuarioPrincipal();
                }

        }

            Intent i = new Intent(this, Lugar.class);
            startActivity(i);
        }

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

        URL_USERS = "http://pedidoslab.6te.net/consultas/obtenerLoginUsuarios.php" + "?id_usuario=" + Login.gIdUsuario;

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
                                            Login.gIdCliente = jsonObject1.getInt("id_cliente");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {

    }
}