package api;

import org.junit.After;
import service.UserClient;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    protected List<String> accessTokens = new ArrayList<>();
    protected UserClient userClient = new UserClient();

    @After
    public void tearDown() {
        for (String accessToken : accessTokens) {
            if (accessToken != null) { // Добавь эту строчку
                userClient.delete(accessToken);
            }
        }
    }
}