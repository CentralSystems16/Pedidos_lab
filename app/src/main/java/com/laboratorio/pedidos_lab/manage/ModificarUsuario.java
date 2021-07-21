package com.laboratorio.pedidos_lab.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.maps.LocalizacionEdit;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import java.util.Calendar;

public class ModificarUsuario extends AppCompatActivity {

    EditText numero, nombre, email, edad, meses, dui, direccion, password, repeatPassword;
    Button completeEdit, editMap;
    TextView mDisplayDate, tvLatitud, tvLongitud;
    ImageButton flecha, logout;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_usuario_principal);

        mDisplayDate = findViewById(R.id.EditFecha);
        mDisplayDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year1 = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    ModificarUsuario.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year1, month, day);
            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            private static final String TAG = "";

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date  = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        logout = findViewById(R.id.closeSesion);
        logout.setOnClickListener(v -> new FancyGifDialog.Builder(ModificarUsuario.this)
                .setTitle("¿Esta seguro que quiere cerrar la sesión?")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Si, cerrar sesión.")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.closession)
                .isCancellable(false)
                .OnPositiveClicked(() -> startActivity(new Intent(getApplicationContext(), Login.class)))
                .OnNegativeClicked(() -> Toast.makeText(ModificarUsuario.this,"Cancelado",Toast.LENGTH_LONG))
                .build());

        flecha = findViewById(R.id.flecha19);
        flecha.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DatosPrincipales.class)));

        tvLatitud = findViewById(R.id.longitudEdit);
        tvLatitud.setText(Login.latitud);
        tvLongitud = findViewById(R.id.latitudEdit);
        tvLongitud.setText(Login.longitud);

        numero = findViewById(R.id.EditNumero);
        numero.setText(Login.usuario);

        nombre = findViewById(R.id.EditNombre);
        nombre.setText(Login.nombre);

        email = findViewById(R.id.EditEmail);
        email.setText(Login.email);

        mDisplayDate.setText(Login.nacimiento);

        edad = findViewById(R.id.EditEdad);
        edad.setText(String.valueOf(Login.edad));

        meses = findViewById(R.id.EditMeses);
        meses.setText(String.valueOf(Login.meses));

        dui = findViewById(R.id.EditDui);
        dui.setText(String.valueOf(Login.dui));

        direccion = findViewById(R.id.EditDireccion);
        direccion.setText(Login.direccion);

        password = findViewById(R.id.EditPass);
        password.setText(Login.pass);

        repeatPassword = findViewById(R.id.EditRepeatPass);
        repeatPassword.setText(Login.repeatContra);

        editMap = findViewById(R.id.EditMap);
        editMap.setOnClickListener(v -> new FancyGifDialog.Builder(ModificarUsuario.this)
                .setTitle("Recuerda estar en tu domicilio antes de actualizar tu ubicación\n\n Al seleccionar 'Estoy en mi domicilio' aceptas los términos de política y privacidad'\n\nAsegurate de tener activa tu ubicación.")
                .setNegativeBtnText("No estoy en mi domicilio")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Estoy en mi domicilio")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.mapgif)
                .isCancellable(false)
                .OnPositiveClicked(this::iniciarLocalizacion)
                .OnNegativeClicked(() -> Toast.makeText(ModificarUsuario.this,"Cancelado",Toast.LENGTH_LONG))
                .build());

        completeEdit = findViewById(R.id.completeEdit);
        completeEdit.setOnClickListener(v -> {

            String number = numero.getText().toString();
            String name = nombre.getText().toString();
            String date = mDisplayDate.getText().toString();
            String age = edad.getText().toString();
            String month = meses.getText().toString();
            String id = dui.getText().toString();
            String pass = password.getText().toString();
            String passRepeat = repeatPassword.getText().toString();

            if (number.equals("")){
                Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
            }
            else if (name.equals("")){
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
            else if (id.equals("")){
                Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
            }
            else if (pass.equals("")){
                Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
            }
            else if (passRepeat.equals("")){
                Toast.makeText(this, "Parece que hay campos vacíos!", Toast.LENGTH_SHORT).show();
            } else {

                String url = "http://pedidoslab.6te.net/consultas/ModificarUsuario.php"
                        + "?login_usuario=" + numero.getText().toString()
                        + "&nombre_usuario=" + nombre.getText().toString()
                        + "&email_usuario=" + email.getText().toString()
                        + "&nacimiento_usuario=" + mDisplayDate.getText().toString()
                        + "&edad_usuario=" + edad.getText().toString()
                        + "&dui_usuario=" + dui.getText().toString()
                        + "&meses_usuario=" + meses.getText().toString()
                        + "&direccion_usuario=" + direccion.getText().toString()
                        + "&password_usuarios=" + password.getText().toString()
                        + "&password_repeat_usuario=" + repeatPassword.getText().toString()
                        + "&latitud=" + tvLatitud.getText().toString()
                        + "&longitud=" + tvLongitud.getText().toString()
                        + "&id_usuario=" + Login.gIdUsuario;
                ejecutarServicio(url);
                startActivity(new Intent(getApplicationContext(), DatosPrincipales.class));
            }
        });
    }

    public void ejecutarServicio (String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(getApplicationContext(), "INFORMACIÓN ACTUALIZADA CON ÉXITO", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void iniciarLocalizacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocalizacionEdit localizacion = new LocalizacionEdit();
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

    @Override
    public void onBackPressed() {

    }
}