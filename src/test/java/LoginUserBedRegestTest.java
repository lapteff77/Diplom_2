import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class LoginUserBedRegestTest {

    private String token;
    private int actualCode;
    Random random = new Random();
    private final int expectedCodeForTest = 403;
    private final int expectedCodeForOk = 200;
    private final int expectedCodeForSetOver = 202;
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
        RestAssured.baseURI = AdressClass.baseAdress;
    }

    @Test
    @DisplayName("userBedeRegTestWOEmail") // имя теста
    @Description("User - Create, doubles request on API with out email, with password and name") // описание теста
    public void userBedeRegTestWOEmail() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(unCorrectEmail, password, name);
        actualCode = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(AdressClass.regUrl)
                .then()
                .log().all()
                .extract()
                .statusCode();
        assertEquals(expectedCodeForTest, actualCode);

        if (actualCode == expectedCodeForOk) {
            LoginUser loginUserReg = new LoginUser(unCorrectEmail, password);
            token = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginUserReg)
                    .when()
                    .post(AdressClass.regLogin)
                    .then()
                    .log().all()
                    .statusCode(expectedCodeForOk)
                    .extract()
                    .body()
                    .path("accessToken");

            Response response =
                    given()
                            .auth()
                            .oauth2(token.replace("Bearer ", ""))
                            .delete(AdressClass.regUser);
            response.then().statusCode(expectedCodeForSetOver);
        }
    }

    @Test
    @DisplayName("userBedeRegTestWOPass") // имя теста
    @Description("User - Create, doubles request on API with email, with out password and with name") // описание теста
    public void userBedeRegTestWOPass() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
        LoginUser loginUser = new LoginUser(email, unCorrectPassword, name);
        actualCode = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(AdressClass.regUrl)
                .then()
                .log().all()
                .extract()
                .statusCode();
        assertEquals(expectedCodeForTest, actualCode);

        if (actualCode == expectedCodeForOk) {
            LoginUser loginUserReg = new LoginUser(email, unCorrectPassword);
            token = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginUserReg)
                    .when()
                    .post(AdressClass.regLogin)
                    .then()
                    .log().all()
                    .statusCode(expectedCodeForOk)
                    .extract()
                    .body()
                    .path("accessToken");

            Response response =
                    given()
                            .auth()
                            .oauth2(token.replace("Bearer ", ""))
                            .delete(AdressClass.regUser);
            response.then().statusCode(expectedCodeForSetOver);
        }

    }

    @Test
    @DisplayName("userBedeRegTestWOName") // имя теста
    @Description("User - Create, doubles request on API with email, password and with out name") // описание теста
    public void userBedeRegTestWOName() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
        LoginUser loginUser = new LoginUser(email, password, unCorrectName);
        actualCode = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(AdressClass.regUrl)
                .then()
                .log().all()
                .extract()
                .statusCode();
        assertEquals(expectedCodeForTest, actualCode);

        if (actualCode == expectedCodeForOk) {
            LoginUser loginUserReg = new LoginUser(email, password);
            token = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginUserReg)
                    .when()
                    .post(AdressClass.regLogin)
                    .then()
                    .log().all()
                    .statusCode(expectedCodeForOk)
                    .extract()
                    .body()
                    .path("accessToken");

            Response response =
                    given()
                            .auth()
                            .oauth2(token.replace("Bearer ", ""))
                            .delete(AdressClass.regUser);
            response.then().statusCode(expectedCodeForSetOver);
        }
    }

    @Test
    @DisplayName("userBedeRegTestWOAll") // имя теста
    @Description("User - Create, doubles request on API with out all data: email, password and name") // описание теста
    public void userBedeRegTestWOAll() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(unCorrectEmail, unCorrectPassword, unCorrectName);
        actualCode = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(AdressClass.regUrl)
                .then()
                .log().all()
                .extract()
                .statusCode();
        assertEquals(expectedCodeForTest, actualCode);

        if (actualCode == expectedCodeForOk) {
            LoginUser loginUserReg = new LoginUser(unCorrectEmail, unCorrectPassword);
            token = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginUserReg)
                    .when()
                    .post(AdressClass.regLogin)
                    .then()
                    .log().all()
                    .statusCode(expectedCodeForOk)
                    .extract()
                    .body()
                    .path("accessToken");

            Response response =
                    given()
                            .auth()
                            .oauth2(token.replace("Bearer ", ""))
                            .delete(AdressClass.regUser);
            response.then().statusCode(expectedCodeForSetOver);
        }
    }
}

