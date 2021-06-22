package com.laboratorio.pedidos_lab.front;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.conections.VerificarInternet;
import com.laboratorio.pedidos_lab.controler.ActualizarPrefactura;
import com.laboratorio.pedidos_lab.main.ObtenerReportes;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

import java.io.FileNotFoundException;

import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

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

        new FancyGifDialog.Builder(this)
                .setTitle("Hola " + TicketDatos.gNomCliente + "\nGracias! su orden se ha enviado con éxito!!!\n\nPuede revisar el detalle de su orden desde 'Mis ordenes'" +
                        "\n\nSi estableció un correo electronico en su registro también puede consultar su correo para ver su detalle de orden\n\n\nNOTA: Si aún no recibe dicho correo, revise su carpeta de spam. ")
                .setNegativeBtnText("OK")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Ver ahora")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.gif19)
                .isCancellable(false)
                .OnPositiveClicked(() -> {

                Login.gIdPedido = 0;
                gCount = 0.0;
                Intent i = new Intent(this, ObtenerReportes.class);
                startActivity(i);

                })
                .OnNegativeClicked(() -> Toast.makeText(this,"",Toast.LENGTH_SHORT))
                .build();

    }

    @Override
    public void onClick(View v) {

        if (btnRepetir.isPressed()) {

            TicketDatos.gTotal = 0.00;

            Login.gIdPedido = 0;

            gCount = 0.0;

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

            TicketDatos.gTotal = 0.00;

            Login.gIdPedido = 0;

            gCount = 0.0;

        }
    }

    @Override
    public void onBackPressed(){

    }
}
