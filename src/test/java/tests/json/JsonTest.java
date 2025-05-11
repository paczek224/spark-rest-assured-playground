package tests.json;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JsonTest {

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://localhost:4567";

        RestAssured.port = 4567;
        RestAssured.filters(
                new ErrorLoggingFilter(),
                new ResponseLoggingFilter(io.restassured.filter.log.LogDetail.ALL, System.out)
        );
    }

    @Test
    public void shouldBeAbleToCheckExampleJsonResponse() {

        Response response = given()
                .config(RestAssuredConfig.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)))
                .accept("application/json")
                .when()
                .get("/json-example");

        ValidatableResponse then = response.then();
        then.body("lotto.lottoId", equalTo(5));
        then.body("lotto.winning-numbers", hasItems(2, 3));
        then.body("lotto.price", is(new BigDecimal("12.12")));
    }

    @Test
    public void shouldBeAbleToUseGroovyApiListSyntaxOnExampleStoreJsonResponse() {

        Response response = given()
                .accept("application/json")
                .when()
                .get("/json-example-shop");

        ValidatableResponse then = response.then();
        response.jsonPath().getList("store.book.price", Double.class).forEach(System.out::println);
        then
                .body("store.book.collect { it.price}.sum()", closeTo(53, 1))
                .and()
                .body("store.book.findAll {it}.title", hasItems("The Lord of the Rings"))
                .and()
                .body("store.book.max  {it.price}.title", is("The Lord of the Rings"))
                .body("store.book.find  {it.price > 22 }.title", is("The Lord of the Rings"))
                .body("store.book.findAll  {it.price > 10 }.title", hasItems("The Lord of the Rings"))
                .body("store.book.findAll  { it.price > 10 }.sort { it }.title", is(List.of("Sword of Honour", "The Lord of the Rings")))
                .body("store.book.findAll  { it.price > 10 }.title.sort().reverse()", is(List.of("The Lord of the Rings", "Sword of Honour")));
    }


}
