package com.laboratorio.pedidos_lab.front;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.laboratory.views.R;

public class EnviandoTicket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviando_ticket);

        new Handler().postDelayed(() -> {
                Intent i = new Intent(getApplicationContext(), VistaFinal.class);
            startActivity(i);
            finish();
        },8000);
    }
}