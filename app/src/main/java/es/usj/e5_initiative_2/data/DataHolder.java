package es.usj.e5_initiative_2.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.usj.e5_initiative_2.model.Building;
import es.usj.e5_initiative_2.model.Facility;

/**
 * Clase singleton para almacenar datos y hacerlos accesibles en el resto de la aplicación.
 *
 * Created by Juan José Hernández Alonso on 16/11/17.
 */
public class DataHolder {

    /**
     * METODOS a implementar por Raúl junto con el servidor y el API. Para convertir JSON lo mejor
     * es usar clases heredadas del converter para las clases Facility y Building.
     * Luego, por el campo decidido (nombre o id) enlazarlos entre si.
     * Finalmente, cuando se recuperan las capas, acceder a los edificios y asociarlas.
     */
    private void init() {
        /**
         getBuildingsFromJSON();
         getFacilitiesFromJSON();
         bindFacilitiesToBuildings();
         bindKMLLayerToBuildings();
         **/
    }

    public static final String IS_INSIDE = "isInside";
    public static final String LOCATION = "location";
    public static final String FACILITIES = "facilities";
    public static final String BUILDINGS = "buildings";

    private static DataHolder INSTANCE;

    private HashMap<String, Object> data;

    private DataHolder() {
        data = new HashMap<>();
    }

    /**
     * Acceso al Singleton.
     * @return DataHolder INSTANCE.
     */
    public static DataHolder getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DataHolder();
            INSTANCE.put(IS_INSIDE, false);
            //Deberá eliminarse
            INSTANCE.initDummyValues();
        }
        return INSTANCE;
    }

    /**
     * Método que nos permite recuperar y hacer un casting directo del objeto.
     * @param key String clave de la propiedad almacenada.
     * @param clazz Class clase a la que hacer el casting del valor recuperado.
     * @param <T> Genérico que se corresponderá con la clase pasada como parámetro.
     * @return Objeto casteado a la clase pasada como parámetro almacenado en una clave dada.
     */
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(data.get(key));
    }

    /**
     * Método para añadir valores al mapa.
     * @param key String clave para acceder al valor.
     * @param value Object valor que se almacenará en esa clave.
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }

    // EN LA VERSIÓN FINAL ESTOS MÉTODOS DEBERÁN DESAPARECER. Solo se usan para generar lat y lng aleatorias.


    /**
     * Método de inicialización con valores de prueba. Este debería desaparecer ya que los elementos
     * se van a recuperar desde internet y el API REST.
     */
    private void initDummyValues(){
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
