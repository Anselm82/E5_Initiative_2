package es.usj.e5_initiative_2.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import es.usj.e5_initiative_2.data.DataHolder;

/**
 * Clase que provee de localización a la aplicación.
 *
 * Created by Juan José Hernández Alonso on 16/11/17.
 */
public class LocationUSJProvider {
    private LocationManager locManager;
    private LocationListener locListener;
    private Context ctx;

    /**
     * Constructor con los parámetros necesarios para el posicionamiento.
     * @param ctx Contexto o Activity de la aplicación.
     */
    public LocationUSJProvider(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Método que realiza la configuración del servicio de localización, comprobando permisos,
     * última posición conocida, inicializa el listener y establece la frecuencia
     * actualizaciones.
     */
    public void doGPSPositioning() {
        locManager =
                (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions((Activity) ctx, permissions, 1);
            return;
        }
        Location loc =
                locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        registerLocation(loc);
        locListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                registerLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle
                    extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                0, locListener);
    }

    /**
     * Método que almacena la ubicación en el singleton.
     * @param location Location con la ubicación recuperada.
     */
    public void registerLocation(Location location) {
        DataHolder.getInstance().put(DataHolder.LOCATION, location);
    }
}
