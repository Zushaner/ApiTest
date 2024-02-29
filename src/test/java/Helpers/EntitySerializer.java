package Helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.EntityModel;
import models.ObjectModel;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonSerializer<EntityModel> {
    @Override
    public JsonElement serialize(EntityModel entityModel, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject element = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        for(ObjectModel model : entityModel.getObjects()){
            jsonElements.add(jsonSerializationContext.serialize(model));
        }
        element.add("entity", jsonElements);
        return element;
    }
}
