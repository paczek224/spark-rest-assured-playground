package tests.xml;

import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class XmlTest {

    @BeforeAll
    public static void setup() {


        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4567;
        RestAssured.filters(
                new ResponseLoggingFilter(io.restassured.filter.log.LogDetail.ALL, System.out)
        );
    }

    @Test
    public void shouldBeAbleToAssertXmlResponse() {

        Response response = given()
                .accept("application/xml")
                .when()
                .get("/hello-xml");

        response
                .then()
                .statusCode(200)
                .contentType("application/xml")
                .body("helloResponse2.message", equalTo("Witaj świecie")
                        , "helloResponse2.author.name", equalTo("Jan Kowalski"))
                .body(hasXPath("//helloResponse2//name[text()='Jan Kowalski']"));

        HelloResponse helloResponse2 = response.then().extract().as(HelloResponse.class);
        Assertions.assertEquals("Witaj świecie", helloResponse2.message);
        Assertions.assertEquals("Jan Kowalski", helloResponse2.getAuthor().getName());
    }

    @Test
    public void shouldBeAbleToUseGroovyApiListSyntaxOnExampleShoppingXmlResponse() {

        Response response = given()
                .accept("application/xml")
                .when()
                .get("/hello-xml-shopping");

        response
                .then()
                .statusCode(200)
                .contentType("application/xml")
                .body("**.find { it.@quantity == 4}", is("Pens"));
    }
}
