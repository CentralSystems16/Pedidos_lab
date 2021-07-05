package com.laboratorio.pedidos_lab.controler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.laboratorio.pedidos_lab.back.Login;

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

public class MiPersona {

    public static Boolean exito = false;

    public static class InsertarClienteMiPersona extends AsyncTask<String, Void, String> {
        private final WeakReference<Context> context;
        int insertId = 0;

        public InsertarClienteMiPersona(Context context) {
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

                String nombre = Login.nombre;
                String edad = String.valueOf(Login.edad);
                String select = Login.sexo;
                String email = Login.email;
                String nacimiento = Login.nacimiento;
                int meses = Login.meses;
                int usuario = Login.gIdUsuario;
                String direccion = Login.direccion;
                String latitud = Login.latitud;
                String longitud = Login.longitud;

                String data = URLEncoder.encode("nombre_cliente", "UTF-8") + "=" + URLEncoder.encode(nombre, "UTF-8")
                        + "&" + URLEncoder.encode("edad_cliente", "UTF-8") + "=" + URLEncoder.encode(edad, "UTF-8")
                        + "&" + URLEncoder.encode("sexo_cliente", "UTF-8") + "=" + URLEncoder.encode(select, "UTF-8")
                        + "&" + URLEncoder.encode("email_cliente", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&" + URLEncoder.encode("nacimiento_cliente", "UTF-8") + "=" + URLEncoder.encode(nacimiento, "UTF-8")
                        + "&" + URLEncoder.encode("meses_cliente", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(meses), "UTF-8")
                        + "&" + URLEncoder.encode("id_usuario", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(usuario), "UTF-8")
                        + "&" + URLEncoder.encode("direccion_cliente", "UTF-8") + "=" + URLEncoder.encode(direccion, "UTF-8")
                        + "&" + URLEncoder.encode("latitud", "UTF-8") + "=" + URLEncoder.encode(latitud, "UTF-8")
                        + "&" + URLEncoder.encode("longitud", "UTF-8") + "=" + URLEncoder.encode(longitud, "UTF-8");

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
                insertId = responseJSON.getInt("insert_id");

               if(insertId > 0) {
                    Login.gIdCliente = insertId;

                    exito = true;
               }else{
                    exito = false;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resultado;

        }

       /* public void onPostExecute (String resultado){
            Toast.makeText(context.get(),  resultado, Toast.LENGTH_LONG).show();
        } */
    }
}


