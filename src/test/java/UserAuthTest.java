import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserAuthTest {

    private String token;
    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String unCorrectEmail = "timba@mail.com";
    private final String unCorrectPassword = "12345";
    private final int expectedCodeForOk = 200;
    private final int expectedCodeForSetOver = 202;
    private final int expectedCodeForTest = 401;

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseAdress;
        LoginUser loginUser = new LoginUser(email, password, name);
        token = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(AdressClass.regUrl)
                .then()
                .log().all()
                .statusCode(expectedCodeForOk)
                .extract()
                .body()
                .path("accessToken");
    }

    @After
    public void setOver() {
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .delete(AdressClass.regUser);
        response.then().statusCode(expectedCodeForSetOver);
    }

    @Test
    @DisplayName("userAuthTest") // имя теста
    @Description("User - login on API with email, password") // описание теста
    public void userAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regLogin);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }

    @Test
    @DisplayName("userIncorrectPassAuthTest") // имя теста
    @Description("User - login on API with email, Incorrect password") // описание теста
    public void userIncorrectPassAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, unCorrectPassword);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regLogin);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTest);
    }

    @Test
    @DisplayName("userIncorrectEmailAuthTest") // имя теста
    @Description("User - login on API with Incorrect email,  correct password") // описание теста
    public void userIncorrectEmailAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(unCorrectEmail, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regLogin);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTest);
    }

    @Test
    @DisplayName("userIncorrectAllAuthTest") // имя теста
    @Description("User - login on API with Incorrect email, Incorrect password") // описание теста
    public void userIncorrectAllAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(unCorrectEmail, unCorrectPassword);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regLogin);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTest);
    }
}

