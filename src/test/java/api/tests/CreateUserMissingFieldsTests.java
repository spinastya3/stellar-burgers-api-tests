package api.tests;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static utils.TestData.faker;

@RunWith(Parameterized.class)
public class CreateUserMissingFieldsTests extends BaseTest {


    private final User user;
    private final String testName;

    public CreateUserMissingFieldsTests(User user, String testName) {
        this.user = user;
        this.testName=testName;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] getTestData() {

        return new Object[][]{

                // Пользоветель без email
                {new User().withPassword("testtest").withName(faker.name().firstName()), "поле email отсутствует в JSON"},
                {new User().withEmail(null).withPassword("testtest").withName(faker.name().firstName()), "поле email = null"},
                {new User().withEmail("").withPassword("testtest").withName(faker.name().firstName()), "поле email = \"\""},

                // Пользоветель без пароля
                {new User().withEmail(faker.internet().emailAddress()).withName(faker.name().firstName()), "поле пароль отсутствует в JSON"},
                {new User().withEmail(faker.internet().emailAddress()).withPassword(null).withName(faker.name().firstName()), "поле пароль = null"},
                {new User().withEmail(faker.internet().emailAddress()).withPassword("").withName(faker.name().firstName()), "поле пароль = \"\""},

                // Пользоветель без имени
                {new User().withEmail(faker.internet().emailAddress()).withPassword("testtest"), "поле имя отсутствует в JSON"},
                {new User().withEmail(faker.internet().emailAddress()).withPassword("testtest").withName(null), "поле имя = null"},
                {new User().withEmail(faker.internet().emailAddress()).withPassword("testtest").withName(""), "поле имя = \"\""},
        };
    }

    @Test
    @DisplayName("Ошибка при создании пользователя с неполными данными")
    @Description("Параметризованный тест: Регистрируем пользователя без заполненых обязательных полей, проверяем код 403 и корректность сообщения об ошибке")
    public void createUserWithMissingFieldTest(){

        Response response = userClient.create(user);

        String accessToken = response.path("accessToken");
        if (accessToken != null) {
            accessTokens.add(accessToken);
        }

        response.then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}