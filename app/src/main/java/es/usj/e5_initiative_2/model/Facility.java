package es.usj.e5_initiative_2.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Clase que define las instalaciones de los edificios. Serializable por venir de un JSON.
 * ClusterItem para que nos permita la visualización en cluster en el mapa.
 *
 * Created by Juan José Hernández Alonso on 17/11/17.
 */
public class Facility implements ClusterItem, Serializable {

    /**
     * Posición con el formato directo para el marcador de Gmap.
     */
    private LatLng position;
    /**
     * Cadenas con los datos que podremos mostar, normalmente title = nombre de la instalación,
     * snippet = pequeña descripción
     */
    private String title, snippet;

    public Facility(LatLng position, String title, String snippet){
        this.position = position;
        this.title = title;
        this.snippet = snippet;
    }

    // GETTERS
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
