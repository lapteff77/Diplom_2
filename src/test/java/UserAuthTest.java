import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUser;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserAuthTest {

    String token;
    Random random = new Random();
    String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    String password = "aaa" + random.nextInt(10000000);
    String name = "uuu" + random.nextInt(10000000);

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        LoginUser loginUser = new LoginUser(email, password, name);
        token = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .path("accessToken");
    }

    @After
    public void setOver() {
        token = token.replace("Bearer ", "");
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .delete("/api/auth/user");
        response.then().statusCode(202);
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
                        .post("/api/auth/login");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("userIncorrectPassAuthTest") // имя теста
    @Description("User - login on API with email, Incorrect password") // описание теста
    public void userIncorrectPassAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, "12345");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/login");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("userIncorrectEmailAuthTest") // имя теста
    @Description("User - login on API with Incorrect email,  correct password") // описание теста
    public void userIncorrectEmailAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser("timba@mail.com", password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/login");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("userIncorrectAllAuthTest") // имя теста
    @Description("User - login on API with Incorrect email, Incorrect password") // описание теста
    public void userIncorrectAllAuthTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser("timba1@mail.com", "123456");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/login");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}


