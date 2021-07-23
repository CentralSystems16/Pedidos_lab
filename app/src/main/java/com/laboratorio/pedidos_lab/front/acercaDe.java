package com.laboratorio.pedidos_lab.front;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
        politica.setOnClickListener(v -> goToPolitica());

        sobre = findViewById(R.id.sobre);
        sobre.setOnClickListener(v ->

                new FancyGifDialog.Builder(this)
                .setTitle("Recursos utilizados en la aplicación\n\nhttps://lottiefiles.com/\nhttps://www.flaticon.com/\nhttps://github.com/\n\n\n\nVersión: 1.4.1")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Visitar")
                .setNegativeBtnText("Regresar")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.gif15)
                .isCancellable(false)
                .OnPositiveClicked(() -> {

                    goToFlaticon();
                    goToGitHub();
                    goToLottie();

                })
                .OnNegativeClicked(() -> Toast.makeText(this,"",Toast.LENGTH_SHORT))
                .build());

        error = findViewById(R.id.error);
        error.setOnClickListener(v -> {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"centralsystemsmanage@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto: quiero reportar un error en la app");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "El error consiste en:");
            //Si el usuario quiere adjuntar un archivo
            //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
            startActivity(emailIntent);
        });

        facebook = findViewById(R.id.facebook);
        facebook.setOnClickListener(v -> goToFacebook(SplashPrincipal.gFacebookEmpresa));

        gmail = findViewById(R.id.gmail);
        gmail.setOnClickListener(v -> {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SplashPrincipal.gCorreoEmpresa});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto: ");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Quiero comunicarme con el LABORATORIO LCB porque:");
            //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
            startActivity(emailIntent);

        });
    }

    private void goToPolitica() {
        Uri uri = Uri.parse("https://sites.google.com/view/central-systems-manage/p%C3%A1gina-principal");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goToGitHub() {
        Uri uri = Uri.parse("https://github.com/");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goToLottie() {
        Uri uri = Uri.parse("https://lottiefiles.com/");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goToFlaticon() {
        Uri uri = Uri.parse("https://www.flaticon.com/");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void goToFacebook(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}