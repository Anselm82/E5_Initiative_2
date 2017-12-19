package es.usj.e5_initiative_2.data;

import android.util.Log;

import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import es.usj.e5_initiative_2.model.Facility;

/**
 * Clase que realiza las peticiones REST.
 *
 * Created by Juan José Hernández Alonso on 16/11/17.
 */
public class RESTRequest {

    private HttpURLConnection httpURLConnection;
    private URL url;
    private String response;

    /**
     * Constructor que recibe la URL a consultar.
     * @param url String url como cadena a consultar.
     */
    public RESTRequest(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para peticiones GET.
     */
    public void doGet() {
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();
                response = sb.toString();
                httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("GET", "Error in http connection " + e.toString());
        }
    }

    /**
     * Getter de la respuesta del servidor al realizar una petición.
     * @return String response.
     */
    public String getResponse() {
        return response;
    }
}

