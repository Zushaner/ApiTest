package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import src.models.AdditionalDataModel;
import src.models.EndpointsPath;
import src.models.EntitiesModel;
import src.models.EntityModel;
import src.steps.CommonSteps;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

@Epic("Тестирование API Entities")
public class ApiEntityPositiveTest {
    private EntityModel model;
    private final ThreadLocal<Integer> idForDelete = new ThreadLocal<>();
    private final CommonSteps commonSteps = new CommonSteps();

    @BeforeSuite
    public void beforeSuite() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @BeforeMethod
    public void beforeEach() {
        Random random = new Random();
        model = EntityModel.builder()
                .title(RandomStringUtils.randomAlphabetic(8))
                .verified(random.nextBoolean())
                .additionalData(
                        AdditionalDataModel.builder()
                                .additionNumber(random.nextInt())
                                .additionInfo(RandomStringUtils.randomAlphabetic(8))
                                .build())
                .importantNumbers(List.of(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt()))
                .build();
    }

    @Test(description = "Тест api GET")
    @Severity(SeverityLevel.BLOCKER)
    public void getMethodPositiveTest() {
        int id = commonSteps.createEntity(model);
        idForDelete.set(id);

        String response = RestAssured
                .given().filter(new AllureRestAssured())
                .when().get(String.format(EndpointsPath.GET_METHOD_PATH, id))
                .then().statusCode(200).extract().asString();

        Assert.assertNotNull(response, "В ответе нет данных");
        EntityModel entityModel = (EntityModel) commonSteps.fromJson(response, EntityModel.class);
        Assert.assertEquals(entityModel, model, "Сущности должны быть эквивалентными");
    }

    @Test(description = "Тест api POST")
    @Severity(SeverityLevel.BLOCKER)
    public void postMethodPositiveTest() {
        String response = RestAssured
                .given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON).body(commonSteps.toJson(model))
                .when().post(EndpointsPath.POST_METHOD_PATH)
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(response, "В ответе нет данных");

        int id = Integer.parseInt(response);
        idForDelete.set(id);
        EntityModel entityModel = commonSteps.getEntity(id);
        Assert.assertEquals(entityModel, model, "Сущности должны быть эквивалентными");
    }

    @Test(description = "Тест api DELETE")
    @Severity(SeverityLevel.NORMAL)
    public void deleteMethodPositiveTest() {
        int id = commonSteps.createEntity(model);
        idForDelete.set(id);

        RestAssured
                .given().filter(new AllureRestAssured())
                .when().delete(String.format(EndpointsPath.DELETE_METHOD_PATH, id))
                .then().statusCode(204);

        commonSteps.isEntityNotExist(id);
    }

    @Test(description = "Тест api PATCH")
    @Severity(SeverityLevel.NORMAL)
    public void patchMethodPositiveTest() {
        int id = commonSteps.createEntity(model);
        idForDelete.set(id);
        EntityModel expectedModel = model.withVerified(!model.getVerified());

        // отправляется вся модель, так как при попытке отправить в теле один параметр service прекращает работу
        RestAssured
                .given().filter(new AllureRestAssured())
                .contentType(ContentType.JSON).body(commonSteps.toJson(expectedModel))
                .when().patch(String.format(EndpointsPath.PATCH_METHOD_PATH, id))
                .then().statusCode(204);

        EntityModel entityModel = commonSteps.getEntity(id);
        Assert.assertEquals(entityModel, expectedModel, "Сущности должны быть эквивалентными");
    }

    // @DataProvider(parallel = true)
    // при попытке параллельного запуска на все запросы начинает приходить код 500
    @DataProvider
    public Object[][] filtersForGetAllMethod() {
        return new Object[][]{
                new Object[]{"verified", "true"},
                new Object[]{"title", "title"}
        };
    }

    //так как необходимо по одному тесту на метод, приоритетнее проверить данные параметры, чем Page и Per Page
    @Test(description = "Тест api GETALL", dataProvider = "filtersForGetAllMethod")
    @Severity(SeverityLevel.MINOR)
    public void getAllMethodPositiveTest(String attribute, String value) {
        String allEntitiesResponse = RestAssured.given().filter(new AllureRestAssured()).accept(ContentType.JSON)
                .when().get(EndpointsPath.GET_ALL_METHOD_PATH)
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
                .when().get(EndpointsPath.GET_ALL_METHOD_PATH)
                .then().statusCode(200).extract().asString();
        Assert.assertNotNull(filteredResponse, "В ответе нет данных");

        EntitiesModel filteredEntities = (EntitiesModel) commonSteps.fromJson(filteredResponse, EntitiesModel.class);
        Assert.assertEquals(filteredEntities, expectedEntities, "Сущности должны быть эквивалентными");
    }

    @AfterMethod
    public void after() {
        if (idForDelete.get() != null) {
            RestAssured.delete(String.format(EndpointsPath.DELETE_METHOD_PATH, idForDelete.get()));
        }
    }
}
