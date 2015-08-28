package giper;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prochiy on 8/27/15.
 */
@Configuration
public class UserServiceImpl implements UserService{



    @Resource
    private UserRepository userRepository;

    @Override
    @Transactional
    public User create(User user) {
        User createdUser = user;
        return userRepository.save(createdUser);
    }

    @Override
    @Transactional
    public User findById(long id) {
        return userRepository.findOne((long) id);
    }

    @Override
    //@Cacheable("List")
    public List findByStatusOrCreatedAt(Boolean status, Date timestamp) {
        simulateSlowService();
        List<User> userList = userRepository.findByStatusOrCreatedAt(status, timestamp);
        System.out.println("findByStatusAndTimestamp " + userList.size());
        return userRepository.findByStatusOrCreatedAt(status, timestamp);
    }

    @Override
    @Transactional(rollbackFor=UserNotFound.class)
    public User delete(long id) throws UserNotFound {
        User deletedUser = userRepository.findOne(id);

        if (deletedUser == null)
            throw new UserNotFound();

        userRepository.delete(deletedUser);
        return deletedUser;
    }

    @Override
    @Transactional
    public List findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor=UserNotFound.class)
    public User update(User user) throws UserNotFound {
        User updatedUser = userRepository.findOne(user.getId());

        if (updatedUser == null)
            throw new UserNotFound();

        updatedUser.setName(user.getName());
        updatedUser.setFamily(user.getFamily());
        return updatedUser;
    }

    @Override
    @Transactional(rollbackFor=UserNotFound.class)
    public Map<String, Object> update(Long id, Boolean newStatus) throws UserNotFound {
        User updatedUser = userRepository.findOne(id);

        if (updatedUser == null)
            throw new UserNotFound();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("oldStatus", updatedUser.getStatus());
        map.put("newStatus", newStatus);

        updatedUser.setStatus(newStatus);

        return map;
    }

    private void simulateSlowService() {
        try {
            long time = (long) (5000L);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
