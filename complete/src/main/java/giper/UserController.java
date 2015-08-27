package giper;

import database.JDBCUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by prochiy on 8/22/15.
 */


@EnableAsync
//@Service
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "image", method = RequestMethod.POST)
    public String getImageURL(@RequestBody byte[] image){
        String url = ImageProc.write(image);
        return url;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public long createUser(@RequestBody User user){
        System.out.println("userRepository = " + userRepository);
        userRepository.save(user);
        return user.getId();
    }

    //@Async(value = "restLowPriority")
    //@Async
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") long id){
        System.out.println("-------- id = " + id + " --------");
        User user = userRepository.findOne(id);
        System.out.println(user);
        //return new AsyncResult<String>("Все работает как надо");
        return user;
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.PUT)
    public Map<String, Object> updateStatus(@PathVariable("id") int id, @RequestParam("status") boolean status){
        System.out.println("Парметр запроса " + status);
        boolean oldStatus = JDBCUtilities.getJBDCUtilities().updateStatus(id, status);
        Map<String, Object> out = new HashMap<>();
        out.put("id", id);
        out.put("newStatus", status);
        out.put("oldStatus", oldStatus);
        return out;
    }

    @RequestMapping(value = "user/statistics", method = RequestMethod.GET)
    public List<StatisticResponse> getStatistics(@RequestParam(value = "status", required = false, defaultValue = "null") Boolean status,
                                                 @RequestParam(value = "timestamp", required = false, defaultValue = "-1") long timestamp){
        System.out.println("Параметры запроса status = " + status + " timestamp = " + timestamp);
        StatisticResponse statisticResponse = new StatisticResponse();
        statisticResponse.setImageURL("imageURl DFDFKJLJ");
        statisticResponse.setStatus(true);
        statisticResponse.setTimestamp(3333);
        List<StatisticResponse> list = new ArrayList<>();
        list.add(statisticResponse);
        return list;
    }

}
