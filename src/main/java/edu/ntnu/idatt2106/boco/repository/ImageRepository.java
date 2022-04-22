package edu.ntnu.idatt2106.boco.repository;

import edu.ntnu.idatt2106.boco.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>
{

}
