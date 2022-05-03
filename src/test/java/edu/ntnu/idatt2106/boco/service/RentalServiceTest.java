package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest
{
    @InjectMocks
    private RentalService rentalService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RentalRepository rentalRepository;

    @BeforeEach
    public void beforeEach()
    {
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockImageRepository(imageRepository);
        RepositoryMock.mockItemRepository(itemRepository);
        RepositoryMock.mockRentalRepository(rentalRepository);
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
