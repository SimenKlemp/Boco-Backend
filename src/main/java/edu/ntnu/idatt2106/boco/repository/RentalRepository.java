package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>
{
    List<Rental> findAllByItem(Item item);

    List<Rental> findAllByUserAndStatus(User user, Rental.Status status);

    List<Rental> findAllByUserAndStatusOrStatus(User user, Rental.Status rejected, Rental.Status canceled);
}
