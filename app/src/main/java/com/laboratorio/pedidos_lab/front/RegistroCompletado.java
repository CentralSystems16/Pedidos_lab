package com.laboratorio.pedidos_lab.front;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratory.views.R;

public class RegistroCompletado extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_completado);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(RegistroCompletado.this, Login.class);
            startActivity(i);
            finish();
        },5000);
    }
}
