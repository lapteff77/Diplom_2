package praktikum;

import io.restassured.response.Response;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;

public class CreateOrder {
    private String ingredients;

    private static final int expectedCodeForOk = 200;
    private static final int expectedCodeForTest = 401;
    private static final int expectedCodeForTestByInvalidHas = 500;
    private static final int expectedCodeForTestWOBody = 400;
    private static int actualCode;
    private static String token;

    public CreateOrder(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }


    public static void createOrderWORegister(String ingredients) {
        CreateOrder createOrder = new CreateOrder(ingredients);
        actualCode = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrder)
                .when()
                .post(AdressClass.regOrders)
                .then()
                .log().all()
                .extract()
                .statusCode();
        assertNotEquals(expectedCodeForOk, actualCode);
    }


    public static void userOrderListWOToken() {
        Response response =
                given()
                        .headers("Content-Type", "application/json")
                        .get(AdressClass.regOrders);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTest);
    }


    public static void userOrderListTestWithToken(String email, String password) {
        token = LoginUser.getTokenForRegUser(email, password);
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

    public static void createOrderWithToken(String email, String password, String ingredients) {
        CreateOrder createOrder = new CreateOrder(ingredients);
        token = LoginUser.getTokenForRegUser(email, password);
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .get(AdressClass.regOrders);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
        given()
                .auth()
                .oauth2(token.replace("Bearer ", ""))
                .headers("Content-Type", "application/json")
                .body(createOrder)
                .post(AdressClass.regOrders)
                .then()
                .statusCode(expectedCodeForOk);
    }


    public static void createOrderOk(String ingredients, String email, String password) {
        CreateOrder createOrder = new CreateOrder(ingredients);
        token = LoginUser.getTokenForRegUser(email, password);
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .body(createOrder)
                        .post(AdressClass.regOrders);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(expectedCodeForOk);
    }


    public static void createOrderByInvalidHash(String ingredientsUnReal, String email, String password) {
        CreateOrder createOrder = new CreateOrder(ingredientsUnReal);
        token = LoginUser.getTokenForRegUser(email, password);
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .body(createOrder)
                        .post(AdressClass.regOrders);
        response.then().statusCode(expectedCodeForTestByInvalidHas);
    }


    public static void createOrderWOBody(String email, String password) {
        token = LoginUser.getTokenForRegUser(email, password);
        Response response =
                given()
                        .auth()
                        .oauth2(token.replace("Bearer ", ""))
                        .headers("Content-Type", "application/json")
                        .post(AdressClass.regOrders);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(expectedCodeForTestWOBody);
    }
}