package com.laboratorio.pedidos_lab.maps;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.laboratory.views.R;

public class Localizacion extends FragmentActivity implements LocationListener {

    MainActivity mainActivity;
    TextView latitud, longitud;
    public static String latitud1, longitud1;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity, TextView latitud, TextView longitud) {
        this.mainActivity = mainActivity;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Este metodo se ejecuta cuando el GPS recibe nuevas coordenadas
         latitud1 = String.valueOf(location.getLatitude());
         longitud1 = String.valueOf(location.getLongitude());

        latitud.setText(latitud1);
        longitud.setText(longitud1);

        mapa(location.getLatitude(), location.getLongitude());
    }

    public void mapa(double lat, double lon) {
        // Fragment del Mapa
        FragmentMaps fragment = new FragmentMaps();

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", new Double(lat));
        bundle.putDouble("lon", new Double(lon));
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getMainActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment, fragment, null);
        fragmentTransaction.commit();
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
