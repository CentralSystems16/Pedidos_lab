package com.laboratorio.pedidos_lab.conections;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.laboratorio.pedidos_lab.front.NoHayInternet;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;

public class VerificarInternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Intent i = new Intent(this, SplashPrincipal.class);
            startActivity(i);
        } else {
            Intent i2 = new Intent(this, NoHayInternet.class);
            startActivity(i2);
            Toast.makeText(this, "Necesitas una conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }
}