package com.laboratorio.pedidos_lab.controler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.main.ObtenerProductos;
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


public class InsertarDetPedido extends AsyncTask<String, Void, String> {

public static boolean exitoInsertProd= false;

private final WeakReference<Context> context;


public InsertarDetPedido (Context context) {
    this.context = new WeakReference<>(context);
}

@SuppressLint("WrongThread")
protected String doInBackground (String...params){

    Login.gIdFacDetPedido = 0;
    //String registrar_url = "http://pedidoslab.atspace.cc/insertarDetPedido.php"
    String registrar_url = "http://pedidoslab.6te.net/consultas/insertarDetPedido.php"
            +"?id_prefactura=" + Login.gIdPedido
            +"&id_producto=" + ObtenerProductos.gIdProducto
            +"&precio_venta=" + ObtenerProductos.gPrecio
            +"&monto=" + ObtenerProductos.gDetMonto
            +"&monto_iva=" + ObtenerProductos.gDetMontoIva;

    String resultado = null;

    try {

        URL url = new URL(registrar_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        String idPrefactura = String.valueOf(Login.gIdPedido);
        String idProducto = String.valueOf(ObtenerProductos.gIdProducto);
        String precioVenta = String.valueOf(ObtenerProductos.gPrecio);
        String monto = String.valueOf(ObtenerProductos.gDetMonto);
        String montoIva = String.valueOf(ObtenerProductos.gDetMontoIva);


        String data = URLEncoder.encode("id_prefactura", "UTF-8") + "=" + URLEncoder.encode(idPrefactura, "UTF-8")
                + "&" + URLEncoder.encode("id_examen", "UTF-8") + "=" + URLEncoder.encode(idProducto, "UTF-8")
                + "&" + URLEncoder.encode("precio_venta", "UTF-8") + "=" + URLEncoder.encode(precioVenta, "UTF-8")
                + "&" + URLEncoder.encode("monto", "UTF-8") + "=" + URLEncoder.encode(monto, "UTF-8")
                + "&" + URLEncoder.encode("monto_iva", "UTF-8") + "=" + URLEncoder.encode(montoIva, "UTF-8");

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
        Login.gIdFacDetPedido= responseJSON.getInt("insert_id");

        // Condicion para insetar det_pedido si el insertar de pedido fue correcto
        if(Login.gIdFacDetPedido > 0) {
            exitoInsertProd = true;
        }else{
            exitoInsertProd = false;
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
}
