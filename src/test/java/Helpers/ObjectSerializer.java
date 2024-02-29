package Helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.ObjectModel;

import java.lang.reflect.Type;

public class ObjectSerializer implements JsonSerializer<ObjectModel> {

    @Override
    public JsonElement serialize(ObjectModel objectModel, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        for(Integer i : objectModel.getImportantNumbers()) { array.add(i);}
        json.add("addition", jsonSerializationContext.serialize(objectModel.getAdditionalData()));
        json.add("important_numbers", array);
        json.addProperty("title", objectModel.getTitle());
        json.addProperty("verified", objectModel.getVerified());
        return json;
    }
}
