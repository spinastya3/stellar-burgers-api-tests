package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends BaseClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";
    @Step("Получение данных об ингридиентах")
    public Response getIngredients( ) {
        return given()
                .spec(getSpec())
                .get(INGREDIENTS_PATH);
    }
}
