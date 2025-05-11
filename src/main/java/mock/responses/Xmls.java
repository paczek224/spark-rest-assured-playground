package mock.responses;

import spark.Request;
import spark.Response;

public class Xmls {
    public static String getExampleXmlResponse(Request req, Response res) {
        res.type("application/xml");
        return """
                    <helloResponse2><message>Witaj świecie</message>
                    <author>
                            <name>Jan Kowalski</name>
                            <email>jan@example.com</email>
                        </author>
                    </helloResponse2>""";
    }

    public static String getExampleXmlShoppingResponse(Request req, Response res) {
        res.type("application/xml");
        return """
                <shopping>
                      <category type="groceries">
                        <item>Chocolate</item>
                        <item>Coffee</item>
                      </category>
                      <category type="supplies">
                        <item>Paper</item>
                        <item quantity="4">Pens</item>
                      </category>
                      <category type="present">
                        <item when="Aug 10">Kathryn's Birthday</item>
                      </category>
                </shopping>""";
    }
}
