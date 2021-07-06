package com.laboratorio.pedidos_lab.manage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laboratorio.pedidos_lab.back.Login;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

public class ModificarUsuario extends AppCompatActivity {

    EditText numero, nombre, email, edad, meses, dui, direccion;
    Button changePassword, completeEdit;
    TextView nacimiento;
    RadioGroup sexo;
    int gEstadoUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_usuario_principal);

        numero = findViewById(R.id.EditNumero);
        numero.setText(Login.usuario);

        nombre = findViewById(R.id.EditNombre);
        nombre.setText(Login.nombre);

        email = findViewById(R.id.EditEmail);
        email.setText(Login.email);

        nacimiento = findViewById(R.id.EditFecha);
        nacimiento.setText(Login.nacimiento);

        edad = findViewById(R.id.EditEdad);
        edad.setText(String.valueOf(Login.edad));

        meses = findViewById(R.id.EditMeses);
        meses.setText(String.valueOf(Login.meses));

        dui = findViewById(R.id.EditDui);
        dui.setText(String.valueOf(Login.dui));

        direccion = findViewById(R.id.EditDireccion);
        direccion.setText(Login.direccion);

        changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), changePass.class)));

        completeEdit = findViewById(R.id.completeEdit);
        completeEdit.setOnClickListener(v -> {
            String url = "http://pedidoslab.6te.net/consultas/ModificarUsuario.php"
                    + "?login_usuario=" + numero.getText().toString()
                    + "&nombre_usuario=" + nombre.getText().toString()
                    + "&email_usuario=" + email.getText().toString()
                    + "&nacimiento_usuario=" + nacimiento.getText().toString()
                    + "&edad_usuario=" + edad.getText().toString()
                    + "&estado_usuario=" + gEstadoUs
                    + "&dui_usuario=" + dui.getText().toString()
                    + "&meses_usuario=" +  meses.getText().toString()
                    + "&direccion_cliente=" + direccion.getText().toString()
                    + "&id_usuario=" + Login.gIdUsuario;
            ejecutarServicio(url);
            System.out.println("La URL es: " + url);

        });

    }

    public void ejecutarServicio (String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(getApplicationContext(), "INFORMACIÓN ACTUALIZADA CON ÉXITO", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}