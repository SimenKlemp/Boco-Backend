package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest
{
    @InjectMocks
    private RentalService rentalService;

    @Mock
    private RentalRepository rentalRepository;

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
    public void getCorrect()
    {

    }

    @Test
    public void getWrongRentalId()
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
}
