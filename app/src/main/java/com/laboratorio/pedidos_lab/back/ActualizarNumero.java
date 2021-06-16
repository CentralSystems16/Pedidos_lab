package com.laboratorio.pedidos_lab.back;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratory.views.R;

public class ActualizarNumero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actualizar_numero);

        EditText etEditNumber = findViewById(R.id.newNumber);
        Button btnComplete = findViewById(R.id.btnActualizarNumero);

        btnComplete.setOnClickListener(v -> {

            String number = etEditNumber.getText().toString().trim();
            Intent i = new Intent(getApplicationContext(), VerificarNumero.class);
            i.putExtra("phoneNo", "+503"+number);
            startActivity(i);

            ejecutarServicio("http://pedidoslab.6te.net/consultas/actualizarNumero.php"
                                    + "?login_usuario=" + etEditNumber.getText().toString()
                                    + "&id_usuario=" + RegistroUsuario.gusuarioR);

        });
    }

    public void ejecutarServicio (String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(getApplicationContext(), "NÚMERO ACTUALIZADO CON ÉXITO", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}