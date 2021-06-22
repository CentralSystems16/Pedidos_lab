package com.laboratorio.pedidos_lab.front;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

public class acercaDe extends AppCompatActivity {

    ImageView facebook, gmail, politica, sobre, error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        politica = findViewById(R.id.politica);
        politica.setOnClickListener(v -> goToURL());

        sobre = findViewById(R.id.sobre);
        sobre.setOnClickListener(v -> {

            new FancyGifDialog.Builder(this)
                    .setTitle("Recursos utilizados en la aplicación\n\nhttps://lottiefiles.com/\nhttps://www.flaticon.com/\nhttps://github.com/\n\n\n\nVersión: 1.3.6")
                    .setNegativeBtnText("Regresar")
                    .setPositiveBtnBackground(R.color.rosado)
                    .setPositiveBtnText("Visitar")
                    .setNegativeBtnBackground(R.color.rojo)
                    .setGifResource(R.drawable.gif15)
                    .isCancellable(false)
                    .OnPositiveClicked(() -> {


                    })
                    .OnNegativeClicked(() -> Toast.makeText(this,"",Toast.LENGTH_SHORT))
                    .build();

        });

        error = findViewById(R.id.error);
        error.setOnClickListener(v -> {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"centralsystemsmanage@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto: quiero reportar un error en la app");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "El error consiste en:");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
            startActivity(emailIntent);
        });

        facebook = findViewById(R.id.facebook);
        facebook.setOnClickListener(v -> goToURL2(SplashPrincipal.gFacebookEmpresa));

        gmail = findViewById(R.id.gmail);
        gmail.setOnClickListener(v -> {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SplashPrincipal.gCorreoEmpresa});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto: ");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Quiero comunicarme con el LABORATORIO LCB porque:");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
            startActivity(emailIntent);

        });

    }

    private void goToURL() {
        Uri uri = Uri.parse("https://sites.google.com/view/central-systems-manage/p%C3%A1gina-principal");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goToURL2(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}