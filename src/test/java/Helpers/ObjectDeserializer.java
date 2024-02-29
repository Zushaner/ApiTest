package Helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import models.AdditionalData;
import models.ObjectModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ObjectDeserializer implements JsonDeserializer<ObjectModel> {

    @Override
    public ObjectModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        ObjectModel objectModel = new ObjectModel();
        objectModel.setAdditionalData(jsonDeserializationContext
                .deserialize(jsonObject.get("addition"), AdditionalData.class));
        objectModel.setId(jsonObject.get("id").getAsInt());

        JsonArray importantNumbers = jsonObject.getAsJsonArray("important_numbers");
        List<Integer> numbers = new ArrayList<>();
        for (JsonElement importantNumber : importantNumbers) {
            numbers.add(importantNumber.getAsInt());
        }

        objectModel.setImportantNumbers(numbers);
        objectModel.setTitle(jsonObject.get("title").getAsString());
        objectModel.setVerified(jsonObject.get("verified").getAsBoolean());
        return objectModel;
    }
}
