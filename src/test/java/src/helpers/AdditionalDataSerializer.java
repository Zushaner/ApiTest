package src.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import src.models.AdditionalDataModel;

import java.lang.reflect.Type;

public class AdditionalDataSerializer implements JsonSerializer<AdditionalDataModel> {


    @Override
    public JsonElement serialize(AdditionalDataModel additionalData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        json.addProperty("additional_info", additionalData.getAdditionInfo());
        json.addProperty("additional_number", additionalData.getAdditionNumber());
        return json;
    }
}
