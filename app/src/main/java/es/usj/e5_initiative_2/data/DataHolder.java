package es.usj.e5_initiative_2.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.usj.e5_initiative_2.model.Facility;

/**
 * Created by Anselm on 16/11/17.
 */

public class DataHolder {

    public static final String LOCATION = "location";
    public static final String FACILITIES = "facilities";

    private static DataHolder INSTANCE;
    private HashMap<String, Object> data;

    private DataHolder() {
        data = new HashMap<>();
    }

    public static DataHolder getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DataHolder();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(getInstance().data.get(key));
    }

    public void put(String key, Object value) {
        getInstance().data.put(key, value);
    }

    private void init(){
        List<Facility> facilities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Facility f = new Facility(new LatLng(getRandomLat(), getRandomLng()), "POI " + i, "Snippet " + i);
            facilities.add(f);
        }
        put(FACILITIES, facilities);
    }

    private double getRandomLng() {
        return getRandomNum(-0.837628, -0.831057);
    }

    private double getRandomLat() {
        return getRandomNum(41.755803, 41.757872);
    }

    private double getRandomNum(double min, double max){
        double value;
        do {
            value = min + Math.random();
        } while(value > max);
        return value;
    }
}
