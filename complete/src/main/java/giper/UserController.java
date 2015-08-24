package giper;

import database.JDBCUtilities;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prochiy on 8/22/15.
 */
@RestController
public class UserController {

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public Integer createUser(@RequestBody User user){
        return JDBCUtilities.getJBDCUtilities().createUser(user);
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") int id){
        System.out.println("-------- id = " + id + "--------");
        return JDBCUtilities.getJBDCUtilities().getUser(id);

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
