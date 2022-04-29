package edu.ntnu.idatt2106.boco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.ntnu.idatt2106.boco.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT AVG(Rating) FROM Rating WHERE Rating.userId = ?1")
    int getMeanRating(Long userId);
}
