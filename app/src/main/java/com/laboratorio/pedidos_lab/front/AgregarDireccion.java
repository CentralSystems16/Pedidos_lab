package com.laboratorio.pedidos_lab.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.RegistroUsuario;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.maps.Localizacion;
import com.laboratorio.pedidos_lab.maps.LocalizacionEdit;
import com.laboratorio.pedidos_lab.maps.LocalizacionEditFinal;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

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
    TextView latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_direccion);

        latitud = findViewById(R.id.latitudEdit2);
        longitud = findViewById(R.id.longitudEdit2);

        maps = findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new FancyGifDialog.Builder(AgregarDireccion.this)
                        .setTitle("Esta función obtiene su ubicación exacta, por lo tanto se recomienda estar en su domicilio para mejor efectividad de obtención de datos\n\nAl seleccionar 'Estoy en mi domicilio' aceptas los terminos de política y privacidad'\n\nAsegurate de tener activo tu GPS.")
                        .setNegativeBtnText("No estoy en mi domicilio")
                        .setPositiveBtnBackground(R.color.rosado)
                        .setPositiveBtnText("Estoy en mi domicilio")
                        .setNegativeBtnBackground(R.color.rojo)
                        .setGifResource(R.drawable.mapgif)
                        .isCancellable(false)
                        .OnPositiveClicked(() -> {
                            iniciarLocalizacion();
                            new ActualizarDireccionMaps(getApplicationContext()).execute();
                            startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));

                        })
                        .OnNegativeClicked(() -> Toast.makeText(AgregarDireccion.this,"Cancelado",Toast.LENGTH_LONG).show())
                        .build();

            }
        });

        direccion = findViewById(R.id.etDirecNew);
        Button continuar = findViewById(R.id.contDireccion);


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (direccion.equals("") || Login.latitud.equals("0") || Login.longitud.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Por favor, agrega tu dirección para continuar.", Toast.LENGTH_SHORT).show();
                } else {
                    new ActualizarDireccion(getApplicationContext()).execute();
                    new ActualizarDireccion2(getApplicationContext()).execute();
                    Toast.makeText(AgregarDireccion.this, "Gracias, se ha agregado su dirección.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class));
                }
            }
        });
    }

    public void iniciarLocalizacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocalizacionEditFinal localizacion = new LocalizacionEditFinal();
        localizacion.setMainActivity(this, latitud, longitud);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!gpsEnabled) {

            Toast.makeText(this, "Por favor, activa tu GPS y vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);

        } else {
            Toast.makeText(this, "Gracias, se ha obtenido tu ubicación actual.", Toast.LENGTH_SHORT).show();
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, localizacion);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, localizacion);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarLocalizacion();
                return;
            }
        }
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

    public class ActualizarDireccion2 extends AsyncTask<String, Void, String> {

        String direct = direccion.getText().toString();
        private final WeakReference<Context> context;

        public ActualizarDireccion2 (Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String...params){
            String actualizar_url = "http://pedidoslab.6te.net/consultas/insertarDireccion2.php"
                    + "?direccion_usuario=" + direct
                    + "&id_usuario=" + Login.gIdUsuario;

            String resultado;

            try {

                URL url = new URL(actualizar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String direccion = (direct);
                String idUsuario = String.valueOf(Login.gIdUsuario);

                String data = URLEncoder.encode("direccion_usuario", "UTF-8") + "=" + URLEncoder.encode(direccion, "UTF-8")
                        + "&" + URLEncoder.encode("id_usuario", "UTF-8") + "=" + URLEncoder.encode(idUsuario, "UTF-8");

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

    public class ActualizarDireccionMaps extends AsyncTask<String, Void, String> {

        String latitudDef = LocalizacionEditFinal.latitud1;
        String longitudDef = LocalizacionEditFinal.longitud1;
        private final WeakReference<Context> context;

        public ActualizarDireccionMaps (Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String...params){
            String actualizar_url = "http://pedidoslab.6te.net/consultas/insertarDireccionMaps.php"
                    + "?latitud=" + latitudDef
                    + "&longitud=" + longitudDef
                    + "&id_cliente=" + Login.gIdCliente;
            System.out.println(actualizar_url);

            String resultado;

            try {

                URL url = new URL(actualizar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String latitud1 = latitudDef;
                String longitud1 = longitudDef;
                String idCliente = String.valueOf(Login.gIdCliente);

                String data = URLEncoder.encode("latitud", "UTF-8") + "=" + URLEncoder.encode(latitud1, "UTF-8")
                        + "&" + URLEncoder.encode("longitud", "UTF-8") + "=" + URLEncoder.encode(longitud1, "UTF-8")
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