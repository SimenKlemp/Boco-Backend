package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest
{
    @InjectMocks
    private ItemService itemService;

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
    public void registerWithoutImage()
    {

    }

    @Test
    public void registerWithImage()
    {

    }

    @Test
    public void registerWrongUserId()
    {

    }

    @Test
    public void getAllSorted()
    {

    }

    @Test
    public void getAllPage()
    {

    }

    @Test
    public void getAllEmpty()
    {

    }

    @Test
    public void getMyWithItems()
    {

    }

    @Test
    public void getMyEmpty()
    {

    }

    @Test
    public void getMyWrongUserId()
    {

    }

    @Test
    public void updateAll()
    {

    }

    @Test
    public void updateNothing()
    {

    }

    @Test
    public void updateWrongItemId()
    {

    }

    @Test
    public void deleteWithoutAnything()
    {

    }

    @Test
    public void deleteWithImage()
    {

    }

    @Test
    public void deleteWithRental()
    {

    }

    @Test
    public void deleteWrongItemId()
    {

    }

    @Test
    public void getCorrect()
    {

    }

    @Test
    public void getWrongItemId()
    {

    }
}
