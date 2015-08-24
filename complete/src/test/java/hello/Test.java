package hello;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by prochiy on 8/21/15.
 */
public class Test {
    public static void main(String[] args){
        TestRestTemplate template = new TestRestTemplate();
        URL base = null;
        try {
            base = new URL("http://localhost:" + 8080 + "/greet");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        System.out.println(response.getBody());
    }
}
