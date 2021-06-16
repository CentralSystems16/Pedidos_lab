package com.laboratorio.pedidos_lab.front;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashPrincipal extends AppCompatActivity {

    //String URL = "http://pedidoslab.atspace.cc/obtenerEmpresa.php";
    String URL = "http://pedidoslab.6te.net/consultas/obtenerEmpresa.php";
    public static String gNombreEmpresa, gLogoEmpresa, gCorreoEmpresa, gFacebookEmpresa;
    TextView tvEmpresa;
    ImageView imgEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Bloquear orientación de pantalla.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_principal);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo2);

        tvEmpresa = findViewById(R.id.tvLab);
        imgEmpresa = findViewById(R.id.imgSplash);

        tvEmpresa.setAnimation(animacion2);
        imgEmpresa.setAnimation(animacion1);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashPrincipal.this, Login.class);
            startActivity(i);
            finish();
        },7000); //TODO: Tiempo en que permanecera activo el splash.

        DatosEmpresa();
    }

    public void DatosEmpresa(){
        RequestQueue requestQueue = Volley.newRequestQueue(SplashPrincipal.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Empresa");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            gNombreEmpresa = jsonObject1.getString("nombre_empresa");
                            tvEmpresa.setText(gNombreEmpresa);

                            gCorreoEmpresa = jsonObject1.getString("correo_empresa");

                            gLogoEmpresa = jsonObject1.getString("logo_empresa");
                            Glide.with(this).load(gLogoEmpresa).into(imgEmpresa);

                            gFacebookEmpresa = jsonObject1.getString("facebook_empresa");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {};
        requestQueue.add(request);
    }
}
