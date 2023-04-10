import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUser;
import praktikum.CreateOrder;
import praktikum.AdressClass;

import java.util.Random;


public class CreateOrderTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String ingredients = "61c0c5a71d1f82001bdaaa6d";
    private final String ingredientsUnReal = "78946";


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
        LoginUser.userRegCreateTwoVar(email, password, name);
    }

    @After
    public void setOver() {
        LoginUser.setUserEndWOTokenIn(email, password);
    }

    @Test
    @DisplayName("createOrderTestOk") // имя теста
    @Description("Order - create on API with accessToken") // описание теста
    public void createOrderOkTest() {
        CreateOrder.createOrderOk(ingredients, email, password);
    }

    @Test
    @DisplayName("createOrderTestByInvalidHash") // имя теста
    @Description("Order - create on API with accessToken with invalid hash") // описание теста
    public void createOrderTestByInvalidHash() {
        CreateOrder.createOrderByInvalidHash(ingredientsUnReal, email, password);
    }

    @Test
    @DisplayName("createOrderTestWOBody") // имя теста
    @Description("Order - create on API with accessToken but with out ingredients") // описание теста
    public void createOrderTestWOBody() {
        CreateOrder.createOrderWOBody(email, password);
    }
}
