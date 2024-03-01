package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import src.models.AdditionalDataModel;
import src.models.EntitiesModel;
import src.models.EntityModel;
import org.testng.Assert;
import org.testng.annotations.*;
import src.steps.CommonSteps;

import java.lang.reflect.Field;
import java.util.List;

@Epic("Тестирование API Entities")
public class ApiEntityPositiveTest {
    private EntityModel model;
    private final ThreadLocal<Integer> idForDelete = new ThreadLocal<>();
    private final CommonSteps commonSteps = new CommonSteps();

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
        model = EntityModel.builder()
                .title("title")
                .verified(false)
                .additionalData(AdditionalDataModel.builder().additionNumber(19).additionInfo("info").build())
                .importantNumbers(List.of(1, 4, 0, 8))
                .build();
    }

    @Test(description = "Тест api GET")
    @Severity(SeverityLevel.BLOCKER)
    public void getMethodPositiveTest() {
        int id = commonSteps.createEntity(model);
        idForDelete.set(id);

        String response = RestAssured
                .given().filter(new AllureRestAssured())
                .when().get(String.format("/get/%d", id))
                .then().statusCode(200).extract().asString();

        Assert.assertNotNull(response, "В ответе нет данных");
        EntityModel entityModel = (EntityModel) commonSteps.fromJson(response, EntityModel.class);
        Assert.assertEquals(entityModel, model, "Сущности должны быть эквивалентными");
    }

    @Test(description = "Тест api POST", dependsOnMethods = {"getMethodPositiveTest"})
    @Severity(SeverityLevel.BLOCKER)
    public void postMethodPositiveTest() {
        String response = RestAssured
                .given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON).body(commonSteps.toJson(model))
                .when().post("/create")
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "В ответе нет данных");

        int id = Integer.parseInt(response);
        idForDelete.set(id);
        EntityModel entityModel = commonSteps.getEntity(id);
        Assert.assertEquals(entityModel, model, "Сущности должны быть эквивалентными");
    }

    @Test(description = "Тест api DELETE", dependsOnMethods = {"postMethodPositiveTest", "getMethodPositiveTest"})
    @Severity(SeverityLevel.NORMAL)
    public void deleteMethodPositiveTest() {
        int id = commonSteps.createEntity(model);
        idForDelete.set(id);

        RestAssured
                .given().filter(new AllureRestAssured())
                .when().delete(String.format("/delete/%d", id))
                .then().statusCode(204);

        commonSteps.isEntityNotExist(id);
    }

    @Test(description = "Тест api PATCH", dependsOnMethods = {"postMethodPositiveTest", "getMethodPositiveTest"})
    @Severity(SeverityLevel.NORMAL)
    public void patchMethodPositiveTest() {
        int id = commonSteps.createEntity(model);
        idForDelete.set(id);
        EntityModel expectedModel = model.withVerified(true);

        // отправляется вся модель, так как при попытке отправить в теле один параметр service прекращает работу
        RestAssured
                .given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON).body(commonSteps.toJson(expectedModel))
                .when().patch(String.format("/patch/%d", id))
                .then().statusCode(204);

        EntityModel entityModel = commonSteps.getEntity(id);
        Assert.assertEquals(entityModel, expectedModel, "Сущности должны быть эквивалентными");
    }

//    @DataProvider(parallel = true)
// при попытке параллельного запуска на все запросы начинает приходить код 500
    @DataProvider
    public Object[][] filtersForGetAllMethod() {
        return new Object[][] {
                new Object[] {"verified", "true"},
                new Object[] {"title", "title"}
        };
    }
    //так как необходимо по одному тесту на метод, приоритетнее проверить данные параметры, чем Page и Per Page
    @Test(description = "Тест api GETALL", dataProvider = "filtersForGetAllMethod")
    @Severity(SeverityLevel.MINOR)
    public void getAllMethodPositiveTest(String attribute, String value) {
        String allEntitiesResponse = RestAssured.given().filter(new AllureRestAssured()).accept(ContentType.JSON)
                .when().get("/getAll")
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(allEntitiesResponse, "В ответе нет данных");
        EntitiesModel allEntities = (EntitiesModel) commonSteps.fromJson(allEntitiesResponse, EntitiesModel.class);

        EntitiesModel expectedEntities = new EntitiesModel(
                allEntities.getEntities().stream().filter(x -> {
                    try {
                        Field field = EntityModel.class.getDeclaredField(attribute);
                        field.setAccessible(true);
                        return field.get(x).toString().equals(value);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        );

        String filteredResponse = RestAssured
                .given().param(attribute, value).filter(new AllureRestAssured()).accept(ContentType.JSON)
                .when().get("/getAll")
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(filteredResponse, "В ответе нет данных");

        EntitiesModel filteredEntities = (EntitiesModel) commonSteps.fromJson(filteredResponse, EntitiesModel.class);
        Assert.assertEquals(filteredEntities, expectedEntities, "Сущности должны быть эквивалентными");
    }

    @AfterMethod
    public void after() {
        if (idForDelete.get() != null) {
            RestAssured.delete(String.format("/delete/%d", idForDelete.get()));
        }
    }
}
