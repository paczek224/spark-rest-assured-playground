package tests.xml;

import lombok.Data;
import lombok.NoArgsConstructor;

/*@XmlRootElement(name = "helloResponse")*/
@NoArgsConstructor
@Data
public class HelloResponse {
    String message;
    private Author author;
}
