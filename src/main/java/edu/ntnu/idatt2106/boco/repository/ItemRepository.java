package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>
{
    List<Item> findAllByUser(User user);

    Item findByUser(User user);

    List<Item> findAllByCategory(String category);

   // List<Item> findAllByCategoryContainingOrTitleContaining(String searchWord1, String searchWord2, float greaterThanAmount, float lesThanAmount);

   // List<Item> findAllBy(String searchWord1, String searchWord2, float greaterThanAmount, float lesThanAmount);















}
