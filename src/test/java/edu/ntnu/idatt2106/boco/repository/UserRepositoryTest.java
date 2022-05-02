package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Container
    MySQLContainer container = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("mariuskp")
            .withUsername("mariuskp")
            .withPassword("");

    @Autowired
    UserRepository registrationRepository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    public void saveTest() {
        User expectedUser = new User("name", true, "streetAddress", "postalCode", "postOffice", "example@example.com", "password", "USER", null);
        User actualUser = registrationRepository.save(expectedUser);
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("userId").isEqualTo(expectedUser);
    }

    @Test
    public void it_should_save_user() {
        User user = new User("name", true, "streetAddress", "postalCode", "postOffice", "example@example.com", "password", "USER", null);
        user.setName("test name");
        user = entityManager.persistAndFlush(user);

        assertThat(registrationRepository.findById(user.getUserId()).get()).isEqualTo(user);
    }

    @Test
    public void it_should_find_user_byEmail() {
        User user = new User("name", true, "streetAddress", "postalCode", "postOffice", "example@example.com", "password", "USER", null);
        user = entityManager.persistAndFlush(user);
        assertThat(registrationRepository.findByEmail(user.getEmail()).get()).isEqualTo(user);}
}