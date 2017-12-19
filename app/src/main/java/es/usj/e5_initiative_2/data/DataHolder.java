package es.usj.e5_initiative_2.data;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

import es.usj.e5_initiative_2.model.Building;
import es.usj.e5_initiative_2.model.Facility;

/**
 * Clase singleton para almacenar datos y hacerlos accesibles en el resto de la aplicación.
 *
 * Created by Juan José Hernández Alonso on 16/11/17.
 */
public class DataHolder {

     /**
     * METODOS a implementados por Raúl junto con el servidor y el API. Para convertir JSON lo mejor
     * es usar clases heredadas del converter para las clases Facility y Building.
     * Luego, por el campo decidido (nombre o id) enlazarlos entre si.
     * Finalmente, cuando se recuperan las capas, acceder a los edificios y asociarlas.
     */
    private void init() {
        Location l = new Location("");
        l.setLatitude(41.756969);
        l.setLongitude(-0.834666);
        put(LOCATION,l);
        new FacilitiesTask().execute();
        new BuildingsTask().execute();
    }

    /**
     *  Method created to retrieve the data from the AsyncTask
     */
    private void onFacilitiesObtained(ArrayList<Facility> facilities) {
        try{
            this.facilities = facilities;
            put(FACILITIES, facilities);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method created to retrieve the data from the AsyncTask
     */
    private void onBuildingsObtained(HashMap<String, Building> retrievedBuildings) {
        try{
            bindFacilitiesToBuildings(facilities, retrievedBuildings);
            put(BUILDINGS, retrievedBuildings);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static final String IS_INSIDE = "isInside";
    public static final String LOCATION = "location";
    public static final String FACILITIES = "facilities";
    public static final String BUILDINGS = "buildings";

    private static final String buildings_url = "http://ralamarti.tk/android/buildings.json";
    private static final String facilities_url = "http://ralamarti.tk/android/facilities.json";

    private ArrayList<Facility> facilities;

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
            INSTANCE.init();
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

    /**
     * Method that binds the facilities to the buildings
     * @param facilities The list of facilities
     * @param buildings The map of buildings
     */
    private void bindFacilitiesToBuildings(ArrayList<Facility> facilities, HashMap<String, Building> buildings){
        for (Facility facility : facilities) {
            String buildingID = facility.getBuildingID();
            Building building = buildings.get(buildingID);
            building.getFacilities().add(facility);
        }
    }

    /**
     * Class created to download and parse the Facilities JSON in the background
     */
    @SuppressLint("StaticFieldLeak")
    private class FacilitiesTask extends AsyncTask<String, Void, ArrayList<Facility>> {

        @Override
        /**
         * Do in background will be called when the AsyncTask is executed. We try to download the resource
         */
        public ArrayList<Facility> doInBackground(String... sUrl) {

            ArrayList<Facility> facilities;
            RESTRequest request = new RESTRequest(facilities_url);
            request.doGet();
            String json = request.getResponse();
            facilities = JSONLoader.JSONToFacilitiesArrayList(json);
            return facilities;

        }

        @Override
        protected void onPostExecute(ArrayList<Facility> facilities) {
            //After executing the Background task, the facilities are passed to the method
            //in the main thread that updates them
            super.onPostExecute(facilities);
            DataHolder.this.onFacilitiesObtained(facilities);
        }
    }

    /**
     * Class created to download and parse the Buildings JSON in the background
     */
    @SuppressLint("StaticFieldLeak")
    private class BuildingsTask extends AsyncTask<String, Void, HashMap<String,Building>> {

        @Override
        /**
         * Do in background will be called when the AsyncTask is executed. We try to download the resource
         */
        public HashMap<String,Building> doInBackground(String... sUrl) {

            RESTRequest request = new RESTRequest(buildings_url);
            request.doGet();
            String json = request.getResponse();
            return JSONLoader.JSONToBuildingHashMap(json);

        }

        @Override
        protected void onPostExecute(HashMap<String, Building> buildings) {
            //After executing the Background task, the buildings are passed to the method
            //in the main thread that updates them
            super.onPostExecute(buildings);
            DataHolder.this.onBuildingsObtained(buildings);
        }
    }
}
