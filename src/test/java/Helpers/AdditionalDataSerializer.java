package Helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.AdditionalData;

import java.lang.reflect.Type;

public class AdditionalDataSerializer implements JsonSerializer<AdditionalData> {


    @Override
    public JsonElement serialize(AdditionalData additionalData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        json.addProperty("additional_info", additionalData.getAdditionInfo());
        json.addProperty("additional_number", additionalData.getAdditionNumber());
        return json;
    }
}
