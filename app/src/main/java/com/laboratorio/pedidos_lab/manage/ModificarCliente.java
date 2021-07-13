package com.laboratorio.pedidos_lab.manage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.main.ObtenerClientes;
import com.laboratorio.pedidos_lab.maps.LocalizacionEdit;
import com.laboratorio.pedidos_lab.maps.LocalizacionEditClientes;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

public class ModificarCliente extends AppCompatActivity {

    EditText editNombre, editEdad, editMes, editEmail, editDireccion;
    TextView editFecha, tvLatitud, tvLongitud;
    Button modificarCliente, maps;
    ImageButton regresarMod;
    RequestQueue requestQueue;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_usuario);

        maps = findViewById(R.id.EditMap);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FancyGifDialog.Builder(ModificarCliente.this)
                        .setTitle("Recuerda estar en tu domicilio antes de actualizar tu ubicación\n\n Al seleccionar 'Estoy en mi domicilio' aceptas los términos de política y privacidad'\n\nAsegurate de tener activa tu ubicación.")
                        .setNegativeBtnText("No estoy en mi domicilio")
                        .setPositiveBtnBackground(R.color.rosado)
                        .setPositiveBtnText("Estoy en mi domicilio")
                        .setNegativeBtnBackground(R.color.rojo)
                        .setGifResource(R.drawable.mapgif)
                        .isCancellable(false)
                        .OnPositiveClicked(() -> {
                            iniciarLocalizacion();
                        })
                        .OnNegativeClicked(() -> Toast.makeText(ModificarCliente.this,"Cancelado",Toast.LENGTH_LONG))
                        .build();
            }
        });

        tvLatitud = findViewById(R.id.latitudEditCliente);
        tvLongitud = findViewById(R.id.longitudEditCliente);

        editNombre = findViewById(R.id.etNomMod);
        editNombre.setText(ObtenerClientes.nombreCliente);

        editEdad = findViewById(R.id.etEdMod);
        editEdad.setText(String.valueOf(ObtenerClientes.edadCliente));

        editMes = findViewById(R.id.etMeMod);
        editMes.setText(String.valueOf(ObtenerClientes.mesesCliente));

        editFecha = findViewById(R.id.tvFecMod);
        editFecha.setText(ObtenerClientes.nacimientoCliente);

        editEmail = findViewById(R.id.etEmMod);
        editEmail.setText(ObtenerClientes.emailCliente);

        editDireccion = findViewById(R.id.etDirMod);
        editDireccion.setText(ObtenerClientes.direccionCliente);

        modificarCliente = findViewById(R.id.btnContMod);
        modificarCliente.setOnClickListener(v -> ejecutar());

        regresarMod = findViewById(R.id.btnRegMod);
        regresarMod.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ObtenerClientes.class)));
    }

    private void ejecutar(){

        String name = editNombre.getText().toString();
        String date = editFecha.getText().toString();
        String age = editEdad.getText().toString();
        String month = editMes.getText().toString();

        if (name.equals("")){
            Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
        }
        else if (date.equals("")){
            Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
        }
        else if (age.equals("")){
            Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
        }
        else if (month.equals("")){
            Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
        }
        else {

            String url = "http://pedidoslab.6te.net/consultas/ModificarCliente.php"
                    + "?nombre_cliente=" + editNombre.getText().toString()
                    + "&edad_cliente=" + editEdad.getText().toString()
                    + "&meses_cliente=" + editMes.getText().toString()
                    + "&nacimiento_cliente=" + editFecha.getText().toString()
                    + "&email_cliente=" + editEmail.getText().toString()
                    + "&direccion_cliente=" + editDireccion.getText().toString()
                    + "&latitud" + tvLatitud.getText().toString()
                    + "&longitud" + tvLongitud.getText().toString()
                    + "&id_cliente=" + ObtenerClientes.idCliente;

            ejecutarServicio(url);
            startActivity(new Intent(getApplicationContext(), ObtenerClientes.class));
        }
    }

    public void ejecutarServicio (String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(getApplicationContext(), "USUARIO ACTUALIZADO CON ÉXITO", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void iniciarLocalizacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocalizacionEditClientes localizacion = new LocalizacionEditClientes();
        localizacion.setMainActivity(this, tvLatitud, tvLongitud);

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

}