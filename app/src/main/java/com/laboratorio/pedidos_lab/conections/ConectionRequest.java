package com.laboratorio.pedidos_lab.conections;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class ConectionRequest extends StringRequest {

    private Map<String, String> mParams;

    public ConectionRequest(String base, Response.Listener<String> listener) {
        super(Method.POST, "http://pedidoslab.6te.net/consultas/obtenerEmpresa.php", listener, null);
        mParams = new HashMap<String, String>();
        mParams.put("base", base);

    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }
}
