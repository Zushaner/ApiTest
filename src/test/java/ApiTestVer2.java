import Helpers.AdditionalDataDeserializer;
import Helpers.AdditionalDataSerializer;
import Helpers.EntityDeserializer;
import Helpers.EntitySerializer;
import Helpers.ObjectDeserializer;
import Helpers.ObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.AdditionalData;
import models.EntityModel;
import models.ObjectModel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import steps.CommonSteps;

import java.util.List;

public class ApiTestVer2 {
    private final static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(AdditionalData.class, new AdditionalDataSerializer())
            .registerTypeAdapter(ObjectModel.class, new ObjectSerializer())
            .registerTypeAdapter(EntityModel.class, new EntitySerializer())
            .registerTypeAdapter(AdditionalData.class, new AdditionalDataDeserializer())
            .registerTypeAdapter(ObjectModel.class, new ObjectDeserializer())
            .registerTypeAdapter(EntityModel.class, new EntityDeserializer())
            .create();

    @BeforeMethod
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }
    @Test(description = "Тест api GET")
    public void getTest() {
        ObjectModel model = ObjectModel.builder()
                .title("title")
                .verified(false)
                .additionalData(AdditionalData.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1,4,8,8))
                .build();
        int id = CommonSteps.addModel(model);
        String response = RestAssured.given().filter(new AllureRestAssured()).when()
                .get(String.format("/get/%d", id))
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "В ответе нет данных");
        ObjectModel objectModel = gson.fromJson(response, ObjectModel.class);
        Assert.assertEquals(objectModel, model);
    }
    @Test(description = "Тест api POST")
    public void postTest() {
        ObjectModel model = ObjectModel.builder()
                .title("title")
                .verified(false)
                .additionalData(AdditionalData.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1,4,8,8))
                .build();
        String response = RestAssured
                .given().filter(new AllureRestAssured()).contentType(ContentType.JSON).body(gson.toJson(model))
                .when().post("/create")
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "В ответе нет данных");
        int id = Integer.parseInt(response);
        ObjectModel objectModel = CommonSteps.getModel(id);
        Assert.assertEquals(objectModel, model);
    }
    @Test(description = "Тест api DELETE")
    public void deleteTest() {
        ObjectModel model = ObjectModel.builder()
                .title("title")
                .verified(false)
                .additionalData(AdditionalData.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1,4,8,8))
                .build();
        int id = CommonSteps.addModel(model);
        RestAssured
                .given().filter(new AllureRestAssured()).contentType(ContentType.JSON)
                .when().delete("/delete/" + id)
                .then().statusCode(204);
        RestAssured
                .when()
                .get(String.format("/get/%d", id))
                .then().statusCode(500);
    }
    @Test(description = "Тест api PATCH")
    public void patchTest() {
        ObjectModel model = ObjectModel.builder()
                .title("title")
                .verified(false)
                .additionalData(AdditionalData.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1,4,8,8))
                .build();
        int id = CommonSteps.addModel(model);
        model.setVerified(true);
        RestAssured
                .given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(gson.toJson(model))
                .when().patch("/patch/" + id).then().statusCode(204);
        ObjectModel objectModel = CommonSteps.getModel(id);
        Assert.assertEquals(objectModel, model);
    }
    @Test(description = "Тест api GETALL")
    public void getAllTest() {
        String response = RestAssured.given().filter(new AllureRestAssured()).when().get("/getAll").then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "Ответ пустой");
        EntityModel entityModel = gson.fromJson(response, EntityModel.class);
        Assert.assertNotNull(entityModel.getObjects(), "Нет элементов в базе");
    }
}
