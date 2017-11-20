package es.usj.e5_initiative_2.model;

import com.google.maps.android.data.kml.KmlLayer;

import java.util.List;

public class Building {

    private transient KmlLayer layer;
    private List<Facility> facilities;
    private List<String> images;
    private String description;
    private String schedule;

}
