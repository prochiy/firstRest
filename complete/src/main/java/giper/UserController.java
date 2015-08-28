package giper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prochiy on 8/22/15.
 */


@EnableAsync
//@Service
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "image", method = RequestMethod.POST)
    public String getImageURL(@RequestBody byte[] image){
        String url = ImageProc.write(image);
        return url;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public long createUser(@RequestBody User user){
        userService.create(user);
        return user.getId();
    }

    //@Async(value = "restLowPriority")
    //@Async
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") long id){
        System.out.println("-------- id = " + id + " --------");
        User user = userService.findById(id);
        System.out.println(user);
        //return new AsyncResult<String>("Все работает как надо");
        return user;
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.PUT)
    public Map<String, Object> updateStatus(@PathVariable("id") long id, @RequestParam("status") boolean status) throws UserNotFound {
        System.out.println("Парметр запроса id = " + id + " status = " + status);
        //boolean oldStatus = JDBCUtilities.getJBDCUtilities().updateStatus(id, status);

        return userService.update(id, status);
    }

    @RequestMapping(value = "user/statistics", method = RequestMethod.GET)
    public List<Map<String, Object>> getStatistics(@RequestParam(value = "status", required = false) Boolean status,
                                                 @RequestParam(value = "timestamp", required = false) Date timestamp){
        System.out.println("Параметры запроса status = " + status + " timestamp = " + timestamp);
        List<User> userList = userService.findByStatusOrCreatedAt(status, null);
        List<Map<String, Object>> list = new ArrayList<>();
        System.out.println("Коллчество пользователей с заданными параметрами " + userList.size());
        for(User user: userList){
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("imageURL",user.getImageURL());
            map.put("status", user.getCreatedAt());
            list.add(map);
        }

        return list;
    }

}
