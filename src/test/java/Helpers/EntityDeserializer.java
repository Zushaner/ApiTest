package Helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import models.EntityModel;
import models.ObjectModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EntityDeserializer implements JsonDeserializer<EntityModel> {

    @Override
    public EntityModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray array = jsonObject.getAsJsonArray("entity");
        List<ObjectModel> objects = new ArrayList<>();
        for (JsonElement object : array) {
            objects.add(jsonDeserializationContext.deserialize(object, ObjectModel.class));
        }
        return new EntityModel(objects);
    }
}
