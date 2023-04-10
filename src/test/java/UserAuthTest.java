import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;

public class UserAuthTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String unCorrectEmail = "timba@mail.com";
    private final String unCorrectPassword = "12345";


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
        LoginUser loginUser = new LoginUser(email, password, name);
        LoginUser.setUserUp(loginUser);
    }

    @After
    public void setOver() {
        LoginUser loginUser = new LoginUser(email, password);
        LoginUser.setUserEnd(loginUser);
    }

    @Test
    @DisplayName("userAuthTest") // имя теста
    @Description("User - login on API with email, password") // описание теста
    public void userAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, password);
        LoginUser.userAuthTestOk(loginUser);
    }

    @Test
    @DisplayName("userIncorrectPassAuthTest") // имя теста
    @Description("User - login on API with email, Incorrect password") // описание теста
    public void userIncorrectPassAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, unCorrectPassword);
        LoginUser.userIncorrectAuthTest(loginUser);
    }

    @Test
    @DisplayName("userIncorrectEmailAuthTest") // имя теста
    @Description("User - login on API with Incorrect email,  correct password") // описание теста
    public void userIncorrectEmailAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(unCorrectEmail, password);
        LoginUser.userIncorrectAuthTest(loginUser);
    }

    @Test
    @DisplayName("userIncorrectAllAuthTest") // имя теста
    @Description("User - login on API with Incorrect email, Incorrect password") // описание теста
    public void userIncorrectAllAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(unCorrectEmail, unCorrectPassword);
        LoginUser.userIncorrectAuthTest(loginUser);
    }
}

