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

public class LoginUserBedRegestTest {

    String token;
    Random random = new Random();
    String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    String password = "aaa" + random.nextInt(10000000);
    String name = "uuu" + random.nextInt(10000000);
    LoginUser loginUser = new LoginUser(email, password, name);

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
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
    @DisplayName("loginUserDoubleRegTest") // имя теста
    @Description("User - Create, doubles request on API with email, password and name") // описание теста
    public void loginUserDoubleRegTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/register");
        response.then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);

    }

    @Test
    @DisplayName("userBedeRegTestWOEmail") // имя теста
    @Description("User - Create, doubles request on API with out email, with password and name") // описание теста
    public void userBedeRegTestWOEmail() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        String email = "";
        String password = "aaa" + random.nextInt(10000000);
        String name = "uuu" + random.nextInt(10000000);
        LoginUser loginUser = new LoginUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/register");
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("userBedeRegTestWOPass") // имя теста
    @Description("User - Create, doubles request on API with email, with out password and with name") // описание теста
    public void userBedeRegTestWOPass() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "";
        String name = "uuu" + random.nextInt(10000000);
        LoginUser loginUser = new LoginUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/register");
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("userBedeRegTestWOName") // имя теста
    @Description("User - Create, doubles request on API with email, password and with out name") // описание теста
    public void userBedeRegTestWOName() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "aaa" + random.nextInt(10000000);
        String name = "";
        LoginUser loginUser = new LoginUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/register");
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("userBedeRegTestWOAll") // имя теста
    @Description("User - Create, doubles request on API with out all data: email, password and name") // описание теста
    public void userBedeRegTestWOAll() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        String email = "";
        String password = "";
        String name = "";
        LoginUser loginUser = new LoginUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/register");
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}

