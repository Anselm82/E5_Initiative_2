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
 * Created by Anselm on 28/4/17.
 */

public class RESTRequest {

    private HttpURLConnection httpURLConnection;
    private URL url;
    private String response;

    public RESTRequest(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public RESTRequest(URL url) {
        this.url = url;
    }

    public void doPost(Object param) {
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-type", "application/json");
            httpURLConnection.connect();
            String json = new GsonBuilder().create().toJson(param, Facility.class);
            JSONObject jsonObject = new JSONObject(json);
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();
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
            Log.e("POST", "Error in http connection " + e.toString());
        }
    }

    public String getResponse() {
        return response;
    }
}

