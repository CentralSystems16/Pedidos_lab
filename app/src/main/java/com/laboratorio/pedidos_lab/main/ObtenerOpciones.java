package com.laboratorio.pedidos_lab.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.adapters.AdaptadorOpciones;
import com.laboratorio.pedidos_lab.model.Opciones;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ObtenerOpciones extends AppCompatActivity {

    RecyclerView rvOpciones = null;
    AdaptadorOpciones adaptador = null;
    List<Opciones> listaOpciones;
    //public static final String URL_OPCIONES = "http://pedidoslab.atspace.cc/obtenerOpciones.php";
    public static final String URL_OPCIONES = "http://pedidoslab.6te.net/consultas/obtenerOpciones.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_opciones);

        rvOpciones = findViewById(R.id.rvListaOpciones);
        rvOpciones.setLayoutManager(new GridLayoutManager(this, 3));

        listaOpciones = new ArrayList<>();

        adaptador = new AdaptadorOpciones(ObtenerOpciones.this, listaOpciones);
        rvOpciones.setAdapter(adaptador);

        obtenerOpciones();
    }

    public void obtenerOpciones() {

        String url = URL_OPCIONES + "?id_producto=" + ObtenerProductos.gIdProducto;
        System.out.println("Entro al metodo obtener opciones: " + url);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Opciones");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            listaOpciones.add(
                                    new Opciones(

                                            jsonObject1.getInt("id_opcion"),
                                            jsonObject1.getString("nombre_opcion"),
                                            jsonObject1.getInt("id_producto")));

                        }

                        adaptador = new AdaptadorOpciones(ObtenerOpciones.this, listaOpciones);
                        rvOpciones.setAdapter(adaptador);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace
        ) {};

        requestQueue.add(stringRequest);

    }
}
