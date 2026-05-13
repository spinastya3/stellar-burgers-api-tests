package service;

import models.Order;

import java.util.List;

public class OrderGenerator {
    public static Order order(List<String> ingredients){
        return new Order(ingredients);
    }
}
