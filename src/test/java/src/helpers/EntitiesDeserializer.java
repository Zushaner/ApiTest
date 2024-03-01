package src.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import src.models.EntitiesModel;
import src.models.EntityModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EntitiesDeserializer implements JsonDeserializer<EntitiesModel> {

    @Override
    public EntitiesModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray array = jsonObject.getAsJsonArray("entity");
        List<EntityModel> objects = new ArrayList<>();
        for (JsonElement object : array) {
            objects.add(jsonDeserializationContext.deserialize(object, EntityModel.class));
        }
        return new EntitiesModel(objects);
    }
}
