package es.usj.e5_initiative_2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que define la entidad edificio del campus. Implementa serializable ya que debe proceder
 * de un servicio REST.
 *
 * Created by Juan José Hernández Alonso on 17/11/17.
 */
public class Building implements Serializable {

    /**
     * Id del edificio. Podría utilizarse como clave para enlazar JSON y KML.
     * Debería obtenerse desde bbdd/KML.
     */
    private int id;
    /**
     * Nombre del edificio. Podría utilizarse como clave para enlazar los marcadores (POIs) con la capa
     * KML (KMLLayer). Debería obtenerse desde bbdd/KML.
     */
    private String name;
    /**
     * Lista de instalaciones. Recuperadas desde JSON.
     */
    private List<Facility> facilities;
    /**
     * Lista de imágenes del servidor para este edificio. Lo más sencillo sería crear una estructura
     * de carpetas basada en el nómbre del edificio o su id, y que fuese allí donde se subiesen
     * las imágenes capturadas por los usuarios. Son URL COMPLETAS. Se podría hacer una especie de
     * cache con el singleton y guardando los archivos.
     * Debería obtenerse desde bbdd.
     */
    private List<String> images;
    /**
     * Descripción de edificio, debería obtenerse desde bbdd.
     */
    private String description;
    /**
     * Horario del edificio, debería obtenerse desde bbdd.
     */
    private String schedule;

    // GETTERS Y SETTERS

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

    public String getSchedule() {
        return schedule;
    }

    public String getName() {
        return name;
    }
}
