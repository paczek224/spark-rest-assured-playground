package mock.responses;

import spark.Request;
import spark.Response;

public class Jsons {

    public static String getExampleJsonResponse(Request req, Response res) {
        res.type("application/json");
        return """
                {
                "lotto":{
                "price": 12.12,
                 "lottoId":5,
                 "winning-numbers":[2,45,34,23,7,5,3],
                 "winners":[{
                   "winnerId":23,
                   "numbers":[2,45,34,23,3,5]
                 },{
                   "winnerId":54,
                   "numbers":[52,3,12,11,18,22]
                 }]
                }
                }""";
    }

    public static String getExampleJsonStoreResponse(Request req, Response res) {
        res.type("application/json");
        return """
                { \s
                    "store":{ \s
                       "book":[ \s
                          { \s
                             "author":"Nigel Rees",
                             "category":"reference",
                             "price":8.95,
                             "title":"Sayings of the Century"
                          },
                          { \s
                             "author":"Evelyn Waugh",
                             "category":"fiction",
                             "price":12.99,
                             "title":"Sword of Honour"
                          },
                          { \s
                             "author":"Herman Melville",
                             "category":"fiction",
                             "isbn":"0-5.5.11311-3",
                             "price":8.99,
                             "title":"Moby Dick"
                          },
                          { \s
                             "author":"J. R. R. Tolkien",
                             "category":"fiction",
                             "isbn":"0-395-19395-8",
                             "price":22.99,
                             "title":"The Lord of the Rings"
                          }
                       ]
                    }
                 }""";
    }
}
