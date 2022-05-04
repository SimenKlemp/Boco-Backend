package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class RentalServiceTest
{
    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Before
    public void before()
    {
        for (Rental rental : rentalRepository.findAll())
        {
            rentalService.delete(rental.getRentalId());
        }
    }

    @Test
    public void registerCorrect()
    {

    }

    @Test
    public void registerWrongUserId()
    {

    }

    @Test
    public void registerWrongItemId()
    {

    }

    @Test
    public void registerWrongDeliveryInfo()
    {

    }

    @Test
    public void getCorrect()
    {

    }

    @Test
    public void getWrongRentalId()
    {

    }

    @Test
    public void getAllForItemWithRentals()
    {

    }

    @Test
    public void getAllForItemEmpty()
    {

    }

    @Test
    public void getAllForItemWrongItemId()
    {

    }

    @Test
    public void getAllForUserWithRentals()
    {

    }

    @Test
    public void getAllForUserEmpty()
    {

    }

    @Test
    public void getAllForUserWrongUserId()
    {

    }

    @Test
    public void getAllWhereUserWithRentals()
    {

    }

    @Test
    public void getAllWhereUserEmpty()
    {

    }

    @Test
    public void getAllWhereUserWrongUserId()
    {

    }

    @Test
    public void getAllWhereOwnerWithRentals()
    {

    }

    @Test
    public void getAllWhereOwnerEmpty()
    {

    }

    @Test
    public void getAllWhereOwnerWrongUserId()
    {

    }

    @Test
    public void acceptCorrect()
    {

    }

    @Test
    public void acceptWrongRentalId()
    {

    }

    @Test
    public void rejectCorrect()
    {

    }

    @Test
    public void rejectWrongRentalId()
    {

    }

    @Test
    public void cancelCorrect()
    {

    }

    @Test
    public void cancelWrongRentalId()
    {

    }

    @Test
    public void deleteWithoutAnything()
    {

    }

    @Test
    public void deleteWithRating()
    {

    }

    @Test
    public void deleteWithMessage()
    {

    }

    @Test
    public void deleteWithNotification()
    {

    }
}
