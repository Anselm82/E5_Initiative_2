package es.usj.e5_initiative_2.data;

import com.google.gson.Gson;

import java.util.List;

/**
 * Clase base para la conversión de entidades a JSON y viceversa. Cualquier entidad que se quiera
 * utilizar con JSON debería heredar de esta clase parametrizada.
 *
 * Created by Juan José Hernández Alonso on 28/4/17.
 */
public abstract class JSONConverter<T> {

    final Class<T> typeClass;

    /**
     * Constructor parametrizado.
     * @param typeClass Class de la entidad que queremos convertir a/desde JSON.
     */
    public JSONConverter(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    /**
     * Método para convertir una entidad a JSON.
     * @param entity Objeto de la clase específica que queremos convertir.
     * @return String JSON del objeto.
     */
    public String toJSON(T entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }

    /**
     * Método para convertir una cadena JSON en entidad.
     * @param jsonObject String objeto JSON que queremos convertir.
     * @return T objeto de la clase entidad de destino.
     */
    public T toEntity(String jsonObject) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, typeClass);
    }

    /**
     * Método que convierte una lista de entidades en un array JSON.
     * @param entities List<T> de entidades a convertir.
     * @return String con la lista como JSON.
     */
    public String toJSONArray(List<T> entities) {
        String result = "";
        for (T entity: entities) {
            result += toJSON(entity);
        }
        return result;
    }

    /**
     * Método que se especificará en la clase que herede.
     * @param jsonArray String con el array de objetos a convertir.
     * @return List<T> con los objetos desde el array JSON.
     */
    public abstract List<T> toEntityList(String jsonArray);

    /*
    Ejemplo de implementación para una clase POIDTO que además registra una adaptador para los tipos
    fecha, necesario para la conversión desde bbdd.
     @Override
    public List<POIDTO> toEntityList(String jsonArray) {
        Type listType = new TypeToken<List<POIDTO>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        List<POIDTO> entities = gsonBuilder.create().fromJson(jsonArray, listType);
        return entities;
    }
     */
}
