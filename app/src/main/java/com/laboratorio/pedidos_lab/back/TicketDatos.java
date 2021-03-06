package com.laboratorio.pedidos_lab.back;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.laboratorio.pedidos_lab.adapters.AdaptadorCorreos;
import com.laboratorio.pedidos_lab.adapters.adapProdReport;
import com.laboratorio.pedidos_lab.controler.ActualizarPrefactura;
import com.laboratorio.pedidos_lab.controler.ContadorProductos;
import com.laboratorio.pedidos_lab.front.EnviandoTicket;
import com.laboratorio.pedidos_lab.front.Lugar;
import com.laboratorio.pedidos_lab.main.ObtenerAllProductos;
import com.laboratorio.pedidos_lab.main.ObtenerCategorias;
import com.laboratorio.pedidos_lab.model.Correos;
import com.laboratorio.pedidos_lab.model.DetReporte;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import pl.droidsonroids.gif.GifImageView;
import static com.laboratorio.pedidos_lab.controler.ContadorProductos.GetDataFromServerIntoTextView.gCount;

public class TicketDatos extends AppCompatActivity implements View.OnClickListener {

    public static String  url_pedido = "", url_det_pedido = "", envCorreo, gNombre, url_det_pedido_report = "http://pedidoslab.6te.net/consultas/ObtenerDetPedidoReport.php" + "?id_prefactura=" + Login.gIdPedido;
    TextView fechaReporte, totalItem, horaReporte, vaciarRecycler;
    public static TextView subTotalReporte, totalFinal, nombreTicket;
    public static Double gTotal = 0.00;
    RecyclerView rvProductos;
    adapProdReport adaptador;
    List<DetReporte> listaProdReport;
    List<Correos> listaCorreos;
    AdaptadorCorreos adaptadorCorreos;
    Button btnConfirmarEnviar, etBuscadorTicket;
    EditText buscadorPedido;
    javax.mail.Session session;
    int edad, meses;
    String sexo, password, correo, nacimiento, direccion, NOMBRE_DOCUMENTO = "Examen.pdf";
    ImageButton rOfTicket, home, flecha;
    Date d = new Date();
    SimpleDateFormat fecc = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault());
    String fechacComplString = fecc.format(d);
    SimpleDateFormat ho = new SimpleDateFormat("h:mm a");
    String horaString = ho.format(d);
    DecimalFormat formatoDecimal = new DecimalFormat("#.00");
    GifImageView carritoVacio;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticked_datos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buscadorPedido = findViewById(R.id.etBuscadorTicket);
        buscadorPedido.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ObtenerAllProductos.class)));

        flecha = findViewById(R.id.flecha222);

        flecha.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ObtenerCategorias.class)));

        carritoVacio = findViewById(R.id.carritoVacio);
        carritoVacio.setVisibility(View.GONE);

        vaciarRecycler = findViewById(R.id.vaciarRecycler);
        vaciarRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FancyGifDialog.Builder(TicketDatos.this)
                        .setTitle("¿Está seguro de vaciar la carretilla?")
                        .setNegativeBtnText("Cancelar")
                        .setPositiveBtnBackground(R.color.rosado)
                        .setPositiveBtnText("Vaciar")
                        .setNegativeBtnBackground(R.color.rojo)
                        .setGifResource(R.drawable.emptycart)
                        .isCancellable(false)
                        .OnPositiveClicked(() -> {
                            listaProdReport.clear();
                            adaptador.notifyDataSetChanged();
                            if (listaProdReport.isEmpty()){
                                gCount = 0.0;
                                gTotal = 0.0;
                                carritoVacio.setVisibility(View.VISIBLE);
                                btnConfirmarEnviar.setEnabled(false);
                                vaciarRecycler.setEnabled(false);
                            }
                        })
                        .OnNegativeClicked(() -> Toast.makeText(TicketDatos.this,"Cancelado",Toast.LENGTH_SHORT))
                        .build();
            }
        });

        home = findViewById(R.id.home2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(244, 67,54)));
                startActivity(new Intent(getApplicationContext(), DatosPrincipales.class));
            }
        });

        rOfTicket = findViewById(R.id.rOfTicket);
        rOfTicket.setOnClickListener(v -> {
            gTotal = 0.00;
            new ContadorProductos.GetDataFromServerIntoTextView(getApplicationContext()).execute();
            Intent i = new Intent(getApplicationContext(), ObtenerCategorias.class);
            startActivity(i);
        });

        nombreTicket = findViewById(R.id.nombreReporte);
        fechaReporte = findViewById(R.id.fechaReporte);
        subTotalReporte = findViewById(R.id.subTotalReporte);
        totalFinal = findViewById(R.id.TotalFinal);
        totalItem = findViewById(R.id.totalItem);
        horaReporte = findViewById(R.id.horaReporte);
        fechaReporte.setText((fechacComplString));
        horaReporte.setText(horaString);

        btnConfirmarEnviar = findViewById(R.id.aceptarEnviar);
        btnConfirmarEnviar.setOnClickListener(this);

        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        listaProdReport = new ArrayList<>();

        listaCorreos = new ArrayList<>();
        adaptadorCorreos = new AdaptadorCorreos(TicketDatos.this, listaCorreos);

        adaptador = new adapProdReport(TicketDatos.this, listaProdReport);
        rvProductos.setAdapter(adaptador);

        //TODO: Permisos para acceder al alamcenamiento interno del télefono
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        //TODO: Correo del que quiero enviar el Email
        correo = "centralsystemsmailing@gmail.com";
        password="Nicole_07";

        generarPedido();
        generarDetPedido();
        obtenerDetPedidoReport();
        obtenerCorreos();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }

    public void generarPedido() {
        url_pedido = "http://pedidoslab.6te.net/consultas/obtenerPedido.php" + "?id_prefactura=" + Login.gIdPedido;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url_pedido,

                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Reporte");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                          gNombre = jsonObject1.getString("nombre_cliente");
                          nombreTicket.setText(gNombre);
                          jsonObject1.getString("fecha_creo");
                          nacimiento = jsonObject1.getString("nacimiento_cliente");
                          edad = jsonObject1.getInt("edad_cliente");
                          sexo = jsonObject1.getString("sexo_cliente");
                          meses = jsonObject1.getInt("meses_cliente");
                          direccion = jsonObject1.getString("direccion_cliente");
                        }

                        System.out.println();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                ObtenerCategorias.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void generarDetPedido() {

        url_det_pedido = "http://pedidoslab.6te.net/consultas/obtenerDetPedido.php"+"?id_prefactura="+ Login.gIdPedido;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

          StringRequest stringRequest = new StringRequest(Request.Method.GET,url_det_pedido,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("Detreporte");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            Double totalFila = jsonObject1.getDouble("monto") + jsonObject1.getDouble("monto_iva");
                            gTotal = gTotal + totalFila;

                            subTotalReporte.setText(formatoDecimal.format(gTotal));
                            totalFinal.setText(formatoDecimal.format(gTotal));

                            listaProdReport.add(

                                    new DetReporte(

                                            jsonObject1.getInt("id_det_prefactura"),
                                            jsonObject1.getString("nombre_producto"),
                                            jsonObject1.getDouble("cantidad_producto"),
                                            jsonObject1.getDouble("precio_venta"),
                                            jsonObject1.getDouble("monto"),
                                            jsonObject1.getDouble("monto_iva")));

                        }

                        adaptador = new adapProdReport(TicketDatos.this, listaProdReport);
                        rvProductos.setAdapter(adaptador);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                ObtenerCategorias.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }


    public void obtenerDetPedidoReport() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url_det_pedido_report,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("DetDetalle");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                       jsonObject1.getString("nombre_producto");
                       jsonObject1.getDouble("cantidad_producto");
                       jsonObject1.getDouble("precio_venta");
                       jsonObject1.getDouble("monto_total");
                       jsonObject1.getDouble("iva_total");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace
        );

        requestQueue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createPDF() throws FileNotFoundException {

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, Login.gIdPedido + " Examen.pdf");

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new Document(pdfDocument);

        Drawable d = getDrawable(R.drawable.logologinpdf);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        com.itextpdf.layout.element.Image image = new Image(imageData);
        image.setHeight(100);
        image.setWidth(100);

        Drawable d2 = getDrawable(R.drawable.logologinmarca);
        Bitmap bitmap2 = ((BitmapDrawable)d2).getBitmap();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
        byte[] bitmapData2 = stream2.toByteArray();

        ImageData  imageData2 = ImageDataFactory.create(bitmapData2);
        Image image2 = new Image(imageData2);
        image2.setHeight(532);
        image2.setWidth(532);

        Drawable d3 = getDrawable(R.drawable.barcode);
        Bitmap bitmap3 = ((BitmapDrawable)d3).getBitmap();
        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3);
        byte[] bitmapData3 = stream3.toByteArray();

        ImageData imageData3 = ImageDataFactory.create(bitmapData3);
        Image image3 = new Image(imageData3);
        image3.setHeight(50);
        image3.setWidth(100);
        image3.setMarginLeft(398);
        image3.setMarginTop(-30);

        StringBuffer sb = new StringBuffer(18);
        for ( int i = 0; i < 7 ;i++) {
            sb.append("0");
        }
        String codigo = sb.toString();

        Paragraph pedido = new Paragraph( "Número de orden: " + codigo + Login.gIdPedido);

        Paragraph fecha = new Paragraph( "Fecha y hora de la orden: " + fechacComplString + " a las " + horaString);

        Paragraph nombre = new Paragraph( "Paciente: " + gNombre);

        Paragraph dui = new Paragraph( "Documento de identidad registrado: " + Login.dui);
        System.out.println("El dui del usuario es: " + Login.dui);

        Paragraph genero = new Paragraph( "Género: " + sexo);

        Paragraph fechaNac = new Paragraph( "Fecha de nacimiento: " + nacimiento);

        Paragraph edadCli = new Paragraph( "Edad: " + edad + " Año(s) " + "con " + meses + " Mes(es)");

        Paragraph direct = new Paragraph("Dirección: " + direccion);

        Paragraph orden = new Paragraph("Tipo de orden: " + Lugar.tipoPedido);

        float[] medidaCeldas = {0.78f, 2.40f, 1.40f, 0.63f};
        Table table = new Table(medidaCeldas);
        Border border1 = new GrooveBorder(2);

        table.addCell(new Cell().setBackgroundColor(new DeviceRgb(76,175,80)).setBorder(border1).add(new Paragraph("CANTIDAD").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().setBackgroundColor(new DeviceRgb(76,175,80)).setBorder(border1).add(new Paragraph("PRODUCTO").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().setBackgroundColor(new DeviceRgb(76,175,80)).setBorder(border1).add(new Paragraph("PRECIO UNITARIO").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().setBackgroundColor(new DeviceRgb(76,175,80)).setBorder(border1).add(new Paragraph("MONTO").setTextAlignment(TextAlignment.CENTER)));

        for(int i = 0 ; i < listaProdReport.size(); i++) {

            table.addCell(new Cell()
                    .add(new Paragraph(String.valueOf((adapProdReport.lCantProducto = listaProdReport.get(i).getCantiProd())))).setTextAlignment(TextAlignment.CENTER));

            table.addCell(new Cell()
                    .add(new Paragraph(listaProdReport.get(i).getNombreProducto()).setTextAlignment(TextAlignment.LEFT)));

            table.addCell(new Cell()
                    .add(new Paragraph("$ " + formatoDecimal.format(adapProdReport.lPrecioVta = listaProdReport.get(i).getPrecioVenta())).setTextAlignment(TextAlignment.RIGHT)));

            table.addCell(new Cell()
                    .add(new Paragraph("$ " + formatoDecimal.format(adapProdReport.lDetMonto = listaProdReport.get(i).getMonto() + listaProdReport.get(i).getMontoIva())).setTextAlignment(TextAlignment.RIGHT)));

        }

        table.addFooterCell(new Cell(0,6).add(new Paragraph("TOTAL: $ " + gTotal).setTextAlignment(TextAlignment.RIGHT)));

        Paragraph linea = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------");
        linea.setBackgroundColor(new DeviceRgb(76,175,80));
        linea.setFontColor(new DeviceRgb(76,175,80));
        linea.setMarginTop(400);
        linea.setPaddingTop(10);
        linea.setPaddingBottom(10);

        Paragraph direcion = new Paragraph("Dirección: Carretera internacional frente a ex caseta municipal 2201 Metapán, El Salvador");

        Paragraph telefono = new Paragraph("Teléfono: 2402-4817");

        Paragraph sitio = new Paragraph("Facebook: Laboratorio clínico LCB");

        Paragraph barcode = new Paragraph(codigo + Login.gIdPedido);
        barcode.setMarginLeft(420);
        barcode.setMarginTop(-40);

        document.add(image.setFixedPosition(400,700));
        document.add(image2.setFixedPosition(170,-10));
        document.add(pedido);
        document.add(fecha);
        document.add(nombre);
        document.add(dui);
        document.add(genero);
        document.add(edadCli);
        document.add(fechaNac);
        document.add(direct);
        document.add(orden);
        document.add(table.setFixedPosition(50,420,500));
        document.add(linea);
        document.add(direcion);
        document.add(telefono);
        document.add(sitio);
        document.add(image3);
        document.add(barcode);

        document.close();

    }

    public void obtenerCorreos() {

        System.out.println("Si estro al metodo");
        final String URL_CORREOS = "http://pedidoslab.6te.net/consultas/obtenerCorreos.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CORREOS,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Correos");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            new Correos( envCorreo = jsonObject1.getString("direccion_correo"));

                        }
                        System.out.println(envCorreo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace
        );

        requestQueue.add(stringRequest);

    }

    public void enviarPDF(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        try {
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, password);
                }
            });

            if (session!=null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("Nuevo pedido");


                message.addRecipients(Message.RecipientType.TO,  InternetAddress.parse("centralsystemsmanage2@gmail.com"));
                message.addRecipients(Message.RecipientType.TO,  InternetAddress.parse("adielmoranoficial@gmail.com"));
                message.addRecipients(Message.RecipientType.TO,  InternetAddress.parse("irvisadele999@gmail.com"));

                BodyPart bodyPart1 = new MimeBodyPart();
                bodyPart1.setText("Se ha adjuntado el pedido");
                MimeBodyPart bodyPart2 = new MimeBodyPart();

                String path = String.valueOf(new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + Login.gIdPedido + " Examen.pdf"))));

                FileDataSource source = new FileDataSource(path);
                bodyPart2.setDataHandler(new DataHandler(source));
                bodyPart2.setFileName(NOMBRE_DOCUMENTO);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(bodyPart1);
                multipart.addBodyPart(bodyPart2);

                message.setContent(multipart);

                Transport.send(message);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        new FancyGifDialog.Builder(this)
                .setTitle("Hola " + gNombre + " ¿Está seguro de confirmar la orden?, aún puede modificar su pedido")
                .setNegativeBtnText("Confirmar")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Cancelar")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.gif16)
                .isCancellable(false)
                .OnPositiveClicked(() -> {

                })
                .OnNegativeClicked(() ->{

                    try {
                        createPDF();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    enviarPDF();

                    new ActualizarPrefactura(this).execute();

                    Intent i = new Intent(getApplicationContext(), EnviandoTicket.class);
                    startActivity(i);
                    finish();

                })
                .build();

    }

    @Override
    public void onBackPressed(){

    }

}