package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.laboratorio.pedidos_lab.conections.ConectionRequest;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;
import com.laboratorio.pedidos_lab.main.ObtenerNegocios;
import com.laboratorio.pedidos_lab.model.Negocios;
import com.laboratory.views.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AdaptadorNegocios extends RecyclerView.Adapter<AdaptadorNegocios.NegociosViewHolder> {

    Context cContext;
   public static List<Negocios> listaNegocios;
    Response.Listener<String> responseListener;

    public AdaptadorNegocios(Context cContext, List<Negocios> listaNegocios) {

        this.cContext = cContext;
        AdaptadorNegocios.listaNegocios = listaNegocios;
    }

    @NonNull
    @Override
    public NegociosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_negocios, viewGroup, false);
        return new NegociosViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull NegociosViewHolder negociosViewHolder, int posicion) {

        Negocios negocios = listaNegocios.get(posicion);

        negociosViewHolder.tvNegocio.setText(listaNegocios.get(posicion).getNombre());
        Glide.with(cContext).load(negocios.getImagen()).into(negociosViewHolder.imgNegocio);


        if (posicion == 0){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(121, 200,67)));
        }

        if (posicion == 1){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(226, 109,194)));
        }

        if (posicion == 2){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(82, 175,221)));
        }

        if (posicion == 3){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 4){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 5){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 6){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 7){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 8){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        if (posicion == 9){
            negociosViewHolder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30,99)));
        }

        negociosViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtener el id del negocio
                ObtenerNegocios.idNegocio = listaNegocios.get(posicion).getIdNegocio();

                String root = "localhost";
                String usuario = "215714";
                String password = "Nicole_07";
                String base = "215714db3";

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {

                    URL url = new URL("http://pedidoslab.6te.net/consultas/conn.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("root", root));
                    params.add(new BasicNameValuePair("usuario", usuario));
                    params.add(new BasicNameValuePair("password", password));
                    params.add(new BasicNameValuePair("base", base));

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(params));
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();
                }catch (IOException e){

                }
                System.out.println("El id de negocio es: " + ObtenerNegocios.idNegocio);

                //Intencion al splash dependiendo del id del negocio y la conexion
                cContext.startActivity(new Intent(cContext, SplashPrincipal.class));

            }
        });
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    public int getItemCount() {
        return listaNegocios.size();
    }

    public static class NegociosViewHolder extends RecyclerView.ViewHolder {

        ImageView imgNegocio;
        TextView tvNegocio;

        public NegociosViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNegocio = itemView.findViewById(R.id.imgNegocio);
            tvNegocio = itemView.findViewById(R.id.nombreNegocio);


        }
    }
}