package es.usj.e5_initiative_2.data;

/**
 * Created by Raúl Lapeña Martí
 */

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.usj.e5_initiative_2.model.Building;
import es.usj.e5_initiative_2.model.Facility;

/**
 * Class with the methods to get and convert JSON data
 */
public class JSONLoader {

    /**
     * Method that returns a HashMap of Buildings from a JSON file as string
     * @param fileAsString the JSON file from which buildings must be recovered
     * @return a hash map of buildings
     */
    public static HashMap<String, Building> JSONToBuildingHashMap(String fileAsString){
        try{

            //I create the JSONArray from the string
            JSONArray jsonArray = new JSONArray(fileAsString);

            //I create the variable for the HashMap of Buildings to fill and return
            HashMap<String, Building> buildings = new HashMap<>();

            //If the JSONArray object is not null
            //For every element in the array, I create a Building with the data and add it to
            //the HashMap<String, Building>
            for(int element = 0; element < jsonArray.length(); element++)
            {
                //Retrieving the object
                JSONObject retrievedObject = jsonArray.getJSONObject(element);

                //Creating the Building object
                Building building = new Building();
                building.setId(retrievedObject.getString("ID"));
                building.setName(retrievedObject.getString("name"));
                building.setDescription(retrievedObject.getString("description"));
                building.setSchedule(retrievedObject.getString("schedule"));

                // Get the images and add them to the building
                JSONArray images = retrievedObject.getJSONArray("images");
                for (int image = 0; image < images.length(); image++)
                {
                    JSONObject retrievedImage = images.getJSONObject(image);
                    building.getImages().add(retrievedImage.getString("url"));
                }

                //Adding it to the map
                buildings.put(building.getId(), building);
            }

            //Returning the buildings
            return buildings;

        }catch(Exception ex){
            return null;
        }
    }

    /**
     * Method that returns an ArrayList of Facilities from a JSON file
     * @param fileAsString the JSON from which facilities must be recovered
     * @return an array list of facilities
     */
    public static ArrayList<Facility> JSONToFacilitiesArrayList(String fileAsString){
        try{

            //I create the JSONArray from the string
            JSONArray jsonArray = new JSONArray(fileAsString);

            //I create the variable for the ArrayList of Facilities to fill and return
            ArrayList<Facility> facilities = new ArrayList<>();

            //If the JSONArray object is not null
            //For every element in the array, I create a Facilityu with the data and add it to
            //the ArrayList<Facility>
            for(int element = 0; element < jsonArray.length(); element++)
            {
                //Retrieving the object
                JSONObject retrievedObject = jsonArray.getJSONObject(element);

                String buildingId = retrievedObject.getString("buildingID");
                String title = retrievedObject.getString("title");
                String snippet = retrievedObject.getString("snippet");
                LatLng coordinates = new LatLng(retrievedObject.getDouble("latitude"), retrievedObject.getDouble("longitude"));

                //Creating the Facility object
                Facility facility = new Facility(buildingId, coordinates, title, snippet);

                //Adding it to the list
                facilities.add(facility);
            }

            //Returning the facilities
            return facilities;

        }catch(Exception ex){
            return null;
        }
    }

    /**
     * Method that returns an ArrayList of String from a JSON file
     * @param fileAsString the JSON from which strings must be recovered
     * @return an array list of string
     */
    public static ArrayList<String> JSONToStringArrayList(String fileAsString)
    {
        try
        {

            //I create the JSONArray from the string
            JSONArray jsonArray = new JSONArray(fileAsString);

            //I create the variable for the ArrayList of String to fill and return
            ArrayList<String> strings = new ArrayList<>();

            //If the JSONArray object is not null
            //For every element in the array, I create a string with the data and add it to
            //the ArrayList<String>
            for(int element = 0; element < jsonArray.length(); element++)
            {
                //Retrieving the object
                String string = jsonArray.getString(element);

                //Adding it to the list
                strings.add(string);
            }

            //Returning the facilities
            return strings;

        }catch(Exception ex){
            return null;
        }
    }

}