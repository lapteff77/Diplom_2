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

public class UserOrderListTest {

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
        CreateOrder createOrder = new CreateOrder("61c0c5a71d1f82001bdaaa6d");
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

        token = token.replace("Bearer ", "");
        given()

                .auth()
                .oauth2(token)
                .headers("Content-Type", "application/json")
                .body(createOrder)
                .post("/api/orders")
                .then()
                .statusCode(200);
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
    @DisplayName("userOrderListTestWithToken") // имя теста
    @Description("OrderList - get on API with accessToken") // описание теста
    public void userOrderListTestWithToken() {
        Response response =
                given()
                        .auth()
                        .oauth2(token)
                        .headers("Content-Type", "application/json")
                        .get("/api/orders");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("userOrderListTestWOToken") // имя теста
    @Description("OrderList - get on API with out accessToken") // описание теста
    public void userOrderListTestWOToken() {
        Response response =
                given()
                        .headers("Content-Type", "application/json")
                        .get("/api/orders");
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}

