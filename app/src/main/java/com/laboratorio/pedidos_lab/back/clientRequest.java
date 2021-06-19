package com.laboratorio.pedidos_lab.back;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class clientRequest extends StringRequest {

    private final Map<String, String> params;

    public static final String URL = "http://pedidoslab.6te.net/consultas/ingresoOtraPersona.php";

    public clientRequest(String username, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("nombre_cliente", username);

    }

    @Override
    protected Map<String, String> getParams(){
        return params;

 }
}
