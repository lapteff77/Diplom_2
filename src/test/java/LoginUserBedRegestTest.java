import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
<<<<<<< HEAD
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
=======
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
>>>>>>> origin/develop2
import praktikum.LoginUser;

import java.util.Random;

import static io.restassured.RestAssured.given;
<<<<<<< HEAD
import static org.junit.Assert.assertEquals;

public class LoginUserBedRegestTest {

    private String token;
    private int actualCode;
    Random random = new Random();
    private final int expectedCodeForTest = 403;
    private final int expectedCodeForOk =200;
    private final int expectedCodeForSetOver =202;
    private final String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    private final String password = "aaa" + random.nextInt(10000000);
    private final String name = "uuu" + random.nextInt(10000000);
    private final String unCorrectEmail = "";
    private final String unCorrectPassword = "";
    private final String unCorrectName = "";
=======
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserBedRegestTest {

    String token;
    Random random = new Random();
    String email = "something" + random.nextInt(10000000) + "@yandex.ru";
    String password = "aaa" + random.nextInt(10000000);
    String name = "uuu" + random.nextInt(10000000);
    LoginUser loginUser = new LoginUser(email, password, name);
>>>>>>> origin/develop2

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
<<<<<<< HEAD
        RestAssured.baseURI = AdressClass.baseAdress;
=======
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

>>>>>>> origin/develop2
    }

    @Test
    @DisplayName("userBedeRegTestWOEmail") // имя теста
    @Description("User - Create, doubles request on API with out email, with password and name") // описание теста
    public void userBedeRegTestWOEmail() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/develop2
    }

    @Test
    @DisplayName("userBedeRegTestWOPass") // имя теста
    @Description("User - Create, doubles request on API with email, with out password and with name") // описание теста
    public void userBedeRegTestWOPass() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
<<<<<<< HEAD
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

=======
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
>>>>>>> origin/develop2
    }

    @Test
    @DisplayName("userBedeRegTestWOName") // имя теста
    @Description("User - Create, doubles request on API with email, password and with out name") // описание теста
    public void userBedeRegTestWOName() {
        // перед началом теста необходимо заполнение уникальными значениями для login password firstName
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/develop2
    }

    @Test
    @DisplayName("userBedeRegTestWOAll") // имя теста
    @Description("User - Create, doubles request on API with out all data: email, password and name") // описание теста
    public void userBedeRegTestWOAll() {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/develop2
    }
}

