package es.usj.e5_initiative_2.data;

import java.util.HashMap;

/**
 * Created by Anselm on 16/11/17.
 */

public class DataHolder {

    public static final String LOCATION = "location";

    private static DataHolder INSTANCE;
    private HashMap<String, Object> data;

    private DataHolder() {
        data = new HashMap<>();
    }

    public static DataHolder getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DataHolder();
        }
        return INSTANCE;
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(getInstance().data.get(key));
    }

    public void put(String key, Object value) {
        getInstance().data.put(key, value);
    }
}
