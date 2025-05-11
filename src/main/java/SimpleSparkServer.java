import mock.responses.Auth;
import mock.responses.Jsons;
import mock.responses.Xmls;

import static spark.Spark.*;

public class SimpleSparkServer {


    public static void main(String[] args) {
        port(4567);

        // jwt token auth
        get("/secure", Auth::getExampleTokenAuthResponse);

        //basic login/password
        get("/secure-basic", Auth::getExampleSecureBasicResponse);

        //oauth2 authorization token
        post("/token", Auth::getOauthTokenResponse);
        get("/secure-oauth", Auth::getExampleSecureAuthResponse);

        get("/hello-xml",  Xmls::getExampleXmlResponse);
        get("/hello-xml-shopping",  Xmls::getExampleXmlShoppingResponse);


        get("/json-example",  Jsons::getExampleJsonResponse);
        get("/json-example-shop",  Jsons::getExampleJsonStoreResponse);

        get("/json-annonymous",  "application/json", (request, response) -> {
            response.type("application/json");
            return "[1, 2, 3]";
        });


    }
}
