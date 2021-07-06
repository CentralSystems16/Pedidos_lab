package com.laboratorio.pedidos_lab.back;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.maps.Localizacion;
import com.laboratorio.pedidos_lab.maps.MainActivity;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity {

    TextView mDisplayDate, errorPass, errorEdad2, errorNumber;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    RequestQueue requestQueue;
    String select;
    public static EditText regPhoneNo;
    EditText pas, nom, pr, em, ed, mes, dui, dir;
    RadioGroup rg;
    int errorEdad, meses, number;
    Button maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);
        //TODO: Bloquear orientación de pantalla.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Button botonRegistrar = findViewById(R.id.btnRegistrarUsuario);
        final Button botonCancelar = findViewById(R.id.btnCancelarRegistro);

        errorPass = findViewById(R.id.errorPass);
        errorEdad2 = findViewById(R.id.errorEdad);
        errorNumber = findViewById(R.id.errorNumber);

        regPhoneNo = findViewById(R.id.campoTelefono);
        pas = findViewById(R.id.campoPass);
        nom = findViewById(R.id.campoNombre);
        pr = findViewById(R.id.campoPassRepeat);
        em = findViewById(R.id.campoEmail);
        ed = findViewById(R.id.campoEdad);
        rg = findViewById(R.id.rgSexo);
        dui = findViewById(R.id.campoDui);
        mes = findViewById(R.id.campoMeses);
        dir = findViewById(R.id.campoDireccion);
        mDisplayDate = findViewById(R.id.tvDate);

        maps = findViewById(R.id.DirectMaps);
        maps.setOnClickListener(v -> new FancyGifDialog.Builder(RegistroUsuario.this)
                .setTitle("Esta función obtiene su ubicación exacta, por lo tanto se recomienda estar en su domicilio para mejor efectividad de obtención de datos\n\nTambién asegurate de activar tu GPS para obtener tu ubicación exacta.")
                .setNegativeBtnText("No estoy en mi domicilio")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Estoy en mi domicilio")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.mapgif)
                .isCancellable(false)
                .OnPositiveClicked(() -> startActivity(new Intent(RegistroUsuario.this, MainActivity.class)))
                .OnNegativeClicked(() -> Toast.makeText(RegistroUsuario.this,"Cancelado, puede onmitir esta opción y agregarla cuando sea requerida",Toast.LENGTH_LONG).show())
                .build());

        regPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String number1 = regPhoneNo.getText().toString();

                try {
                    number = Integer.parseInt(number1);

                    if (regPhoneNo.length()<8 || regPhoneNo.length()>8){
                        errorNumber.setText("El numero no es correcto");
                    } else if (regPhoneNo.length() == 8){
                        errorNumber.setText("");
                    }
                }catch (NumberFormatException e)
                {
                    // handle the exception
                }
            }
        });

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String edad = ed.getText().toString();

                try
                {
                    errorEdad = Integer.parseInt(edad);

                    if (errorEdad < 18){
                        errorEdad2.setText(R.string.edadError);
                    } else if (errorEdad > 17){
                        errorEdad2.setText("");
                    }
                }
                catch (NumberFormatException e)
                {
                    // handle the exception
                }
            }
        });

        mes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String me = mes.getText().toString();

                try
                {
                    meses = Integer.parseInt(me);

                    if (meses < 1 || meses > 11){
                        errorEdad2.setText("El rango de meses debe estar entre 1 y 11");
                    } else if (meses > 0 || meses < 11){
                        errorEdad2.setText("");
                    }
                }
                catch (NumberFormatException e)
                {
                    // handle the exception
                }

            }
        });

        pr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String password = pas.getText().toString();
                String passwordRepeat = pr.getText().toString();

                if (password.equals(passwordRepeat)) {
                    errorPass.setText("");
                } else {
                    errorPass.setText(R.string.nocoicide);
                }

            }
        });

        mDisplayDate.setOnClickListener(v -> {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = null;
            try {
                date = sdf.parse("2003/07/01");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    RegistroUsuario.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            private static final String TAG = "";

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                Log.d(TAG, "onDateSet: d/MMMM/yyyy: " + day + month  + year);

                String date  = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        botonRegistrar.setOnClickListener(v -> {

            String nombre = nom.getText().toString();
            String edad = ed.getText().toString();
            String fechaNacimiento = mDisplayDate.getText().toString();
            String usuario = regPhoneNo.getText().toString();
            String password = pas.getText().toString();
            String passwordRepeat = pr.getText().toString();
            String id = dui.getText().toString();
            String me = mes.getText().toString();
            String di = dir.getText().toString();

            int radiogroupSexo = rg.getCheckedRadioButtonId();
            if (radiogroupSexo < 0){

            } else {
                View rbM = rg.findViewById(radiogroupSexo);
                int idx = rg.indexOfChild(rbM);
                RadioButton r = (RadioButton) rg.getChildAt(idx);
                select = r.getText().toString();
            }

            if (nombre.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (id.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (me.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (di.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (usuario.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (password.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (passwordRepeat.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (password.length() < 6){
                Toast.makeText(this, "La contraseña debe contener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
            }

            else if (!password.equals(passwordRepeat)){
                Toast.makeText(this, "Las contraseñas no coiciden", Toast.LENGTH_SHORT).show();
            }

            else if (edad.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (fechaNacimiento.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();

            }

            else if (select.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (errorEdad < 18){
                Toast.makeText(this, "Debe ser mayor de 18 años", Toast.LENGTH_SHORT).show();
            }

            else if (meses < 1 || meses > 11){
                Toast.makeText(this, "El rango de meses debe estar entre 1 y 11", Toast.LENGTH_SHORT).show();

            } else if (regPhoneNo.length()<8 || regPhoneNo.length()>8){
                Toast.makeText(this, "Parece que el numero no es correcto.", Toast.LENGTH_SHORT).show();
            }

            else {
                String phoneNo = regPhoneNo.getText().toString();
                Intent i = new Intent(getApplicationContext(), VerificarNumero.class);
                i.putExtra("phoneNo", phoneNo);
                startActivity(i);
                ejecutarServicio("http://pedidoslab.6te.net/consultas/registro.php");
            }

        });

        botonCancelar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        });
    }

    public void ejecutarServicio(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

        },
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() {

                String usuario = regPhoneNo.getText().toString();
                String password = pas.getText().toString();
                String nombre = nom.getText().toString();
                String fechaNacimiento = mDisplayDate.getText().toString();
                String passwordRepeat = pr.getText().toString();
                String email = em.getText().toString();
                String edad = ed.getText().toString();
                String id = dui.getText().toString();
                String me = mes.getText().toString();
                String di = dir.getText().toString();
                String latitud = Localizacion.latitud1;
                String longitud = Localizacion.longitud1;

                int radiogroupSexo = rg.getCheckedRadioButtonId();
                if (radiogroupSexo < 0){

                } else {
                    View rbM = rg.findViewById(radiogroupSexo);
                    int idx = rg.indexOfChild(rbM);
                    RadioButton r = (RadioButton) rg.getChildAt(idx);
                    select = r.getText().toString();
                }

                Map<String, String> parametros = new HashMap<>();

                parametros.put("login_usuario", usuario);
                parametros.put("nombre_usuario", nombre);
                parametros.put("password_usuarios", password);
                parametros.put("password_repeat_usuario", passwordRepeat);
                parametros.put("email_usuario", email);
                parametros.put("edad_usuario", edad);
                parametros.put("nacimiento_usuario", fechaNacimiento);
                parametros.put("dui_usuario", id);
                parametros.put("meses_usuario", me);
                parametros.put("sexo_usuario", select);
                parametros.put("direccion_usuario", di);
                parametros.put("latitud", latitud);
                parametros.put("longitud", longitud);

                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {

    }
}