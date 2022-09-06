import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import praktikum.AdressClass;
import praktikum.CreateOrder;


public class CreateOrderTestWORegisterTest {

    private final String ingredients = "61c0c5a71d1f82001bdaaa6d";

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = AdressClass.baseUrl;
    }

    @Test
    @DisplayName("createOrderTestWORegister") // имя теста
    @Description("Order - create on API with out accessToken") // описание теста
    public void createOrderWORegisterTest() {
        CreateOrder.createOrderWORegister(ingredients);
    }
}
