package giper;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by prochiy on 8/22/15.
 */
public class TestImageController {
    public static void main(String[] args){
        TestRestTemplate template = new TestRestTemplate();
        URL base = null;
        try {
            base = new URL("http://localhost:" + 8080 + "/image");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> response = template.postForEntity(base.toString(), ImageProc.read(""), String.class);
        System.out.println(response.getBody());
    }
}
