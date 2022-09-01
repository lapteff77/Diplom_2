import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;

public class RegisteredUserDataCorrectTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String theNewEmail = "rumpel234@pochta.net";
    private final String theNewPassword = "4657890";


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
    }


    @Test
    @DisplayName("registeredUserDataCorrectTest") // имя теста
    @Description("User - replace user data on API with accessToken, email,  password") // описание теста
    public void registeredUserDataCorrectTest() {
        LoginUser.registeredUserDataCorrect(email, password, name, theNewEmail, theNewPassword);
    }

    @Test
    @DisplayName("registeredUserDataWithDoublesMailTest") // имя теста
    @Description("User - replace user data on API with accessToken, repeated email,  password") // описание теста
    public void registeredUserDataWithDoublesMailTest() {
        LoginUser.registeredUserDataWithDoublesMail(email, password, name, theNewPassword);
    }

    @Test
    @DisplayName("unregisterUserDataCorrectTest") // имя теста
    @Description("User - login on API  email,  password but w/o accessToken") // описание теста
    public void unregisterUserDataCorrectTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser.unregisterUserDataCorrect(email, password, name);
    }
}