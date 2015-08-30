package giper;

import org.springframework.cache.annotation.Cacheable;
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
    public User findById(long id) throws UserNotFoundException {
        User user = userRepository.findOne((long) id);
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    @Override
    @Cacheable("user")
    public List findByStatusOrCreatedAt(Boolean status, Date timestamp) {
        simulateSlowService();
        List<User> userList = userRepository.findByStatusOrCreatedAt(status, timestamp);
        System.out.println("findByStatusOrTimestamp " + userList.size());
        return userRepository.findByStatusOrCreatedAt(status, timestamp);
    }

    @Override
    @Transactional(rollbackFor= UserNotFoundException.class)
    public User delete(long id) throws UserNotFoundException {
        User deletedUser = userRepository.findOne(id);

        if (deletedUser == null)
            throw new UserNotFoundException();

        userRepository.delete(deletedUser);
        return deletedUser;
    }

    @Override
    @Transactional
    public List findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor= UserNotFoundException.class)
    public User update(User user) throws UserNotFoundException {
        User updatedUser = userRepository.findOne(user.getId());

        if (updatedUser == null)
            throw new UserNotFoundException();

        updatedUser.setName(user.getName());
        updatedUser.setFamily(user.getFamily());
        return updatedUser;
    }

    @Override
    @Transactional(rollbackFor= UserNotFoundException.class)
    public Map<String, Object> update(Long id, Boolean newStatus) throws UserNotFoundException {
        User updatedUser = userRepository.findOne(id);

        if (updatedUser == null)
            throw new UserNotFoundException();

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
