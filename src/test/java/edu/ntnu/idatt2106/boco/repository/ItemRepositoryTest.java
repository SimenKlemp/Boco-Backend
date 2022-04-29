package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class ItemRepositoryTest
{
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void  saveItem()
    {
        User user = new User("name", true, "streetAddress", "postalCode", "postOffice", "example@example.com", "password", "USER", null);
        Item expectedItem = new Item("streetAddress", "postalCode", "postOffice", 1F, "category", "description", "title", new Date(), true, true, null, user);

        Item actualItem = itemRepository.save(expectedItem);

        assertThat(actualItem).usingRecursiveComparison()
                .ignoringFields("itemId").isEqualTo(expectedItem);
    }

}