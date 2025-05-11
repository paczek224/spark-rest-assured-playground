package tests.auth;

import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AuthTest {

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://localhost:4567";
        RestAssured.filters(
                new ResponseLoggingFilter(io.restassured.filter.log.LogDetail.ALL, System.out)
        );
    }

    @Test
    public void shouldAuthorizeWithValidBearerToken() {

        Response response = given()
                .header("Authorization", "Bearer secret-token" )
                .when()
                .get("/secure");

        response
                .then()
                .statusCode(200)
                .body(equalTo("Authorized!"));
    }

    @Test
    public void shouldAuthorizeWithBasicAuthentication() {

        Response response = given()
                .auth()
                .basic("admin", "password")
                .when()
                .get("/secure-basic");

        response
                .then()
                .statusCode(200)
                .body(equalTo("Authorized!"));
    }

    @Test
    public void shouldObtainAccessTokenAndAccessSecureEndpointUsingOauth2Authentication() {

        String accessToken = given()
                .formParam("client_id", "my-client")
                .formParam("client_secret", "my-secret")
                .when()
                .post("/token")
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        Response response = given()
                .auth()
                .oauth2(accessToken)
                .when()
                .get("/secure-oauth");

        response
                .then()
                .statusCode(200)
                .body(equalTo("Access granted!"));
    }
}
