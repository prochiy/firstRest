package ru.prochiy.services;

import ru.prochiy.main.User;
import ru.prochiy.main.UserNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by prochiy on 8/27/15.
 */
public interface UserService {
    public User create(User user);
    public User delete(long id) throws UserNotFoundException;
    public List findAll();
    public User update(User user) throws UserNotFoundException;
    public Map<String, Object> update(Long id, Boolean newStatus) throws UserNotFoundException;
    public User findById(long id) throws UserNotFoundException;
    public List<User> findByStatusOrCreatedAt(Boolean status, Date timestamp);
}
