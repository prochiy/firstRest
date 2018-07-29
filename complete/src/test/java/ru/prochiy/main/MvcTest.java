package ru.prochiy.main;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.prochiy.controllers.UserController;
import ru.prochiy.services.UserService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by prochiy on 8/22/15.
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(value = "ru.prochiy.ru.prochiy.main.Application.class")
@WebMvcTest(UserController.class)
public class MvcTest {

    private int port = 8080;

    private URL base;
    private TestRestTemplate template;
    Long id;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    private User user;

    @Before
    public void setUp() throws Exception {

        user = new User();
        user.setName("Alexandr");
        user.setFamily("Marinin");
        user.setImageURL("/image/1.png");
        user.setStatus(false);
        user.setAge(31);

    }

    @Test
    public void findUser()
            throws Exception {

        given(service.findById(anyLong())).willReturn(user);

        mvc.perform(get("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(user.getName())));
        verify(service, times(1)).findById(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void addUser() throws Exception {

        given(service.create(user)).willReturn(user);

        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(content().string("0"));


        verify(service, times(1)).create(user);
        verifyNoMoreInteractions(service);
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
