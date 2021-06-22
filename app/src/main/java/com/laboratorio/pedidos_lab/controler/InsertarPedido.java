package com.laboratorio.pedidos_lab.controler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.laboratorio.pedidos_lab.adapters.adapProdReport;
import com.laboratorio.pedidos_lab.back.DatosPrincipales;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsertarPedido extends AsyncTask<String, Void, String> {

    Date d = new Date();
    SimpleDateFormat fecc = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault());
    String fechacComplString = fecc.format(d);
    SimpleDateFormat ho = new SimpleDateFormat("h:mm a");
    String horaString = ho.format(d);

    private final WeakReference<Context> context;

    public InsertarPedido(Context context) {
        this.context = new WeakReference<>(context);
    }

    protected String doInBackground (String...params){

        Login.gIdPedido = 0;

        String registrar_url = "http://pedidoslab.6te.net/consultas/insertarPedido.php"
                +"?nombre_reporte=" + DatosPrincipales.nombre
                +"&fecha_creo=" + fechacComplString + " a las " + horaString
                +"&id_cliente=" + Login.gIdCliente
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

            String nombreRep = DatosPrincipales.nombre;
            String fechaCreo = fechacComplString + horaString;
            String idCliente = String.valueOf(Login.gIdCliente);
            String monto = String.valueOf(adapProdReport.lNewDetMonto);
            String montoIva = String.valueOf(adapProdReport.lDetMontoIva);

            String data = URLEncoder.encode("nombre_reporte", "UTF-8") + "=" + URLEncoder.encode(nombreRep, "UTF-8")
                    + "&" + URLEncoder.encode("fecha_creo", "UTF-8") + "=" + URLEncoder.encode(fechaCreo, "UTF-8")
                    + "&" + URLEncoder.encode("id_cliente", "UTF-8") + "=" + URLEncoder.encode(idCliente, "UTF-8")
                    + "&" + URLEncoder.encode("monto", "UTF-8") + "=" + URLEncoder.encode((monto), "UTF-8")
                    + "&" + URLEncoder.encode("monto_iva", "UTF-8") + "=" + URLEncoder.encode((montoIva), "UTF-8");

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
            Login.gIdPedido = Integer.parseInt(responseJSON.getString("insert_id"));

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