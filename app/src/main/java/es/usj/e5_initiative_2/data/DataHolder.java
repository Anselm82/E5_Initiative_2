package es.usj.e5_initiative_2.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.usj.e5_initiative_2.model.Building;
import es.usj.e5_initiative_2.model.Facility;

/**
 * Created by Anselm on 16/11/17.
 */

public class DataHolder {

    public static final String LOCATION = "location";
    public static final String FACILITIES = "facilities";
    public static final String BUILDINGS = "buildings";
    public final static String IMAGES = "images";

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
        HashMap<String, Building> buildings = new HashMap<>();
        for(int i = 0; i < 3; i++){
            Building b = new Building();
            b.setId(i);
            b.getImages().add("https://lh5.googleusercontent.com/Mu1M6K6YWXfqhKy5k7MzyVY-gUCrkOs-ZZzCZKvayZSivZ4PwEfsRRU1BWGb1sOzbqzQTSlaDqQd24p8s7IKQ1i4vXFv6z_rGuBStt3RUs2oGO_itma5lNr5vfCFjWICRsT22Ss");
            b.getImages().add("https://lh5.googleusercontent.com/Mu1M6K6YWXfqhKy5k7MzyVY-gUCrkOs-ZZzCZKvayZSivZ4PwEfsRRU1BWGb1sOzbqzQTSlaDqQd24p8s7IKQ1i4vXFv6z_rGuBStt3RUs2oGO_itma5lNr5vfCFjWICRsT22Ss");
            b.setName("Edificio Rectorado");
            b.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            b.setDescription( b.getDescription() + b.getDescription());
            b.setSchedule("24/7");
            buildings.put(b.getName(), b);
        }
        put(BUILDINGS, buildings);
        List<Facility> facilities = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Facility f = new Facility(new LatLng(getRandomLat(), getRandomLng()), "POI " + i, "Snippet " + i);
            facilities.add(f);
            Building b = (Building) get(BUILDINGS, HashMap.class).values().toArray()[((int)Math.random() * 3)];
            b.getFacilities().add(f);
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
