package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackWebPageRepository extends JpaRepository<FeedbackWebPage, Long>
{
    List<FeedbackWebPage> findAllByUser(User user);
}
