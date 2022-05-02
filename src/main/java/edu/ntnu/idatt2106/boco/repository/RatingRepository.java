package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Rating;
import edu.ntnu.idatt2106.boco.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByUser(User user);

}

