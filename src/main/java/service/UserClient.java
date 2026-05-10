package service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient{

    private static final String CLIENT_REGISTRATION_PATH = "/api/auth/register";
    private static final String CLIENT_DELETE_PATH = "/api/auth/user";
    private static final String CLIENT_LOGIN_PATH = "/api/auth/login";

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(CLIENT_REGISTRATION_PATH);
    }

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return  given()
                .spec(getSpec())
                .body(user)
                .post(CLIENT_LOGIN_PATH);
    }

    @Step("Создание и авторизация пользователя")
    public String createUserAndLoginUser(User user){
        create(user);
        Response loginResponse = login(user);
        return loginResponse.path("accessToken");
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {

        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .delete(CLIENT_DELETE_PATH);
    }
}
