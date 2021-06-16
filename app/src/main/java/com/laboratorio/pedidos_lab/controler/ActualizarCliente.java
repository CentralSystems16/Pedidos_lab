package com.laboratorio.pedidos_lab.controler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.laboratorio.pedidos_lab.back.Login;
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

public class ActualizarCliente {

    public static class Actualizar extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> context;

        public Actualizar (Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground (String...params){
            String registrar_url = "http://pedidoslab.6te.net/consultas/actualizarClienteUsuario.php" + "?id_cliente=" + Login.gIdCliente + "&id_usuario=" + Login.gIdUsuario;
            String resultado;

            try {

                URL url = new URL(registrar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String idCliente = String.valueOf(Login.gIdCliente);
                String idUsuario = String.valueOf(Login.gIdUsuario);

                String data = URLEncoder.encode("id_cliente", "UTF-8") + "=" + URLEncoder.encode(idCliente, "UTF-8")
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
