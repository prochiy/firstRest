package giper;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import giper.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;



/**
 * Created by prochiy on 8/22/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8080"})
public class ApplicationTest {

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;
    Long id;


    @Before
    public void setUp() throws Exception {
        //this.base = new URL("http://localhost:" + port + "/");
        template = new TestRestTemplate();
        base = new URL("http://localhost:" + port + "/user");
        System.out.println(base.toString());

        User user = new User();
        user.setName("Alexand");
        user.setFamily("Marinin");
        user.setImageURL("/book/1.png");
        user.setStatus(false);
        user.setAge(31);
        ResponseEntity<Long> response = template.postForEntity(base.toString(), user, Long.class);
        assert(response.getBody() != null);
        id = response.getBody();
        System.out.println(response.getBody());
    }

    //@Test
    //public void addImage() throws Exception{
    //
    //    base = new URL("http://localhost:" + port + "/image");
    //    System.out.println(base.toString());
    //    ResponseEntity<String> response = template.postForEntity(base.toString(), ImageProc.read("/home/amarinin/Firefox_wallpaper.jpg"), String.class);
    //    assert(response.getBody() != null);
    //}

    @Test
    public void addUser() throws Exception{

        base = new URL("http://localhost:" + port + "/user");
        System.out.println(base.toString());

        User user = new User();
        user.setName("Alexand");
        user.setFamily("Marinin");
        user.setImageURL("/book/1.png");
        user.setStatus(false);
        user.setAge(31);
        ResponseEntity<Long> response = template.postForEntity(base.toString(), user, Long.class);

        assert(response.getBody() != null);
        System.out.println(response.getBody());
    }

    @Test
    public void getUser() throws Exception {

        base = new URL("http://localhost:" + port + "/user/" + id);
        System.out.println(base.toString());

        ResponseEntity<User> response = template.getForEntity(base.toString(), User.class);
        assert(response.getBody().getId() == id );
        System.out.println(response.getBody());

    }

    @Test
    public void updateStatus() throws Exception {

        base = new URL("http://localhost:" + port + "/user/5?status=true");

        Map<String, Object> response = new HashMap<>();
        template.put(base.toString(), response);
        System.out.println(response);
    }

    @Test
    public void getStatistics() throws Exception{

        base = new URL("http://localhost:" + port + "/user/statistics?status=false");
        System.out.println(base.toString());
        ResponseEntity<List> response = template.getForEntity(base.toString(), List.class);

        for(Object object: response.getBody()){
            System.out.println(object);
        }

    }


}
