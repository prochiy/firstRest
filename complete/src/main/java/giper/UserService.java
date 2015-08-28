package giper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by prochiy on 8/27/15.
 */
public interface UserService {
    public User create(User user);
    public User delete(long id) throws UserNotFound;
    public List findAll();
    public User update(User user) throws UserNotFound;
    public Map<String, Object> update(Long id, Boolean newStatus) throws UserNotFound;
    public User findById(long id);
    public List<User> findByStatusOrCreatedAt(Boolean status, Date timestamp);
}
