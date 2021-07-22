package com.laboratorio.pedidos_lab.main;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.laboratorio.pedidos_lab.adapters.AdaptadorAllProductos;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.model.AllProductos;
import com.laboratorio.pedidos_lab.model.Productos;
import com.laboratory.views.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class ObtenerAllProductos extends AppCompatActivity {

    ImageButton botonContinuar, botonRegresar, microfono, btnScan;
    EditText etBuscador;
    RecyclerView rvLista = null;
    AdaptadorAllProductos adaptador = null;
    List<AllProductos> listaProductos;
    RequestQueue requestQueue11;
    public static final String URL_PRODUCTOS = "http://pedidoslab.6te.net/consultas/ObtenerTodosProd.php";
    public static TextView tvCantidadAllProductos;
    DecimalFormat formatoDecimal = new DecimalFormat("#");
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    TextView txtBarcode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_all_productos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtBarcode = findViewById(R.id.barcodetxtAll);

        microfono = findViewById(R.id.microfonoAll);

        btnScan = findViewById(R.id.barcodeAll);
        btnScan.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(ObtenerAllProductos.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt("Lector - CDP");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });

        tvCantidadAllProductos = findViewById(R.id.tvCantProductos2);
        tvCantidadAllProductos.setText(String.valueOf(formatoDecimal.format(gCount)));

        botonRegresar = findViewById(R.id.flecha3);
        botonRegresar.setOnClickListener(v -> {
            Intent i = new Intent(this, ObtenerCategorias.class);
            startActivity(i);
        });

        requestQueue11 = Volley.newRequestQueue(this);

        botonContinuar = findViewById(R.id.carrito_compra2);
        botonContinuar.setOnClickListener(v -> {
            if (gCount == 0) {
                Toast.makeText(this, "Por favor, agrege como mínimo un producto para continuar", Toast.LENGTH_SHORT).show();
            } else {
                TicketDatos.gTotal = 0.0;
                Intent i = new Intent(this, TicketDatos.class);
                startActivity(i);
            }
        });

        etBuscador = findViewById(R.id.etBuscador2);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rvLista.setVisibility(View.VISIBLE);
                filtrar(s.toString());
            }
        });

        rvLista = findViewById(R.id.rvLista2);

        rvLista.setLayoutManager(new GridLayoutManager(this, 3));

        listaProductos = new ArrayList<>();

        adaptador = new AdaptadorAllProductos(this, listaProductos);
        rvLista.setAdapter(adaptador);

        rvLista.setVisibility(View.GONE);

        obtenerProductos();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Reconocimiento de voz
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    etBuscador.setText(strSpeech2Text);
                }
                break;
            default:
                break;
        }

        //Codigo de barra y QR
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Lector cancelado", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                txtBarcode.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClickImgBtnHablar(View v) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void obtenerProductos() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espera...");
        progressDialog.show();
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTOS,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Productos");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            listaProductos.add(
                                    new AllProductos(
                                            jsonObject1.getInt("id_producto"),
                                            jsonObject1.getString("nombre_producto"),
                                            jsonObject1.getDouble("precio_producto"),
                                            jsonObject1.getInt("opciones"),
                                            jsonObject1.getString("referencia")));
                        }

                        adaptador = new AdaptadorAllProductos(this, listaProductos);
                        rvLista.setAdapter(adaptador);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }, Throwable::printStackTrace
        ) {
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    public void filtrar (String texto) {

        ArrayList<AllProductos> filtrarLista = new ArrayList<>();

        for (AllProductos usuario : listaProductos) {
            if (usuario.getNombreProducto().toLowerCase().contains(texto.toLowerCase())) {
                filtrarLista.add(usuario);
            }
        }

        adaptador.filtrar(filtrarLista);
    }

    public void onBackPressed(){

    }

    public void filtrar2(String texto) {
        ArrayList<AllProductos> filtrarLista = new ArrayList<>();

        for (AllProductos usuario : listaProductos) {
            if (usuario.getBarcode().toLowerCase().contains(texto.toLowerCase()) ) {
                filtrarLista.add(usuario);
            }

        }

        adaptador.filtrar(filtrarLista);
    }
}