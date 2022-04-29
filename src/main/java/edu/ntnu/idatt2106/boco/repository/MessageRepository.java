package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>
{
}
