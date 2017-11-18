package es.usj.e5_initiative_2.data;

import com.google.gson.Gson;

import java.util.List;

/**
 * Clase base para la conversión de entidades a JSON y viceversa.
 *
 * Created by Anselm on 28/4/17.
 */

public abstract class JSONConverter<T> {

    protected String query;
    final Class<T> typeClass;

    public JSONConverter(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public String toJSON(T entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }

    public T toEntity(String jsonObject) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, typeClass);
    }

    public String toJSONArray(List<T> entities) {
        String result = "";
        for (T entity: entities) {
            result += toJSON(entity);
        }
        return result;
    }

    public abstract List<T> toEntityList(String jsonArray);

    protected void append(String param) {
        query += "/" + param;
    }
}
