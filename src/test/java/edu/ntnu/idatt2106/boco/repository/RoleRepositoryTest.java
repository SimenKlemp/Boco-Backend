package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.ERole;
import edu.ntnu.idatt2106.boco.models.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Container
    MySQLContainer container =(MySQLContainer)new MySQLContainer("mysql:latest")
            .withDatabaseName("mariuskp")
            .withUsername("mariuskp")
            .withPassword("");

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void saveRoleTest(){
        Role expectedRole= new Role(ERole.ROLE_ADMIN);
        Role actualRole= roleRepository.save(expectedRole);

        assertThat(actualRole).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedRole);
    }

}