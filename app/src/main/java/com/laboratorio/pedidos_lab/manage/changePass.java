package com.laboratorio.pedidos_lab.manage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.adapters.adapProdReport;
import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.loginRequest;
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

public class changePass extends AppCompatActivity {

    EditText etCurrentPass, newPass, newPassRepeat;
    Button btnConfirmChange;
    String contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        etCurrentPass = findViewById(R.id.currentPass);
        newPass = findViewById(R.id.newPass);
        newPassRepeat = findViewById(R.id.newRepeatPass);
        btnConfirmChange = findViewById(R.id.confrmCambio);

        btnConfirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contra = etCurrentPass.getText().toString();
                Response.Listener<String> responseListener = response -> {

                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        boolean succes = jsonResponse.getBoolean("success");



                        if (succes){

                            new ActualizarPass(getApplicationContext()).execute();

                            Toast.makeText(changePass.this, "Contraseña actualizada con éxito!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), DatosPrincipales.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "La contraseña anterior es incorrecta", Toast.LENGTH_SHORT).show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                };

                changeRequest changeRequest = new changeRequest(contra, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(changeRequest);
            }
        });
    }

    public class ActualizarPass extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> context;

        public ActualizarPass (Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String...params){
            String url_actualizar = "http://pedidoslab.6te.net/consultas/changePass.php"
                    + "?password_usuarios=" + newPass.getText().toString()
                    + "&password_repeat_usuario=" + newPassRepeat.getText().toString()
                    + "&id_usuario=" + Login.gIdUsuario;

            String resultado;

            try {

                URL url = new URL(url_actualizar);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String newPassword = newPass.getText().toString();
                String newRepeatPassword = newPassRepeat.getText().toString();
                String idUsuario = String.valueOf(Login.gIdUsuario);

                String data = URLEncoder.encode("password_usuarios", "UTF-8") + "=" + URLEncoder.encode(newPassword, "UTF-8")
                        + "&" + URLEncoder.encode("password_repeat_usuario", "UTF-8") + "=" + URLEncoder.encode(newRepeatPassword, "UTF-8")
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


}