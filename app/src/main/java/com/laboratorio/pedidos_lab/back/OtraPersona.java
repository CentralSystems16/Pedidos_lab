package com.laboratorio.pedidos_lab.back;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.front.Lugar;
import com.laboratorio.pedidos_lab.front.SegundoRegistro;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratory.views.R;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Calendar;

public class OtraPersona extends AppCompatActivity {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otra_persona);
        //TODO: Bloquear orientación de pantalla.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Button botonContinuar = findViewById(R.id.botonContinuar);

        final EditText nom = findViewById(R.id.etNombreCompleto);
        final EditText ed = findViewById(R.id.etEdadClient);
        final EditText ema = findViewById(R.id.etEmail);
        final RadioGroup sexoClientes = findViewById(R.id.rgSexoClientes);
        final EditText mes = findViewById(R.id.etMesesClient);

        regresar = findViewById(R.id.botonRegres);
        regresar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DatosPrincipales.class);
            startActivity(i);
        });

        mDisplayDate = findViewById(R.id.tvDateCliente);
        mDisplayDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year1 = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    OtraPersona.this,
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

        botonContinuar.setOnClickListener(v -> {

            String nombre = nom.getText().toString();
            String edad = ed.getText().toString();
            String fechaNacimiento = mDisplayDate.getText().toString();
            String email = ema.getText().toString();
            String meses = mes.getText().toString();
            String usuario = String.valueOf(Login.gIdUsuario);

            int radiogroupSexo = sexoClientes.getCheckedRadioButtonId();
            String select = "";
            if (radiogroupSexo < 0){

            } else {
                View rbM = sexoClientes.findViewById(radiogroupSexo);
                int idx = sexoClientes.indexOfChild(rbM);
                RadioButton r = (RadioButton) sexoClientes.getChildAt(idx);
                select = r.getText().toString();
            }

            if (nombre.equals("")){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (edad.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (meses.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            else if (fechaNacimiento.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            } else if (select.isEmpty()){
                Toast.makeText(this, "Campos vacíos!", Toast.LENGTH_SHORT).show();
            }

            //TODO: Si todo salió bien se inserta en la base de datos el cliente con sus datos.
            else {
                new InsertarCliente(OtraPersona.this).execute(nombre, edad, select, email, fechaNacimiento, meses, usuario);
                Intent i = new Intent(getApplicationContext(), Lugar.class);
                startActivity(i);
            }
        });
    }

    public static class InsertarCliente extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> context;

        public InsertarCliente(Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String...params){

            String registrar_url = "http://pedidoslab.6te.net/consultas/clientes.php";
            String resultado = "";

            try {

                URL url = new URL(registrar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String nombre = params[0];
                int edad = Integer.parseInt(params[1]);
                String select = params[2];
                String email = params[3];
                String fechaNac = params[4];
                String meses = params[5];
                String usuario = params[6];

                String data = URLEncoder.encode("nombre_cliente", "UTF-8") + "=" + URLEncoder.encode(nombre, "UTF-8")
                        + "&" + URLEncoder.encode("edad_cliente", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(edad), "UTF-8")
                        + "&" + URLEncoder.encode("sexo_cliente", "UTF-8") + "=" + URLEncoder.encode(select, "UTF-8")
                        + "&" + URLEncoder.encode("email_cliente", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&" + URLEncoder.encode("nacimiento_cliente", "UTF-8") + "=" + URLEncoder.encode(fechaNac, "UTF-8")
                        + "&" + URLEncoder.encode("meses_cliente", "UTF-8") + "=" + URLEncoder.encode(meses, "UTF-8")
                        + "&" + URLEncoder.encode("id_usuario", "UTF-8") + "=" + URLEncoder.encode(usuario, "UTF-8");

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

                JSONObject responseJSON = new JSONObject(String.valueOf(stringBuilder));
                Login.gIdCliente = responseJSON.getInt("insert_id");

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultado;
        }
    }

    @Override
    public void onBackPressed(){

    }
}