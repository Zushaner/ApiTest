package Helpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import models.AdditionalData;

import java.lang.reflect.Type;

public class AdditionalDataDeserializer implements JsonDeserializer<AdditionalData> {

    @Override
    public AdditionalData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        AdditionalData additionalData = new AdditionalData();
        additionalData.setAdditionInfo(jsonObject.get("additional_info").getAsString());
        additionalData.setAdditionNumber(jsonObject.get("additional_number").getAsInt());
        additionalData.setAdditionId(jsonObject.get("id").getAsInt());
        return additionalData;
    }
}
