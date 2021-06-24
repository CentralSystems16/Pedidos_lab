package com.laboratorio.pedidos_lab.back;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class userRequest extends StringRequest {

    private final Map<String, String> params;

    public static final String URL = "http://pedidoslab.6te.net/consultas/seleccionarUsuario.php";

    public userRequest(String username, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("login_usuario", username);

    }

    @Override
    protected Map<String, String> getParams(){
        return params;

 }
}