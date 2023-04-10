package praktikum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class LoginUser {

    private String email;
    private String password;
    private String name;
    private static String token;
    private static int actualCode;

    private static final int expectedCodeForOk = 200;
    private static final int expectedCodeForSetOver = 202;
    private static final int expectedCodeForTest = 403;
    private static final int expectedCodeForTestTwo = 401;


    public LoginUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static void userRegCreate(LoginUser loginUser) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regUrl);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }

    public static void userRegCreateTwoVar(String email, String password, String name) {
        LoginUser loginUser = new LoginUser(email, password, name);
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regUrl);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }

    public static void userRegDoubleCreate(LoginUser loginUser) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regUrl);
        response.then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(expectedCodeForTest);
    }


    public static void userAuthTestOk(LoginUser loginUser) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regLogin);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }

    public static void userIncorrectAuthTest(LoginUser loginUser) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginUser)
                        .post(AdressClass.regLogin);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTestTwo);
    }


    public static void setUserOver(LoginUser loginUser) {
        token = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
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

    public static void setUserUp(LoginUser loginUser) {
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


    public static void setUserEnd(LoginUser loginUser) {
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .delete(AdressClass.regUser);
        response.then().statusCode(expectedCodeForSetOver);

    }

    public static void setUserEndWOTokenIn(String email, String password) {
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

    public static void registeredUserDataCorrect(String email, String password, String name, String theNewEmail, String theNewPassword) {
        LoginUser loginUser = new LoginUser(email, password, name);
        LoginUser loginUserReg = new LoginUser(theNewEmail, theNewPassword);
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

        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .body(loginUserReg)
                        .patch(AdressClass.regUser);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);

        given()
                .auth()
                .oauth2(token.replace("Bearer ", ""))
                .delete(AdressClass.regUser);
    }

    public static void registeredUserDataWithDoublesMail(String email, String password, String name, String theNewPassword) {
        LoginUser loginUser = new LoginUser(email, password, name);
        LoginUser loginUserReg = new LoginUser(email, theNewPassword);
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

        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .body(loginUserReg)
                        .patch(AdressClass.regUser);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTest);

        given()
                .auth()
                .oauth2(token.replace("Bearer ", ""))
                .delete(AdressClass.regUser);
    }


    public static void unregisterUserDataCorrect(String email, String password, String name) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
        LoginUser loginUser = new LoginUser(email, password, name);
        LoginUser loginUserReg = new LoginUser(email, password);
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

        Response response =
                given()
                        .headers("Content-Type", "application/json")
                        .body(loginUserReg)
                        .patch(AdressClass.regUser);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTestTwo);

        given()
                .auth()
                .oauth2(token.replace("Bearer ", ""))
                .delete(AdressClass.regUser);
    }

    public static String getTokenForRegUser(String email, String password) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
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
        return token;
    }


    public static void userRegBedCreate(LoginUser loginUser) {
        // перед началом теста необходимо заполнение уникальными значениями для login password name
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
    }

    public static void noExpectedActualCode(LoginUser loginUserReg) {
        if (actualCode == expectedCodeForOk) {
            token = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginUserReg)
                    .when()
                    .post(AdressClass.regLogin)
                    .then()
                    .log().all()
                    .extract()
                    .body()
                    .path("accessToken");
            given()
                    .auth()
                    .oauth2(token.replace("Bearer ", ""))
                    .delete(AdressClass.regUser);
        }
    }

    public static void setUserRegBedCreate() {
        assertEquals(expectedCodeForTest, actualCode);
    }
}









