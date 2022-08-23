import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUser;
import praktikum.CreateOrder;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class CreateOrderTest {

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
        // перед началом теста необходимо заполнение уникальными значениями для login password name
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
    @DisplayName("createOrderTestOk") // имя теста
    @Description("Order - create on API with accessToken") // описание теста
    public void createOrderTestOk() {
        token = token.replace("Bearer ", "");
        CreateOrder createOrder = new CreateOrder("61c0c5a71d1f82001bdaaa6d");
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .headers("Content-Type", "application/json")
                        .body(createOrder)
                        .post("/api/orders");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("createOrderTestByInvalidHash") // имя теста
    @Description("Order - create on API with accessToken with invalid hash") // описание теста
    public void createOrderTestByInvalidHash() {
        token = token.replace("Bearer ", "");
        CreateOrder createOrder = new CreateOrder("78946");
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .headers("Content-Type", "application/json")
                        .body(createOrder)
                        .post("/api/orders");
        response.then().statusCode(500);
    }

    @Test
    @DisplayName("createOrderTestWOBody") // имя теста
    @Description("Order - create on API with accessToken but with out ingredients") // описание теста
    public void createOrderTestWOBody() {
        token = token.replace("Bearer ", "");
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .headers("Content-Type", "application/json")
                        .post("/api/orders");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(400);
    }
}

