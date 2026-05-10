package service;

import models.User;
import static utils.TestData.faker;

public class UserGenerator {

    public static User user(){
        return  new User()
                .withEmail(faker.internet().emailAddress())
                .withPassword("testtest")
                .withName(faker.name().firstName());
    }
}
