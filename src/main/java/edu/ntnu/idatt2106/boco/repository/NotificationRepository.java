package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Notification;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>
{
    List<Notification> findAllByUser(User user);

    List<Notification> findAllByRental(Rental rental);

    boolean existsByNotificationStatusAndRental(String status, Rental rental);
}
