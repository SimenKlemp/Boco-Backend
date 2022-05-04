package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class ItemServiceTest
{
    @Autowired
    private ItemService itemService;

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
        for (Item item : itemRepository.findAll())
        {
            itemService.delete(item.getItemId());
        }
    }

    @Test
    public void registerWithoutImage() throws Exception
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        RegisterItemRequest request = RequestFactory.getRegisterItemRequest(user.getUserId(), null);

        ItemResponse response = itemService.register(request);

        Item item = itemRepository.findById(response.getItemId()).orElseThrow();
        assertThat(item.getItemId()).isEqualTo(response.getItemId());
        assertThat(item.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(item.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(item.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(item.getPrice()).isEqualTo(response.getPrice()).isEqualTo(request.getPrice());
        assertThat(item.getDescription()).isEqualTo(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(item.getCategory()).isEqualTo(response.getCategory()).isEqualTo(request.getCategory());
        assertThat(item.getTitle()).isEqualTo(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(item.getIsPickupable()).isEqualTo(response.getIsPickupable()).isEqualTo(request.getIsPickupable());
        assertThat(item.getUser().getUserId()).isEqualTo(response.getUser().getUserId()).isEqualTo(request.getUserId());
    }

    @Test
    public void registerWithImage() throws Exception
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Image image = new Image("name", null);
        image = imageRepository.save(image);

        RegisterItemRequest request = RequestFactory.getRegisterItemRequest(user.getUserId(), image.getImageId());

        ItemResponse response = itemService.register(request);

        Item item = itemRepository.findById(response.getItemId()).orElseThrow();
        assertThat(item.getItemId()).isEqualTo(response.getItemId());
        assertThat(item.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(item.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(item.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(item.getPrice()).isEqualTo(response.getPrice()).isEqualTo(request.getPrice());
        assertThat(item.getDescription()).isEqualTo(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(item.getCategory()).isEqualTo(response.getCategory()).isEqualTo(request.getCategory());
        assertThat(item.getTitle()).isEqualTo(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(item.getIsPickupable()).isEqualTo(response.getIsPickupable()).isEqualTo(request.getIsPickupable());
        assertThat(item.getUser().getUserId()).isEqualTo(response.getUser().getUserId()).isEqualTo(request.getUserId());
        assertThat(item.getImage().getImageId()).isEqualTo(response.getImageId()).isEqualTo(request.getImageId());
    }

    @Test
    public void registerWrongUserId() throws Exception
    {
        RegisterItemRequest request = RequestFactory.getRegisterItemRequest(0L, null);

        ItemResponse response = itemService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void getAllWithItems()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item1 = ModelFactory.getItem(null, user);
        item1 = itemRepository.save(item1);

        Item item2 = ModelFactory.getItem(null, user);
        item2 = itemRepository.save(item2);

        Item[] items = {item1, item2};

        List<ItemResponse> responses = itemService.getAll(0, Integer.MAX_VALUE);

        assertThat(items.length).isEqualTo(responses.size());
        for (int i = 0; i < items.length; i++)
        {
            Item item = items[i];
            ItemResponse response = responses.get(i);

            assertThat(item.getStreetAddress()).isEqualTo(response.getStreetAddress());
            assertThat(item.getPostalCode()).isEqualTo(response.getPostalCode());
            assertThat(item.getPostOffice()).isEqualTo(response.getPostOffice());
            assertThat(item.getPrice()).isEqualTo(response.getPrice());
            assertThat(item.getDescription()).isEqualTo(response.getDescription());
            assertThat(item.getCategory()).isEqualTo(response.getCategory());
            assertThat(item.getTitle()).isEqualTo(response.getTitle());
            assertThat(item.getIsPickupable()).isEqualTo(response.getIsPickupable());
            assertThat(item.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        }
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
