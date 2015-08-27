package giper;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by prochiy on 8/22/15.
 */
public class TestUserController {

    public static void addUser(){
        TestRestTemplate template = new TestRestTemplate();
        URL base = null;
        try {
            base = new URL("http://localhost:" + 8080 + "/user");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName("Саша");
        user.setFamily("Маринин");
        user.setImageURL("/book/1.png");
        user.setStatus(false);
        user.setAge(31);
        //ResponseEntity<String> response = template.getForEntity(base.toString(), String.class, user);
        ResponseEntity<Integer> response = template.postForEntity(base.toString(), user, Integer.class);
        System.out.println(response.getBody());
    }

    public static void getUser(){
        TestRestTemplate template = new TestRestTemplate();
        URL base = null;
        try {
            base = new URL("http://localhost:" + 8080 + "/user/5");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //ResponseEntity<String> response = template.getForEntity(base.toString(), String.class, user);
        //ResponseEntity<Integer> response = template.postForEntity(base.toString(), user, Integer.class);
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        System.out.println(response.getBody().getClass());
        System.out.println(response.getBody());
    }

    public static void updateStatus(){
        TestRestTemplate template = new TestRestTemplate();
        URL base = null;
        try {
            base = new URL("http://localhost:" + 8080 + "/user/5?status=true");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //ResponseEntity<String> response = template.getForEntity(base.toString(), String.class, user);
        //ResponseEntity<Integer> response = template.postForEntity(base.toString(), user, Integer.class);
        Map<String, Object> response = new HashMap<>();
        template.put(base.toString(), response);
        System.out.println(response);
    }

    public static void getStatistics(){
        TestRestTemplate template = new TestRestTemplate();
        URL base = null;
        try {
            base = new URL("http://localhost:" + 8080 + "/user/statistics?status=false&timestamp=1234495");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        ResponseEntity<List> response = template.getForEntity(base.toString(), List.class);
        //ResponseEntity<Integer> response = template.postForEntity(base.toString(), user, Integer.class);

        System.out.println(response.getBody());
    }

    public static void main(String[] args){

        getUser();
        //addUser();
        //updateStatus();
        //getStatistics();

    }
}
