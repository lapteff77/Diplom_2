import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;

public class LoginUserDoubleRegTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    LoginUser loginUser = new LoginUser(email, password, name);


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
        LoginUser.setUserUp(loginUser);
    }

    @After
    public void setOver() {
        LoginUser loginUser = new LoginUser(email, password);
        LoginUser.setUserEnd(loginUser);
    }

    @Test
    @DisplayName("loginUserDoubleRegTest") // имя теста
    @Description("User - Create, doubles request on API with email, password and name") // описание теста
    public void loginUserDoubleRegTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser.userRegDoubleCreate(loginUser);
    }
}
