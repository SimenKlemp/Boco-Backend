package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Container
    MySQLContainer container =(MySQLContainer)new MySQLContainer("mysql:latest")
            .withDatabaseName("mariuskp")
            .withUsername("mariuskp")
            .withPassword("");

    @Autowired
    UserRepository registrationRepository;

    @Test
    public void saveTest(){
        User expectedUser = new User("name",true,"address", "example@example.com","password", "USER", null);
        User actualUser=registrationRepository.save(expectedUser);
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("userId").isEqualTo(expectedUser);
    }
}