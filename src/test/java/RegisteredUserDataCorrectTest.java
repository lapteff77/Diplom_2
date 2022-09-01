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

public class RegisteredUserDataCorrectTest {

    String token;
    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String theNewEmail = "rumpel234@pochta.net";
    private final String theNewPassword = "4657890";

    private final int expectedCodeForOk = 200;
    private final int expectedCodeForSetOver = 202;
    private final int expectedCodeForDoublesMailTest = 403;
    private final int expectedCodeForUnregisterUserTest = 401;


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
    @DisplayName("registeredUserDataCorrectTest") // имя теста
    @Description("User - replace user data on API with accessToken, email,  password") // описание теста
    public void registeredUserDataCorrectTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(theNewEmail, theNewPassword);
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .body(loginUser)
                        .patch(AdressClass.regUser);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }

    @Test
    @DisplayName("registeredUserDataWithDoublesMailTest") // имя теста
    @Description("User - replace user data on API with accessToken, repeated email,  password") // описание теста
    public void registeredUserDataWithDoublesMailTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, theNewPassword);
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .body(loginUser)
                        .patch(AdressClass.regUser);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForDoublesMailTest);
    }

    @Test
    @DisplayName("unregisterUserDataCorrectTest") // имя теста
    @Description("User - login on API  email,  password but w/o accessToken") // описание теста
    public void unregisterUserDataCorrectTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, password);
        Response response =
                given()
                        .headers("Content-Type", "application/json")
                        .body(loginUser)
                        .patch(AdressClass.regUser);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForUnregisterUserTest);
    }
}
