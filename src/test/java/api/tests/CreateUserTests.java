package api.tests;

import api.BaseTest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.Test;
import service.UserGenerator;

public class CreateUserTests extends BaseTest {

    private Response response;
    private User user;

    @Test
    @DisplayName("Проверяем код и тело ответа для успешного создания пользователя")
    @Description("Отправляем валидные данные пользователя, проверяем успешное создание пользователя и получение кода 200")
    public void createUserTest() {

        user = UserGenerator.user();
        response = userClient.create(user);

        String accessToken = response.path("accessToken");
        if (accessToken != null) accessTokens.add(accessToken);

        response.then()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()));
    }

    @Test
    @DisplayName("Ошибка при создании дубликата пользователя")
    @Description("Создаем дубликат пользователя, проверяем код ошибки 403 и тело ответа")
    public void createDuplicateUserTest() {

        user = UserGenerator.user();
        response = userClient.create(user);

        String accessToken = response.path("accessToken");
        if (accessToken != null) accessTokens.add(accessToken);

        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }
}
