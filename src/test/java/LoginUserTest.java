import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUser;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {

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
    }


    @Test
    @DisplayName("userRegCreateTestOne") // имя теста
    @Description("User - Create, request on API with email, password and name") // описание теста
    public void userRegCreateTestOne() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post("/api/auth/register");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
}

