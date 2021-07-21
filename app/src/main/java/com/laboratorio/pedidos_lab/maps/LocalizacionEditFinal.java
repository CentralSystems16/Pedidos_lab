package com.laboratorio.pedidos_lab.maps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.laboratorio.pedidos_lab.front.AgregarDireccion;
import com.laboratorio.pedidos_lab.manage.ModificarUsuario;

public class LocalizacionEditFinal implements LocationListener {

    AgregarDireccion mainActivity;
    TextView tvLatitud, tvLongitud;
    public static String latitud1 = "0", longitud1 = "0";

    public void setMainActivity(AgregarDireccion mainActivity, TextView tvLatitud, TextView tvLongitud) {
        this.mainActivity = mainActivity;
        this.tvLatitud = tvLatitud;
        this.tvLongitud = tvLongitud;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        latitud1 = String.valueOf(location.getLatitude());
        longitud1 = String.valueOf(location.getLongitude());

        tvLatitud.setText(latitud1);
        tvLongitud.setText(longitud1);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        //tvMensaje.setText("GPS Activado");
    }

    @Override
    public void onProviderDisabled(String provider) {
        //tvMensaje.setText("GPS Desactivado");
    }
}
