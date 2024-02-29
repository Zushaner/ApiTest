package steps;

import Helpers.AdditionalDataDeserializer;
import Helpers.AdditionalDataSerializer;
import Helpers.EntityDeserializer;
import Helpers.EntitySerializer;
import Helpers.ObjectDeserializer;
import Helpers.ObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.AdditionalData;
import models.EntityModel;
import models.ObjectModel;
import org.testng.Assert;

public class CommonSteps {
    private final static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(AdditionalData.class, new AdditionalDataSerializer())
            .registerTypeAdapter(ObjectModel.class, new ObjectSerializer())
            .registerTypeAdapter(EntityModel.class, new EntitySerializer())
            .registerTypeAdapter(AdditionalData.class, new AdditionalDataDeserializer())
            .registerTypeAdapter(ObjectModel.class, new ObjectDeserializer())
            .registerTypeAdapter(EntityModel.class, new EntityDeserializer())
            .create();
    public static Integer addModel(ObjectModel model) {
        return Integer.parseInt(
                RestAssured
                .given().contentType(ContentType.JSON).body(gson.toJson(model))
                .when().post("/create")
                .then().statusCode(200).extract().asString());
    }
    public static ObjectModel getModel(int id) {
        String response = RestAssured.when()
                .get(String.format("/get/%d", id))
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "Объект под данным id пустой");
        return gson.fromJson(response, ObjectModel.class);
    }
}
