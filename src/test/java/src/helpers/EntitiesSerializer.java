package src.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import src.models.EntitiesModel;
import src.models.EntityModel;

import java.lang.reflect.Type;

public class EntitiesSerializer implements JsonSerializer<EntitiesModel> {
    @Override
    public JsonElement serialize(EntitiesModel entitiesModel, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject element = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        for(EntityModel model : entitiesModel.getEntities()){
            jsonElements.add(jsonSerializationContext.serialize(model));
        }
        element.add("entity", jsonElements);
        return element;
    }
}
