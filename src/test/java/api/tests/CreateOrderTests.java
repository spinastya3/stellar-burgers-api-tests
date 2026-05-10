package api.tests;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import models.User;
import org.junit.Before;
import org.junit.Test;
import service.*;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTests extends BaseTest {

    private final OrderClient orderClient = new OrderClient();
    private final IngredientsClient ingredientsClient = new IngredientsClient();

    private Order order;

    @Before
    public void setUp() {

            List<String> ingredients = ingredientsClient
                    .getIngredients()
                    .path("data._id.shuffled().take(3)");
            order = OrderGenerator.order(ingredients);

    }

    @Test
    @DisplayName("Проверяем код и тело ответа для успешного создания заказа")
    @Description("Отправляем токен авторизованного пользователя, отправляем заказ с 3 ингредиентами, проверяем успешное создание заказа и получение кода 200")
    public void createOrderTest(){

        User user = UserGenerator.user();
        String accessToken = userClient.createUserAndLoginUser(user);

        if (accessToken != null) {
            accessTokens.add(accessToken);
        }

        orderClient.create(accessToken, order)
                .then()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .body("name", is(notNullValue()))
                .body("order.number", is(notNullValue()))
                .body("order.ingredients._id", equalTo(order.getIngredients()));
    }

    @Test
    @DisplayName("Ошибка при создании заказа для неавторизованного пользоваетля")
    @Description("Создаем заказ для неавторизованного пользователя, проверяем код 400")
    public void createOrderWithoutAuthorizationTest(){

        orderClient.create("", order)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false));
    }
}
