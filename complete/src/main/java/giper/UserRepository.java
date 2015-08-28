package giper;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.*;

import java.util.Date;
import java.util.List;

/**
 * Created by prochiy on 8/26/15.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    //Page<User> findAll(Pageable pageable);

    //User findByNameAndFamilyAllIgnoringCase(String name, String family);

    List<User> findByName(String Name);
    List<User> findByStatusOrCreatedAt(Boolean status, Date timestamp);
}
