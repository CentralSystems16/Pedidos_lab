package com.laboratorio.pedidos_lab.front;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratory.views.R;

public class Lugar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);

        Button miDomicilio = findViewById(R.id.domicilio);
        Button laboratorio = findViewById(R.id.laboratorio);

        miDomicilio.setOnClickListener(v -> {
            ContadorProductos.GetDataFromServerIntoTextView.gCount = 0.0;
            startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
        });

        laboratorio.setOnClickListener(v -> {
            ContadorProductos.GetDataFromServerIntoTextView.gCount = 0.0;
            startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
        });
    }
}