package giper;

import java.util.List;

/**
 * Created by prochiy on 8/27/15.
 */
public interface UserService {
    public User create(User user);
    public User delete(long id) throws UserNotFound;
    public List findAll();
    public User update(User user) throws UserNotFound;
    public User findById(long id);
}
