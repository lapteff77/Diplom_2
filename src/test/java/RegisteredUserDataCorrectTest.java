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

public class RegisteredUserDataCorrectTest {

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
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .delete("/api/auth/user");
        response.then().statusCode(202);
    }

    @Test
    @DisplayName("registeredUserDataCorrectTest") // имя теста
    @Description("User - replace user data on API with accessToken, email,  password") // описание теста
    public void registeredUserDataCorrectTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        token = token.replace("Bearer ", "");
        LoginUser loginUser = new LoginUser("rumpel234@pochta.net", "4657890");
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .headers("Content-Type", "application/json")
                        .body(loginUser)
                        .patch("/api/auth/user");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("registeredUserDataWithDoublesMailTest") // имя теста
    @Description("User - replace user data on API with accessToken, repeated email,  password") // описание теста
    public void registeredUserDataWithDoublesMailTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        token = token.replace("Bearer ", "");
        LoginUser loginUser = new LoginUser(email, "4657890");
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .headers("Content-Type", "application/json")
                        .body(loginUser)
                        .patch("/api/auth/user");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("unregisterUserDataCorrectTest") // имя теста
    @Description("User - login on API  email,  password but w/o accessToken") // описание теста
    public void unregisterUserDataCorrectTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        token = token.replace("Bearer ", "");
        LoginUser loginUser = new LoginUser(email, password);
        Response response =
                given()
                        .headers("Content-Type", "application/json")
                        .body(loginUser)
                        .patch("/api/auth/user");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
