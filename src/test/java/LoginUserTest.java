import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;

public class LoginUserTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
    }

    @After
    public void setOver() {
        LoginUser loginUser = new LoginUser(email, password);
        LoginUser.setUserOver(loginUser);
    }

    @Test
    @DisplayName("userRegCreateTestOne") // имя теста
    @Description("User - Create, request on API with email, password and name") // описание теста
    public void userRegCreateTestOne() {
        LoginUser loginUser = new LoginUser(email, password, name);
        LoginUser.userRegCreate(loginUser);
    }
}

