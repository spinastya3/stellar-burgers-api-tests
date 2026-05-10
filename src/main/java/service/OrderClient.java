package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient{

    private static final String ORDER_MAKING_PATH = "/api/orders";

    @Step("Создание заказа")
    public Response create(String accessToken, Order order) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .post(ORDER_MAKING_PATH);
    }
}
