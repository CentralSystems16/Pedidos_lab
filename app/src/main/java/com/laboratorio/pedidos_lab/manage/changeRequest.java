package com.laboratorio.pedidos_lab.manage;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class changeRequest extends StringRequest {

    private final Map<String, String> params;

    public static final String URL = "http://pedidoslab.6te.net/consultas/actuallizarPassword.php";

    public changeRequest(String password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("password_usuarios", password);

    }

    @Override
    protected Map<String, String> getParams(){
        return params;

 }
}
