package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.ItemFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertFalse;


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

    private UserFactory userFactory=new UserFactory();
    private ImageFactory imageFactory=new ImageFactory();
    private ItemFactory itemFactory1 =new ItemFactory();
    private ItemFactory itemFactory2 =new ItemFactory();

    private User user=new User();
    private Item item1 =new Item();
    private Item item2 =new Item();
    private Image image=new Image();

    private RegisterItemRequest registerItemRequest1 =new RegisterItemRequest();
    private RegisterItemRequest registerItemRequest2=new RegisterItemRequest();
    private ItemResponse itemResponse1 =new ItemResponse();
    private ItemResponse itemResponse2 =new ItemResponse();
    private List<ItemResponse> itemList;

    @BeforeEach
    public void beforeEach() throws Exception {
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockImageRepository(imageRepository);
        RepositoryMock.mockItemRepository(itemRepository);
        RepositoryMock.mockRentalRepository(rentalRepository);

        user=userFactory.getObject();
        item1 = itemFactory1.getObject();
        item2=itemFactory2.getObject();
        image=imageFactory.getObject();

        assert user != null;
        assert item1 != null;
        assert item2 != null;
        assert image != null;

        item1.getUser().setUserId(user.getUserId());
        itemList=new ArrayList();

    }


    @AfterEach
    public void cleanUp() {
        itemRepository.delete(item1);
        userRepository.delete(user);
        imageRepository.delete(image);
    }
    @Test
    public void registerWithoutImage() throws Exception {

        registerItemRequest1 =new RegisterItemRequest(
                item1.getStreetAddress(),
                item1.getPostalCode(),
                item1.getPostOffice(),
                item1.getPrice(),
                item1.getDescription(),
                item1.getCategory(),
                item1.getTitle(),
                item1.getIsPickupable(),
                item1.getIsDeliverable(),
                item1.getUser().getUserId(),
                null
        );
        assert registerItemRequest1 != null;
        item1.setImage(null);
        itemRepository.save(item1);

        itemResponse1 =itemService.registerItem(registerItemRequest1);
        System.out.println(itemResponse1);
        assertThat(itemResponse1.getImageId()).isEqualTo(item1.getImage().getImageId()).isEqualTo(registerItemRequest1.getImageId()).isEqualTo(null);

    }

    @Test
    public void registerWithImage(){

            registerItemRequest1 = new RegisterItemRequest(
                    item1.getStreetAddress(),
                    item1.getPostalCode(),
                    item1.getPostOffice(),
                    item1.getPrice(),
                    item1.getDescription(),
                    item1.getCategory(),
                    item1.getTitle(),
                    item1.getIsPickupable(),
                    item1.getIsDeliverable(),
                    user.getUserId(),
                    image.getImageId()
            );
            item1.setImage(image);
            itemRepository.save(item1);
            assertThat(itemRepository.getById(item1.getUser().getUserId()).getImage()).isEqualTo(registerItemRequest1.getImageId());

    }

    @Test
    public void registerWrongUserId()
    {
            registerItemRequest1 =new RegisterItemRequest(
                    item1.getStreetAddress(),
                    item1.getPostalCode(),
                    item1.getPostOffice(),
                    item1.getPrice(),
                    item1.getDescription(),
                    item1.getCategory(),
                    item1.getTitle(),
                    item1.getIsPickupable(),
                    item1.getIsDeliverable(),
                    1l,
                    null
            );
            assertThat(itemRepository.getById(item1.getUser().getUserId())).isNotEqualTo(registerItemRequest1);
    }

    @Test
    public void getAllSorted()
    {
        item1.setImage(image);
        itemRepository.save(item1);
        registerItemRequest1 = new RegisterItemRequest(
                item1.getStreetAddress(),
                item1.getPostalCode(),
                item1.getPostOffice(),
                item1.getPrice(),
                item1.getDescription(),
                item1.getCategory(),
                item1.getTitle(),
                item1.getIsPickupable(),
                item1.getIsDeliverable(),
                user.getUserId(),
                image.getImageId()
        );
        registerItemRequest1 = new RegisterItemRequest(
                item1.getStreetAddress(),
                item1.getPostalCode(),
                item1.getPostOffice(),
                item1.getPrice(),
                item1.getDescription(),
                item1.getCategory(),
                item1.getTitle(),
                item1.getIsPickupable(),
                item1.getIsDeliverable(),
                user.getUserId(),
                image.getImageId()
        );

        itemResponse1 =itemService.registerItem(registerItemRequest1);
        itemResponse2 =itemService.registerItem(registerItemRequest2);

        itemList.add(itemResponse1);
        itemList.add(itemResponse2);

        List<ItemResponse>sortedItem =itemService.getAllItems(1,2);
        assertThat(sortedItem.stream().sorted()).isEqualTo(true);
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
        registerItemRequest1 = new RegisterItemRequest(
                item1.getStreetAddress(),
                item1.getPostalCode(),
                item1.getPostOffice(),
                item1.getPrice(),
                item1.getDescription(),
                item1.getCategory(),
                item1.getTitle(),
                item1.getIsPickupable(),
                item1.getIsDeliverable(),
                user.getUserId(),
                image.getImageId()
        );
        registerItemRequest1 = new RegisterItemRequest(
                item1.getStreetAddress(),
                item1.getPostalCode(),
                item1.getPostOffice(),
                item1.getPrice(),
                item1.getDescription(),
                item1.getCategory(),
                item1.getTitle(),
                item1.getIsPickupable(),
                item1.getIsDeliverable(),
                user.getUserId(),
                image.getImageId()
        );
        item1.getUser().setUserId(user.getUserId());
        item2.getUser().setUserId(user.getUserId());

        itemResponse1=itemService.registerItem(registerItemRequest1);
        itemResponse2=itemService.registerItem(registerItemRequest2);
        itemList.add(itemResponse1);
        itemList.add(itemResponse2);

      List<ItemResponse> myItems= itemService.getMyItems(item1.getUser().getUserId());
      assertThat(myItems.size()).isEqualTo(itemList.size());

    }

    @Test
    public void getMyEmpty() throws Exception {
        User user1=new UserFactory().getObject();
        List<ItemResponse> myItems= itemService.getMyItems(user1.getUserId());
        assertThat(myItems.size()).isZero();
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
        itemRepository.save(item1);
        Item item=itemRepository.getById(item1.getItemId());
        itemResponse1=itemService.getItem(item.getItemId());
        assertThat(itemResponse1.getItemId()).isEqualTo(item.getItemId());

    }

    @Test
    public void getWrongItemId()
    {
        itemRepository.save(item1);
        Item item=itemRepository.getById(item1.getItemId());
        Item actualItem=new Item();
        actualItem.setItemId(2L);
        itemResponse1=itemService.getItem(item.getItemId());

        assertThat(itemRepository.findById(actualItem.getItemId())).isEqualTo(false);
    }
}
