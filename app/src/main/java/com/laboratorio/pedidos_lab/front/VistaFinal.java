package com.laboratorio.pedidos_lab.front;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.conections.VerificarInternet;
import com.laboratorio.pedidos_lab.main.ObtenerReportes;
import com.laboratory.views.R;

public class VistaFinal extends AppCompatActivity implements View.OnClickListener {

   @SuppressLint("StaticFieldLeak")
   public static Button btnRepetir, btnCerrarSesion, salir;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vista_final);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnRepetir = findViewById(R.id.btnRepetirExamen);
        btnRepetir.setOnClickListener(this);

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this);

        salir = findViewById(R.id.btnSalir);
        salir.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (btnRepetir.isPressed()) {

            TicketDatos.gTotal = 0.00;

            Login.gIdPedido = 0;

            Intent i = new Intent (this, DatosPrincipales.class);
            startActivity(i);

        }

        if (salir.isPressed()) {

            Login.gIdPedido= 0;
            finishAffinity();
            finishActivity(0);
            System.exit(0);

        }
        if (btnCerrarSesion.isPressed()){

            Intent i = new Intent (getApplicationContext(), ObtenerReportes.class);
            startActivity(i);

        }
    }

    @Override
    public void onBackPressed(){

    }
}
