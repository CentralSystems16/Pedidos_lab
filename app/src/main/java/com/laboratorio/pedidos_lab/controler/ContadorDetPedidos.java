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

public class ContadorDetPedidos extends AsyncTask<String, Void, String> {

    private final WeakReference<Context> context;

    public ContadorDetPedidos(Context context) {

        this.context = new WeakReference<>(context);
    }

    protected String doInBackground(String... params) {

        //String contador_url = "pedidoslab.atspace.cc/contadorProdPedidos.php" + "?id_prefactura=" + Login.gIdPedido;
        String contador_url = "http://pedidoslab.6te.net/consultas/contadorProdPedidos.php" + "?id_prefactura=" + Login.gIdPedido;

        String resultado;

        try {
            System.out.println("Entro al try catch");
            URL url = new URL(contador_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            String idPrefactura = String.valueOf(Login.gIdPedido);
            System.out.println("Entro a la variable");
            String data = URLEncoder.encode("id_prefactura", "UTF-8") + "=" + URLEncoder.encode(idPrefactura, "UTF-8");
            System.out.println("Data: " + data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            System.out.println("Paso de las variables");
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
        System.out.println("Finalizo con exito!");
        return resultado;
    }
}

