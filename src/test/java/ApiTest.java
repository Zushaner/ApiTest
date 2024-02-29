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
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;


public class ApiTest {
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
        String response = RestAssured.when().get("/get/5").then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "Ответ пустой");
        ObjectModel objectModel = gson.fromJson(response, ObjectModel.class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(objectModel.getId(), "У элемента отсутствует id");
        softAssert.assertNotNull(objectModel.getTitle(), "У элемента отсутствует title");
        softAssert.assertNotNull(objectModel.getVerified(), "У элемента отсутствует верификация");
        // проверяем только эти поля, так как остальные не являются обязательными для элемента
        softAssert.assertAll();
    }

    @Test(description = "Тест api GETALL") //поля параметр.
    public void getAllTest() {
        String response = RestAssured.when().get("/getAll").then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "Ответ пустой");
    }
    @Test(description = "Тест api POST")
    public void postTest() {
        ObjectModel model = ObjectModel.builder()
                .title("title")
                .verified(false)
                .additionalData(AdditionalData.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1,4,8,8))
                .build();
        String response =  RestAssured
                .given().contentType(ContentType.JSON).body(gson.toJson(model))
                .when().post("/create")
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "Ответ пустой");
        Assert.assertTrue(StringUtils.isNumeric(response), "В ответе должно быть число");
    }
    @Test(description = "Тест api DELETE")
    public void deleteTest() {
        RestAssured
                .when()
                .delete("/delete/8") //необходимо менять id вручную, так как при повторном запуске будет код 400
                .then()
                .statusCode(204); //единственное, что можно проверить - верный статус код
    }
    @Test(description = "Тест api PATCH")
    public void patchTest() {
        ObjectModel model = ObjectModel.builder()
                .title("title")
                .verified(true)
                .additionalData(AdditionalData.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1,4,8,8))
                .build();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(gson.toJson(model))
                .when()
                .patch("/patch/6")
                .then()
                .statusCode(204); //единственное, что можно проверить - верный статус код
    }
}
