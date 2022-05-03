package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Message;
import edu.ntnu.idatt2106.boco.models.Rental;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>
{
    List<Message> findAllByRental(Rental rental, Sort sort);
}
