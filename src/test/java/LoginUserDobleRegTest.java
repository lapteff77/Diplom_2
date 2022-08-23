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

public class LoginUserDobleRegTest {
    private String token;
    private final int expectedCodeForOk =200;
    private final int expectedCodeForSetOver =202;
    private final int expectedCodeForTest = 403;
    Random random = new Random();
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    LoginUser loginUser = new LoginUser(email, password, name);


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseAdress;
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
    @DisplayName("loginUserDoubleRegTest") // имя теста
    @Description("User - Create, doubles request on API with email, password and name") // описание теста
    public void loginUserDoubleRegTest() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regUrl);
        response.then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(expectedCodeForTest );
    }
}
