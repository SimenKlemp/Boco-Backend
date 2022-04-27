package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
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
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository userRepository;

    @BeforeEach
    public void setUp() {

        User user = new User("Name",true,"streetAddress", "postalCode", "postOffice", "email", "password", "Admin", null);
        Item item1 = new Item("streetAddress", "postalCode", "postOffice", 200,"Description", "Category", "Title",null, true, true, null, user);

        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);

        Mockito.lenient().when(itemRepository.findAllByUser(Mockito.any())).thenReturn(items);
        Mockito.lenient().when(itemRepository.findAll()).thenReturn(items);
        Mockito.lenient().when(itemRepository.save(Mockito.any())).thenReturn(0);
        Mockito.lenient().when(itemRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(item1));
        //Mockito.lenient().when(userRepository.findById(Mockito.anyLong())).thenReturn(user);
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
