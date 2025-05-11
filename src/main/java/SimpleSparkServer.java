import mock.responses.Auth;
import mock.responses.Jsons;
import mock.responses.Xmls;

import static spark.Spark.*;

public class SimpleSparkServer {

    public static void main(String[] args) {
        port(4567);

        // jwt token auth
        //basic login/password
        //oauth2 authorization token

        get("/secure", Auth::getExampleTokenAuthResponse);
        get("/secure-basic", Auth::getExampleSecureBasicResponse);
        post("/token", Auth::getOauthTokenResponse);
        get("/secure-oauth", Auth::getExampleSecureAuthResponse);

        //json-paths
        get("/hello-xml", Xmls::getExampleXmlResponse);
        get("/hello-xml-shopping", Xmls::getExampleXmlShoppingResponse);
        get("/json-example", Jsons::getExampleJsonResponse);
        get("/json-example-shop", Jsons::getExampleJsonStoreResponse);
        get("/json-annonymous", "application/json", (request, response) -> {
            response.type("application/json");
            return "[1, 2, 3]";
        });

        //posting
        post("student-post-example", Jsons::addNewStudent);
        get("get-students", Jsons::getStudents);
    }
}
