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

public class LoginUserTest {

    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final int expectedCodeForOk =200;
    private final int expectedCodeForSetOver =202;
    private String token;

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseAdress;
    }

    @After
    public void setOver() {
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

    @Test
    @DisplayName("userRegCreateTestOne") // имя теста
    @Description("User - Create, request on API with email, password and name") // описание теста
    public void userRegCreateTestOne() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regUrl);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }
}
