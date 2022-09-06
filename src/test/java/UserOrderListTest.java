import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;
import praktikum.CreateOrder;

import java.util.Random;


public class UserOrderListTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String ingredients = "61c0c5a71d1f82001bdaaa6d";

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
        LoginUser.userRegCreateTwoVar(email, password, name);
        CreateOrder.createOrderWithToken(email, password, ingredients);
    }

    @After
    public void setOver() {
        LoginUser.setUserEndWOTokenIn(email, password);
    }

    @Test
    @DisplayName("userOrderListTestWithToken") // имя теста
    @Description("OrderList - get on API with accessToken") // описание теста
    public void userOrderListTestWithToken() {
        CreateOrder.userOrderListTestWithToken(email, password);
    }

    @Test
    @DisplayName("userOrderListTestWOToken") // имя теста
    @Description("OrderList - get on API with out accessToken") // описание теста
    public void userOrderListTestWOToken() {
        CreateOrder.userOrderListWOToken();
    }
}

