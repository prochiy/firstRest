package ru.prochiy.main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by prochiy on 02.07.18.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan("ru.porochiy")

public class JpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    User user;

    @Before
    public void setUp(){

        user = new User();
        user.setName("Alex");
        user.setFamily("Marinin");
        user.setImageURL("/image/1.png");
        user.setStatus(false);
        user.setAge(31);
        user.setCreatedAt(new Date());
        entityManager.persist(user);
        entityManager.flush();

    }

    @Test
    public void findByName() {

        // when
        User found = userRepository.findByName(user.getName()).get(0);

        // then
        assertThat(found.getName()).isEqualTo(user.getName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void notFoundByName() {

        // when
        User found = userRepository.findByName("Вася").get(0);

    }

    @Test
    public void update_user() {

        // when
        Date date = user.getCreatedAt();
        user.setAge(33);
        entityManager.flush();

        // then
        assertThat(!date.equals(user.getCreatedAt()));
    }

}
