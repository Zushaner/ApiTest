package src.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import src.models.EntityModel;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonSerializer<EntityModel> {

    @Override
    public JsonElement serialize(EntityModel entityModel, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        for(Integer i : entityModel.getImportantNumbers()) { array.add(i);}
        json.add("addition", jsonSerializationContext.serialize(entityModel.getAdditionalData()));
        json.add("important_numbers", array);
        json.addProperty("title", entityModel.getTitle());
        json.addProperty("verified", entityModel.getVerified());
        return json;
    }
}
