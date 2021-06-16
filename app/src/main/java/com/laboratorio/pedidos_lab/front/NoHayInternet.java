package com.laboratorio.pedidos_lab.front;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.laboratory.views.R;
public class NoHayInternet extends AppCompatActivity {

    Button btnRefrescar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_hay_internet);

        btnRefrescar = findViewById(R.id.btnRefrescar);
        btnRefrescar.setOnClickListener(v -> {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                Intent i = new Intent(this, SplashPrincipal.class);
                startActivity(i);
            } else {
                Intent i2 = new Intent(this, NoHayInternet.class);
                startActivity(i2);
                Toast.makeText(this, "Aún no tienes internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}