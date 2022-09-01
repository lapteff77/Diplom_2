import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.LoginUser;
import praktikum.CreateOrder;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserOrderListTest {

    String token;
    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String ingredientsReal = "61c0c5a71d1f82001bdaaa6d";
    private final int expectedCodeForOk = 200;
    private final int expectedCodeForSetOver = 202;
    private final int expectedCodeForTest = 401;


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseAdress;
        LoginUser loginUser = new LoginUser(email, password, name);
        CreateOrder createOrder = new CreateOrder(ingredientsReal);
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

        given()
                .auth()
                .oauth2(token.replace("Bearer ", ""))
                .headers("Content-Type", "application/json")
                .body(createOrder)
                .post(AdressClass.regOrders)
                .then()
                .statusCode(expectedCodeForOk);
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
    @DisplayName("userOrderListTestWithToken") // имя теста
    @Description("OrderList - get on API with accessToken") // описание теста
    public void userOrderListTestWithToken() {
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .get(AdressClass.regOrders);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }

    @Test
    @DisplayName("userOrderListTestWOToken") // имя теста
    @Description("OrderList - get on API with out accessToken") // описание теста
    public void userOrderListTestWOToken() {
        Response response =
                given()
                        .headers("Content-Type", "application/json")
                        .get(AdressClass.regOrders);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTest);
    }
}

