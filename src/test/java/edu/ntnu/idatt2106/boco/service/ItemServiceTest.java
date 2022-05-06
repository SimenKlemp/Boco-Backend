package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.SearchRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @BeforeEach
    public void beforeEach()
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
        List<ItemResponse> responses = itemService.getAll(0, Integer.MAX_VALUE);
        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getMyWithItems()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item1 = ModelFactory.getItem(null, user);
        item1 = itemRepository.save(item1);

        Item item2 = ModelFactory.getItem(null, user);
        item2 = itemRepository.save(item2);

        Item[] items = {item1, item2};

        List<ItemResponse> responses = itemService.getAllMy(user.getUserId());

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
    public void getMyEmpty()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        List<ItemResponse> responses = itemService.getAllMy(user.getUserId());
        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getMyWrongUserId()
    {
        List<ItemResponse> responses = itemService.getAllMy(0L);
        assertThat(responses).isNull();
    }

    @Test
    public void updateAll() throws Exception
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Image image1 = ModelFactory.getImage();
        image1 = imageRepository.save(image1);

        Item item = ModelFactory.getItem(image1, user);
        item = itemRepository.save(item);

        Image image2 = ModelFactory.getImage();
        image2 = imageRepository.save(image2);

        UpdateItemRequest request = RequestFactory.getUpdateItemRequest(image2.getImageId());

        ItemResponse response = itemService.update(item.getItemId(), request);
        item = itemRepository.findById(item.getItemId()).orElseThrow();

        assertThat(item.getStreetAddress()).isEqualTo(response.getStreetAddress()).isEqualTo(request.getStreetAddress());
        assertThat(item.getPostalCode()).isEqualTo(response.getPostalCode()).isEqualTo(request.getPostalCode());
        assertThat(item.getPostOffice()).isEqualTo(response.getPostOffice()).isEqualTo(request.getPostOffice());
        assertThat(item.getPrice()).isEqualTo(response.getPrice()).isEqualTo(request.getPrice());
        assertThat(item.getDescription()).isEqualTo(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(item.getCategory()).isEqualTo(response.getCategory()).isEqualTo(request.getCategory());
        assertThat(item.getTitle()).isEqualTo(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(item.getIsPickupable()).isEqualTo(response.getIsPickupable()).isEqualTo(request.getIsPickupable());
        assertThat(item.getIsDeliverable()).isEqualTo(response.getIsDeliverable()).isEqualTo(request.getIsDeliverable());
        assertThat(item.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
    }

    @Test
    public void updateNothing() throws Exception
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Image image1 = ModelFactory.getImage();
        image1 = imageRepository.save(image1);

        Item item = ModelFactory.getItem(image1, user);
        item = itemRepository.save(item);

        Image image2 = ModelFactory.getImage();
        image2 = imageRepository.save(image2);

        UpdateItemRequest request = new UpdateItemRequest();

        ItemResponse response = itemService.update(item.getItemId(), request);
        item = itemRepository.findById(item.getItemId()).orElseThrow();

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

    @Test
    public void updateWrongItemId() throws Exception
    {
        UpdateItemRequest request = RequestFactory.getUpdateItemRequest(null);

        ItemResponse response = itemService.update(0L, request);
        assertThat(response).isNull();
    }

    @Test
    public void searchCorrect()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item1 = ModelFactory.getItem(null, user);
        item1 = itemRepository.save(item1);

        Item item2 = ModelFactory.getItem(null, user);
        item2 = itemRepository.save(item2);

        Item[] items = {item1, item2};

        SearchRequest request = RequestFactory.getSearchRequest();

        List<ItemResponse> responses = itemService.search(request);

        assertThat(items.length).isEqualTo(responses.size());
    }

    @Test
    public void getCorrect()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        ItemResponse response = itemService.getItem(item.getItemId());

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

    @Test
    public void getWrongItemId()
    {
        ItemResponse response = itemService.getItem(0L);
        assertThat(response).isNull();
    }

    @Test
    public void getAllOccupiedDatesForItemWithRental()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        LocalDate today = new Date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Rental rental1 = ModelFactory.getRental(user2, item);
        rental1.setStartDate(
                Date.from(today.minusDays(10).atStartOfDay(ZoneOffset.systemDefault()).toInstant())
        );
        rental1.setEndDate(
                Date.from(today.plusDays(10).atStartOfDay(ZoneOffset.systemDefault()).toInstant())
        );
        rental1 = rentalRepository.save(rental1);

        Rental rental2 = ModelFactory.getRental(user2, item);
        rental2.setStartDate(
                Date.from(today.plusDays(15).atStartOfDay(ZoneOffset.systemDefault()).toInstant())
        );
        rental2.setEndDate(
                Date.from(today.plusDays(15).atStartOfDay(ZoneOffset.systemDefault()).toInstant())
        );
        rental2 = rentalRepository.save(rental2);

        List<LocalDate> occupiedDates = itemService.getAllOccupiedDatesForItem(item.getItemId());

        assertThat(occupiedDates.size()).isEqualTo(12);
        for (int i = 0; i <= 10; i++)
        {
            assertThat(occupiedDates.get(i)).isEqualTo(today.plusDays(i));
        }

        assertThat(occupiedDates.get(11)).isEqualTo(today.plusDays(15));
    }

    @Test
    public void getAllOccupiedDatesForItemEmpty()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        List<LocalDate> occupiedDates = itemService.getAllOccupiedDatesForItem(item.getItemId());

        assertThat(occupiedDates.isEmpty()).isTrue();
    }

    @Test
    public void getAllOccupiedDatesForItemWrongItemId()
    {
        List<LocalDate> occupiedDates = itemService.getAllOccupiedDatesForItem(0L);
        assertThat(occupiedDates).isNull();
    }

    @Test
    public void deleteWithoutAnything()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        boolean success = itemService.delete(item.getItemId());
        Optional<Item> optionalItem = itemRepository.findById(item.getItemId());

        assertThat(success).isTrue();
        assertThat(optionalItem.isEmpty()).isTrue();
    }

    @Test
    public void deleteWithImage()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Image image = ModelFactory.getImage();
        image = imageRepository.save(image);

        Item item = ModelFactory.getItem(image, user);
        item = itemRepository.save(item);

        boolean success = itemService.delete(item.getItemId());
        Optional<Item> optionalItem = itemRepository.findById(item.getItemId());
        Optional<Image> optionalImage = imageRepository.findById(image.getImageId());

        assertThat(success).isTrue();
        assertThat(optionalItem.isEmpty()).isTrue();
        assertThat(optionalImage.isEmpty()).isTrue();
    }

    @Test
    public void deleteWithRental()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        boolean success = itemService.delete(item.getItemId());
        Optional<Item> optionalItem = itemRepository.findById(item.getItemId());
        Optional<Rental> optionalRental = rentalRepository.findById(rental.getRentalId());

        assertThat(success).isTrue();
        assertThat(optionalItem.isEmpty()).isTrue();
        assertThat(optionalRental.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongItemId()
    {
        boolean success = itemService.delete(0L);
        assertThat(success).isFalse();
    }
}
