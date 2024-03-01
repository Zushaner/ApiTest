package src.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import src.models.AdditionalDataModel;
import src.models.EntityModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EntityDeserializer implements JsonDeserializer<EntityModel> {

    @Override
    public EntityModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        EntityModel entityModel = new EntityModel();
        entityModel.setAdditionalData(jsonDeserializationContext
                .deserialize(jsonObject.get("addition"), AdditionalDataModel.class));
        entityModel.setId(jsonObject.get("id").getAsInt());

        JsonArray importantNumbers = jsonObject.getAsJsonArray("important_numbers");
        List<Integer> numbers = new ArrayList<>();
        for (JsonElement importantNumber : importantNumbers) {
            numbers.add(importantNumber.getAsInt());
        }

        entityModel.setImportantNumbers(numbers);
        entityModel.setTitle(jsonObject.get("title").getAsString());
        entityModel.setVerified(jsonObject.get("verified").getAsBoolean());
        return entityModel;
    }
}
