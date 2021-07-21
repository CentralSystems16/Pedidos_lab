package com.laboratorio.pedidos_lab.controler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.laboratorio.pedidos_lab.back.DatosPrincipales;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.main.ObtenerProductos;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.DecimalFormat;

public class ContadorProductos {

    public static class GetDataFromServerIntoTextView extends AsyncTask<Void, Void, Void> {

        @SuppressLint("StaticFieldLeak")
        public Context context;
        HttpResponse httpResponse;
        JSONArray jsonObject = null;
        String StringHolder = "" ;
        String contador_url = "http://pedidoslab.6te.net/consultas/contadorProdPedidos.php" + "?id_prefactura=" + Login.gIdPedido;
        public static Double gCount = 0.0;
        DecimalFormat formatoDecimal = new DecimalFormat("#");

        public GetDataFromServerIntoTextView(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(contador_url);

            try {
                httpResponse = httpClient.execute(httpPost);

                StringHolder = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            } catch (IOException e) {
                e.printStackTrace();
            }

            try{

                JSONArray jsonArray = new JSONArray(StringHolder);
                jsonObject = jsonArray.getJSONArray(0);

            } catch ( Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {

            try {

                JSONObject responseJSON = new JSONObject(String.valueOf(StringHolder));
                gCount = (responseJSON.getJSONArray("voto").getJSONObject(0).getDouble("count"));
                ObtenerProductos.tvCantProductos.setText(String.valueOf(formatoDecimal.format(gCount)));
                ObtenerCategorias.tvCantProd3.setText(String.valueOf(formatoDecimal.format(gCount)));
                DatosPrincipales.tvCantProductos4.setText(String.valueOf(formatoDecimal.format(gCount)));

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }
}