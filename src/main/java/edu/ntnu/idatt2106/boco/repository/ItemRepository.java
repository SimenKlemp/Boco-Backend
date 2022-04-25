package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, CustomItemRepository
{
    List<Item> findAllByUser(User user);

    List<Item> findAllByTitleContaining(String title);

    List<Item> findAllByCategory(String category);
}
