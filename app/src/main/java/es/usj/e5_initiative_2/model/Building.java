package es.usj.e5_initiative_2.model;

import com.google.maps.android.data.kml.KmlLayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Building implements Serializable {

    private int id;
    private String name;
    private transient KmlLayer layer;
    private List<Facility> facilities;
    private List<String> images;
    private String description;
    private String schedule;

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setLayer(KmlLayer layer) {
        this.layer = layer;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<Facility> getFacilities() {
        if (facilities == null) {
            facilities = new ArrayList<>();
        }
        return facilities;
    }

    public List<String> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    public String getDescription() {
        return description;
    }

    public KmlLayer getLayer() {
        return layer;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getName() {
        return name;
    }
}
