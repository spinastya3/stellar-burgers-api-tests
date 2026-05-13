package api.tests;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;
import service.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.*;

public class AuthorizationUserTests extends BaseTest {

    private User user;

    @Before
    public void setUp(){

        user = UserGenerator.user();

        Response response = userClient.create(user);
        String accessToken = response.path("accessToken");
        if (accessToken != null) {
            accessTokens.add(accessToken);
        }
    }

    @Test
    @DisplayName("Проверяем код и тело ответа для успешной авторизации пользователя")
    @Description("Отправляем валидные данные пользователя и проверяем успешную авторизацию пользователя и получение кода 200")
    public void authorisationUserTest() {

        userClient.login(user)
                .then()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя с неверным паролем")
    @Description("Пытаемся авторизовать пользователя с неверным паролем, проверяем получение ошибки 401")
    public void authorisationUserWrongPassTest() {

        user.setPassword(user.getPassword()+"wrongPass");

        userClient.login(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя с неверным email")
    @Description("Пытаемся авторизовать пользователя с неверным email, проверяем получение ошибки 401")
    public void authorisationUserWrongEmailTest() {

        user.setEmail("wrongEmail" + user.getEmail());

        userClient.login(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя c email null в теле запроса")
    @Description("Пытаемся авторизовать пользователя c email null в запросе, проверяем получение ошибки 401")
    public void authorisationUserNullEmailTest() {

        user.setEmail(null);

        userClient.login(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя c email \"\" в теле запроса")
    @Description("Пытаемся авторизовать пользователя c email \"\" в запросе, проверяем получение ошибки 401")
    public void authorisationUserWithEmptyEmailTest() {

        user.setEmail("");

        userClient.login(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя без передачи email в теле запроса")
    @Description("Пытаемся авторизовать пользователя без передачи email в запросе, проверяем получение ошибки 401")
    public void authorisationUserWithoutEmailTest() {

        User userTest = new User()
                .withPassword(user.getPassword());

        userClient.login(userTest)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя c паролем null в теле запроса")
    @Description("Пытаемся авторизовать пользователя c паролем null  запросе, проверяем получение ошибки 401")
    public void authorisationUserNullPassTest() {

        user.setPassword(null);

        userClient.login(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя c паролем \"\" в теле запроса")
    @Description("Пытаемся авторизовать пользователя c паролем \"\" в запросе, проверяем получение ошибки 401")
    public void authorisationUserWithEmptyPassTest() {

        user.setPassword("");

        userClient.login(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Ошибка при авторизации пользователя без передачи пароля в теле запроса")
    @Description("Пытаемся авторизовать пользователя без передачи пароля в запросе, проверяем получение ошибки 401")
    public void authorisationUserWithoutPassTest() {

        User userTest = new User()
                .withEmail(user.getEmail());

        userClient.login(userTest)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
