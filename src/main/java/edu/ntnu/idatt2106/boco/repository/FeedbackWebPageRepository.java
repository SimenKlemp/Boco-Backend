package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.payload.request.FeedbackWebPageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackWebPageRepository extends JpaRepository<FeedbackWebPage, Long> {

}
