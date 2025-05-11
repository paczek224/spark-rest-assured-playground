package tests.json;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JsonTest {

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://localhost:4567";

        RestAssured.port = 4567;
        RestAssured.filters(
                new RequestLoggingFilter(),
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

    @Test
    public void shouldBeAbleToPostNewStudent() {

        final String firstName = "firstName";
        final String lastName = "lastName";

        int numberOfStudentsIdsBeforeCreation = given()
                .get("/get-students")
                .then()
                .extract()
                .jsonPath()
                .getList("id", Long.class)
                .size();

        final String firstStudentName = "Lukasz";
        final String firstStudentLastName = "Paczek";
        final int firstStudentId = given()
                .body(Map.of(firstName, firstStudentName, lastName, firstStudentLastName))
                .when()
                .post("/student-post-example")
                .then()
                .body(firstName, equalTo(firstStudentName))
                .body(lastName, equalTo(firstStudentLastName))
                .body("id", notNullValue())
                .extract()
                .jsonPath().getInt("id");

        final String secondStudentName = "Jan";
        final String secondStudentLastName = "Kowalski";
        final int secondStudentId = firstStudentId + 1;
        given()
                .body(Map.of(firstName, secondStudentName, lastName, secondStudentLastName))
                .when()
                .post("/student-post-example")
                .then()
                .body(firstName, equalTo(secondStudentName))
                .body(lastName, equalTo(secondStudentLastName))
                .body("id", is(secondStudentId));


        ValidatableResponse then = given()
                .get("/get-students")
                .then();

        then.body("", hasItems(
                allOf(hasEntry(firstName, firstStudentName), hasEntry(lastName, firstStudentLastName)),
                allOf(hasEntry(firstName, secondStudentName), hasEntry(lastName, secondStudentLastName))
        ));

        final String GROOVY_FORMAT = "findAll {it.firstName == '%s' && it.lastName == '%s' && it.id == %s}";
        then.body(String.format(GROOVY_FORMAT, firstStudentName, firstStudentLastName, firstStudentId), not(empty()));
        then.body(String.format(GROOVY_FORMAT, secondStudentName, secondStudentLastName, secondStudentId), not(empty()));
        then.body("count {it}", equalTo(numberOfStudentsIdsBeforeCreation + 2));

        given()
                .queryParams(lastName, firstStudentLastName)
                .get("/get-students")
                .then()
                .body("id", hasItem(firstStudentId));
    }

    @SneakyThrows
    @Test
    public void shouldBeAbleToAddNewStudentFromFile() {

        File file = Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource("new-student.json")).toURI()).toFile();

        given()
                .multiPart("file", file)
                .post("/upload-students")
                .then()
                .body("firstName", is("Marian"))
                .body("lastName", is("Nowak"))
                .body("id", notNullValue()) ;
    }
}
