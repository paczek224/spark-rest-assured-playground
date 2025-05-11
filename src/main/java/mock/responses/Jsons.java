package mock.responses;

import com.google.gson.Gson;
import mock.dto.Student;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Jsons {

    private static final Set<Student> students = new HashSet<>();

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

    public static String addNewStudent(Request request, Response response) {
        String student = createStudent(request.body());

        response.type("application/json");
        response.status(201);

        return student;
    }

    public static String getStudents(Request req, Response response) {
        response.type("application/json");
        Gson gson = new Gson();
        String lastName = req.queryParams("lastName");

        return gson.toJson(students
                .stream()
                .filter(s -> Objects.isNull(lastName) || s.getLastName().equals(lastName))
                .toList()
        );
    }

    public static String uploadStudent(Request req, Response response) throws ServletException, IOException {

        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));

        try (InputStream is = req.raw().getPart("file").getInputStream()) {

            String student = createStudent(new String(is.readAllBytes()));

            response.type("application/json");
            response.status(201);

            return student;
        }
    }

    private static String createStudent(String content) {
        Gson gson = new Gson();

        int id = getNewStudentId();
        Student newStudent = gson.fromJson(content, Student.class);
        newStudent.setId(id);
        students.add(newStudent);

        return gson.toJson(newStudent);
    }

    private static int getNewStudentId() {
        return students.stream()
                .max(Comparator.comparing(Student::getId))
                .map(Student::getId)
                .orElse(0) + 1;
    }
}
