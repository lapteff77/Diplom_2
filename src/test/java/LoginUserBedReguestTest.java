import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;


public class LoginUserBedReguestTest {

    Random random = new Random();

    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String unCorrectEmail = "";
    private final String unCorrectPassword = "";
    private final String unCorrectName = "";

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
    }

    @Test
    @DisplayName("userBedeRegTestWOEmail") // имя теста
    @Description("User - Create, doubles request on API with out email, with password and name") // описание теста
    public void userBedeRegTestWOEmail() {
        LoginUser loginUser = new LoginUser(unCorrectEmail, password, name);
        LoginUser loginUserReg = new LoginUser(unCorrectEmail, password);
        LoginUser.userRegBedCreate(loginUser);
        LoginUser.noExpectedActualCode(loginUserReg);
        LoginUser.setUserRegBedCreate();

    }

    @Test
    @DisplayName("userBedeRegTestWOPass") // имя теста
    @Description("User - Create, doubles request on API with email, with out password and with name") // описание теста
    public void userBedeRegTestWOPass() {
        LoginUser loginUser = new LoginUser(email, unCorrectPassword, name);
        LoginUser loginUserReg = new LoginUser(email, unCorrectPassword);
        LoginUser.userRegBedCreate(loginUser);
        LoginUser.noExpectedActualCode(loginUserReg);
        LoginUser.setUserRegBedCreate();
    }

    @Test
    @DisplayName("userBedeRegTestWOName") // имя теста
    @Description("User - Create, doubles request on API with email, password and with out name") // описание теста
    public void userBedeRegTestWOName() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
        LoginUser loginUser = new LoginUser(email, password, unCorrectName);
        LoginUser loginUserReg = new LoginUser(email, password);
        LoginUser.userRegBedCreate(loginUser);
        LoginUser.noExpectedActualCode(loginUserReg);
        LoginUser.setUserRegBedCreate();
    }


    @Test
    @DisplayName("userBedeRegTestWOAll") // имя теста
    @Description("User - Create, doubles request on API with out all data: email, password and name") // описание теста
    public void userBedeRegTestWOAll() {
        LoginUser loginUser = new LoginUser(unCorrectEmail, unCorrectPassword, unCorrectName);
        LoginUser loginUserReg = new LoginUser(unCorrectEmail, unCorrectPassword);
        LoginUser.userRegBedCreate(loginUser);
        LoginUser.noExpectedActualCode(loginUserReg);
        LoginUser.setUserRegBedCreate();
    }
}


