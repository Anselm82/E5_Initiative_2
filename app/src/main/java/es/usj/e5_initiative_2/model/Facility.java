package es.usj.e5_initiative_2.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Anselm on 15/11/17.
 */

public class Facility implements ClusterItem {
    private LatLng position;
    private String title, snippet;

    public Facility(LatLng position, String title, String snippet){
        this.position = position;
        this.title = title;
        this.snippet = snippet;
    }

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
}
