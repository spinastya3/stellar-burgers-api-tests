package api.tests;

import api.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import models.User;
import org.junit.Before;
import org.junit.Test;
import service.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderWrongIngredientsTests extends BaseTest{

    private final OrderClient orderClient = new OrderClient();

    private Order order;
    private String accessToken;

    @Before
    public void setUp() {

        User user = UserGenerator.user();
        accessToken = userClient.createUserAndLoginUser(user);

        if (accessToken != null) {
            accessTokens.add(accessToken);
        }
    }

    @Test
    @DisplayName("Ошибка при создании заказа без ингредиентов")
    @Description("Ошибка создания пустого заказа получение кода 400")
    public void createEmptyOrderTest(){

        order = OrderGenerator.order(new ArrayList<>());

        orderClient.create(accessToken, order)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Ошибка при создании заказа с неверным хешем ингредиентов")
    @Description("Ошибка создания заказа с неверным хешем ингредиентов и получение кода 500")
    public void createWrongIngredientOrderTest(){

        order = OrderGenerator.order(List.of("ингредиент"));

        orderClient.create(accessToken, order)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
