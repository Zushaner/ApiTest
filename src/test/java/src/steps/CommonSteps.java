package src.steps;

import src.helpers.AdditionalDataDeserializer;
import src.helpers.AdditionalDataSerializer;
import src.helpers.EntitiesDeserializer;
import src.helpers.EntitiesSerializer;
import src.helpers.EntityDeserializer;
import src.helpers.EntitySerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import src.models.AdditionalDataModel;
import src.models.EndpointsPath;
import src.models.EntitiesModel;
import src.models.EntityModel;
import org.testng.Assert;

public class CommonSteps {
    private final static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(AdditionalDataModel.class, new AdditionalDataSerializer())
            .registerTypeAdapter(EntityModel.class, new EntitySerializer())
            .registerTypeAdapter(EntitiesModel.class, new EntitiesSerializer())
            .registerTypeAdapter(AdditionalDataModel.class, new AdditionalDataDeserializer())
            .registerTypeAdapter(EntityModel.class, new EntityDeserializer())
            .registerTypeAdapter(EntitiesModel.class, new EntitiesDeserializer())
            .create();

    @Step("Добавление новой сущности")
    public Integer createEntity(EntityModel model) {
        return Integer.parseInt(
                RestAssured
                        .given().contentType(ContentType.JSON).body(gson.toJson(model))
                        .when().post(EndpointsPath.POST_METHOD_PATH)
                        .then().statusCode(200).extract().asString()
        );
    }

    @Step("Получение сущности с id = {id}")
    public EntityModel getEntity(int id) {
        String response = RestAssured
                .when().get(String.format(EndpointsPath.GET_METHOD_PATH, id))
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "В ответе нет данных");
        return gson.fromJson(response, EntityModel.class);
    }

    @Step("Проверка отсутствия сущности c id = {id}")
    public void isEntityNotExist(int id) {
        RestAssured
                .given().filter(new AllureRestAssured())
                .when()
                .when().get(String.format(EndpointsPath.GET_METHOD_PATH, id))
                .then().statusCode(500);
    }

    @Step("Формирование объекта из Json")
    public Object fromJson(String json, Class<?> cl) {
        return gson.fromJson(json, cl);
    }

    @Step("Формирование json из объекта")
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
