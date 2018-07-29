package ru.prochiy.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by prochiy on 8/26/15.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String Name);
    List<User> findByStatusOrCreatedAt(Boolean status, Date timestamp);
}
