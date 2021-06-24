package com.laboratorio.pedidos_lab.manage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratory.views.R;

public class ModificarCliente extends AppCompatActivity {

    EditText editNombre, editEdad, editMes, editFecha, editEmail;
    RadioGroup rgSexo;
    RequestQueue requestQueue;
    Button modificarCliente, regresarMod;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_usuario);

        editNombre = findViewById(R.id.etNomMod);
        editUsuario.setText(UsuarioFragment.gLoginUusario);

        editEdad = findViewById(R.id.etEdMod);
        editNombre.setText(UsuarioFragment.gNombreUsuario);

        editMes = findViewById(R.id.etMeMod);
        editPass.setText(UsuarioFragment.gPasswordUsuario);

        rgSexo = findViewById(R.id.rgSeMod);

        editFecha = findViewById(R.id.tvFecMod);
        editRepeatPass.setText(UsuarioFragment.gPasswordRepeatUsuario);

        editEmail = findViewById(R.id.etEmMod);
        editEmail.setText(UsuarioFragment.gEmailUsuario);

        modificarCliente = findViewById(R.id.btnContMod);
        modificarCliente.setOnClickListener(v -> ejecutar());

        regresarMod = findViewById(R.id.btnRegMod);

    }

    private void ejecutar(){

        ejecutarServicio(
                "http://pedidoslab.6te.net/consultas/ModificarCliente.php"
                        + "?nombre_cliente=" + editUsuario.getText().toString()
                        + "&edad_cliente=" + editNombre.getText().toString()
                        + "&sexo_cliente=" + editPass.getText().toString()
                        + "&email_cliente=" + editRepeatPass.getText().toString()
                        + "&email_usuario=" + editEmail.getText().toString()
                        + "&estado_usuario=" + gEstadoUs
                        + "&id_usuario=" + UsuarioFragment.gIdUsuario);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    public void ejecutarServicio (String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(getApplicationContext(), "USUARIO ACTUALIZADO CON ÉXITO", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}