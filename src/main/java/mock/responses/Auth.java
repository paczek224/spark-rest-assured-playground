package mock.responses;

import spark.Request;
import spark.Response;

import java.util.Base64;

public class Auth {

    static final String VALID_CLIENT_ID = "my-client";
    static final String VALID_CLIENT_SECRET = "my-secret";
    static final String VALID_TOKEN = "abc123token";

    public static String getExampleSecureAuthResponse(Request req, Response res) {
        String authHeader = req.headers("Authorization");
        if (authHeader != null && authHeader.equals("Bearer " + VALID_TOKEN)) {
            return "Access granted!";
        } else {
            res.status(401);
            return "Unauthorized";
        }
    }

    public static String getOauthTokenResponse(Request req, Response res) {
        String clientId = req.queryParams("client_id");
        String clientSecret = req.queryParams("client_secret");

        if (VALID_CLIENT_ID.equals(clientId) && VALID_CLIENT_SECRET.equals(clientSecret)) {
            res.type("application/json");
            return "{\"access_token\":\"" + VALID_TOKEN + "\",\"token_type\":\"Bearer\"}";
        } else {
            res.status(401);
            return "{\"error\":\"invalid_client\"}";
        }
    }

    public static String getExampleSecureBasicResponse(Request request, Response response) {
        String authHeader = request.headers("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            if ("admin:password".equals(credentials)) {
                return "Authorized!";
            }
        }

        response.status(401);
        response.header("WWW-Authenticate", "Basic realm=\"Access to the secure site\"");
        return "Unauthorized";
    }

    public static String getExampleTokenAuthResponse(Request request, Response response) {
        String authHeader = request.headers("Authorization");

        if (authHeader != null && authHeader.equals("Bearer secret-token")) {
            return "Authorized!";
        } else {
            response.status(401);
            return "Unauthorized";
        }
    }
}
