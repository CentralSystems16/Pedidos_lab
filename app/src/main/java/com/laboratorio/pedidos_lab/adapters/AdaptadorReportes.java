package com.laboratorio.pedidos_lab.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.laboratorio.pedidos_lab.back.Login;
import com.laboratorio.pedidos_lab.back.TicketDatos;
import com.laboratorio.pedidos_lab.main.ObtenerDetReporte;
import com.laboratorio.pedidos_lab.model.Reportes;
import com.laboratory.views.R;

import java.io.File;
import java.util.List;
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

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ReportesViewHolder> {

    Context cContext;
   public static List<Reportes> listaReportes;
    javax.mail.Session session;
    String NOMBRE_DOCUMENTO = "Examen.pdf", correo = "centralsystemsmailing@gmail.com", password="Nicole_07";

    public AdaptadorReportes(Context cContext, List<Reportes> listaReportes) {

        this.cContext = cContext;
        AdaptadorReportes.listaReportes = listaReportes;
    }

    @NonNull
    @Override
    public ReportesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_reportes, viewGroup, false);
        return new ReportesViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ReportesViewHolder reportesViewHolder, int posicion) {

        reportesViewHolder.tvNombre.setText(listaReportes.get(posicion).getNombre());
        reportesViewHolder.tvFecha.setText(listaReportes.get(posicion).getFecha());
        reportesViewHolder.tvPedido.setText(Integer.toString(listaReportes.get(posicion).getPedido()));

        reportesViewHolder.ver.setOnClickListener(v -> {

            Login.gIdPedido = listaReportes.get(posicion).getPedido();

            Intent i = new Intent(cContext, ObtenerDetReporte.class);
            cContext.startActivity(i);

            TicketDatos.gTotal = 0.00;

        });

        reportesViewHolder.reenviar.setOnClickListener(v -> {

            Login.gIdPedido = listaReportes.get(posicion).getPedido();

            AsyncTask.execute(() -> {

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

                        //TODO: Correo al que quiero enviar el Email
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("centralsystemsmanage2@gmail.com"));

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

            });

            Toast.makeText(cContext, "PDF reenviado, revise su bandeja de entrada.", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public static class ReportesViewHolder extends RecyclerView.ViewHolder {

   TextView tvNombre, tvFecha, tvPedido;
   Button reenviar, ver;

        public ReportesViewHolder(@NonNull View itemView) {
            super(itemView);

        tvNombre = itemView.findViewById(R.id.nombreCliente);
        tvFecha = itemView.findViewById(R.id.tvFecha);
        tvPedido = itemView.findViewById(R.id.numeroPedido);
        reenviar = itemView.findViewById(R.id.reenviarCorreo);
        ver = itemView.findViewById(R.id.verReporte);
        }
    }
}