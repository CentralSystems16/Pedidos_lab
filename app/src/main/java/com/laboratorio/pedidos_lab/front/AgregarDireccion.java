package com.laboratorio.pedidos_lab.front;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratory.views.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AgregarDireccion extends AppCompatActivity {

    EditText direccion;
    Button maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_direccion);

        maps = findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), MapsActivity.class));

            }
        });

        direccion = findViewById(R.id.etDirecNew);
        Button continuar = findViewById(R.id.contDireccion);


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActualizarDireccion(getApplicationContext()).execute();
                startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
            }
        });
    }

    public class ActualizarDireccion extends AsyncTask<String, Void, String> {

        String direct = direccion.getText().toString();
        private final WeakReference<Context> context;

        public ActualizarDireccion (Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String...params){
            String actualizar_url = "http://pedidoslab.6te.net/consultas/insertarDireccion.php"
                    + "?direccion_cliente=" + direct
                    + "&id_cliente=" + Login.gIdCliente;

            String resultado;

            try {

                URL url = new URL(actualizar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String direccion = (direct);
                String idCliente = String.valueOf(Login.gIdCliente);

                String data = URLEncoder.encode("direccion_cliente", "UTF-8") + "=" + URLEncoder.encode(direccion, "UTF-8")
                        + "&" + URLEncoder.encode("id_cliente", "UTF-8") + "=" + URLEncoder.encode(idCliente, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                resultado = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                Log.d("MiAPP", "Se ha utilizado una URL con formato incorrecto");
                resultado = "Se ha producido un ERROR";
            } catch (IOException e) {
                Log.d("MiAPP", "Error inesperado!, posibles problemas de conexion de red");
                resultado = "Se ha producido un ERROR, comprueba tu conexion a Internet";
            }

            return resultado;
        }
    }
}