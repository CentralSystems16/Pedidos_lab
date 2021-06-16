package com.laboratorio.pedidos_lab.back;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.laboratorio.pedidos_lab.controler.ActualizarCliente;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.controler.MiPersona;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.main.ObtenerReportes;
import com.laboratory.views.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatosPrincipales extends AppCompatActivity implements View.OnClickListener {

    TextView tvUsuario;
    Button btnParaMi, btnParaOtra, misPedidos;
    public static String nombre = Login.nombre;

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
            new ContadorProductos.GetDataFromServerIntoTextView(getApplicationContext()).execute();
            if(Login.gIdCliente == 0){

                try {
                    // Se agrega el método "get()" para obtener el resultado de la ejecución e impedir el proceso
                    // de la ejecución hasta obtener un resultado.
                    new MiPersona.InsertarClienteMiPersona(DatosPrincipales.this).execute().get();

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (MiPersona.exito) {
                    new ActualizarCliente.Actualizar(DatosPrincipales.this).execute();

                }
            }

            Intent i = new Intent(this, ObtenerCategorias.class);
            startActivity(i);
        }

        if (btnParaOtra.isPressed()) {
            Login.gIdPedido = 0;
            Intent in = new Intent(this, OtraPersona.class);
            startActivity(in);

        }

        if (misPedidos.isPressed()){
            Intent i = new Intent(getApplicationContext(), ObtenerReportes.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {

    }
}