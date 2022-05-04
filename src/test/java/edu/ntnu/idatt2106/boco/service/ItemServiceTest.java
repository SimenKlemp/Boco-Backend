package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.ImageFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.ItemFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.RentalFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.UpdateItemRequestFactory;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


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

    Rental rental=new Rental();
    RentalFactory rentalFactory =new RentalFactory();

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
        rental=rentalFactory.getObject();

        assert user != null;
        assert item1 != null;
        assert item2 != null;
        assert image != null;
        assert rental != null;

        item1.getUser().setUserId(user.getUserId());
        item2.getUser().setUserId(user.getUserId());


        userRepository.save(user);
        imageRepository.save(image);
        rentalRepository.save(rental);

        item1.setImage(image);
        item2.setImage(image);

        itemRepository.save(item1);
        itemRepository.save(item2);

        itemList=new ArrayList();
    }

    @AfterEach
    public void cleanUp() {
        itemRepository.deleteAll();
        userRepository.delete(user);
        imageRepository.deleteAll();
    }
    @Test
    public void should_store_item_without_image() throws Exception {

        item1.setImage(null);
        itemRepository.save(item1);
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
                item1.getImage().getImageId());

        assert registerItemRequest1 != null;

        ItemResponse actual =itemService.registerItem(registerItemRequest1);
        Item expected = itemRepository.findById(item1.getItemId()).get();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("imageId")
                .isEqualTo(expected);

        assertThat(actual.getImageId()).isNull();
    }

    @Test
    public void should_store_item_with_image(){

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
        ItemResponse actual =itemService.registerItem(registerItemRequest1);
        Item expected = itemRepository.findById(item1.getItemId()).get();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("itemId","imageId","publicityDate","user")
                .isEqualTo(expected);
        assertThat(actual.getImageId()).isNotNull();

    }

    @Test
    public void should_not_store_item_with_wrong_user_id()
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
            assertThat(itemRepository.getById(item1.getUser().getUserId()))
                    .isNotEqualTo(registerItemRequest1);
    }

    @Test
    public void should_return_all_sorted_items()
    {
        item1.setImage(image);
        itemRepository.save(item1);
        item2.setImage(image);
        itemRepository.save(item2);

        List<ItemResponse>sortedItem =itemService.getAllItems(1,1);
        assertThat(sortedItem.stream().sorted().collect(Collectors.toList()));
    }


    @Test
    public void should_return_all_my_items()
    {
        item1.setUser(user);
        item2.setUser(user);

        itemRepository.save(item1);
        itemRepository.save(item2);

      List<Item> items =new ArrayList();
      items.add(item1);
      items.add(item2);
      List<ItemResponse> myItems= itemService.getMyItems(item1.getUser().getUserId());
      assertThat(myItems.size()).isEqualTo(items.size());

    }

    @Test
    public void should_return_null_with_wrong_userid() throws Exception {
       User newUser=userFactory.getObject();
       userRepository.save(newUser);
        assertThat(itemService.getMyItems(newUser.getUserId()).isEmpty());
    }

    @Test
    public void getMyWrongUserId() throws Exception {
        User user1=new UserFactory().getObject();
        user1.setUserId(4L);
        assertThat(itemService.getMyItems(user1.getUserId())).isNull();
    }

    @Test
    public void should_update_item_with_wrong_itemId() throws Exception {
        UpdateItemRequest updateItemRequest=new UpdateItemRequestFactory().getObject();
        ItemResponse updated=itemService.updateItem(4L,updateItemRequest);
        assertThat(updated).isNull();
    }
    @Test
    public void should_update_all_items() throws Exception {
        UpdateItemRequest updateItemRequest=new UpdateItemRequestFactory().getObject();
        item1.setUser(user);
        item1.setImage(image);
        itemRepository.save(item1);

        Item actual=itemRepository.findById(item1.getItemId()).get();

        ItemResponse updated=itemService.updateItem(item1.getItemId(),updateItemRequest);
        assertThat(updated)
                .usingRecursiveComparison()
                .ignoringFields("itemId")
                .isNotEqualTo(actual);

    }

    @Test
    public void should_not_update_items()
    {
        UpdateItemRequest updateItemRequest=new UpdateItemRequest(
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
              item1.getImage().getImageId());

        Item actual=itemRepository.findById(item1.getItemId()).get();
        ItemResponse updated=itemService.updateItem(item1.getItemId(),updateItemRequest);


        assertThat(actual.getStreetAddress()).isEqualTo(updated.getStreetAddress()).isEqualTo(updateItemRequest.getStreetAddress());
        assertThat(actual.getPostalCode()).isEqualTo(updated.getPostalCode()).isEqualTo(updateItemRequest.getPostalCode());
        assertThat(actual.getPostOffice()).isEqualTo(updated.getPostOffice()).isEqualTo(updateItemRequest.getPostOffice());
        assertThat(actual.getPrice()).isEqualTo(updated.getPrice()).isEqualTo(updateItemRequest.getPrice());
        assertThat(actual.getDescription()).isEqualTo(updated.getDescription()).isEqualTo(updateItemRequest.getDescription());
        assertThat(actual.getCategory()).isEqualTo(updated.getCategory()).isEqualTo(updateItemRequest.getCategory());
        assertThat(actual.getIsPickupable()).isEqualTo(updated.getIsPickupable()).isEqualTo(updateItemRequest.getIsPickupable());
        assertThat(actual.getIsDeliverable()).isEqualTo(updated.getIsDeliverable()).isEqualTo(updateItemRequest.getIsDeliverable());

    }
    @Test
    public void should_not_update_item_with_wrong_itemId() throws Exception {
        UpdateItemRequest updateItemRequest =new UpdateItemRequestFactory().getObject();

        Item expected=itemRepository.findById(item1.getItemId()).get();
        ItemResponse actual=itemService.updateItem(item2.getItemId(),updateItemRequest);

        assertThat(item2).isNotEqualTo(item1);
        assertThat(actual.getItemId()).isNotEqualTo(expected.getItemId());
    }

    @Test
    public void delete_item_without_is_not_found()
    {
        Boolean delete=itemService.deleteItem(4L);
        assertThat(delete).isFalse();
    }

    @Test
    public void delete_item_with_image()
    {
        item1.setImage(image);
        itemRepository.save(item1);
        assertThat(itemService.deleteItem(item1.getItemId())).isTrue();

    }

    @Test
    public void delete_item_with_rental() throws Exception {

        rental.setItem(item1);
        rental.setUser(user);
        assert rental != null;
        rentalRepository.save(rental);
        assertThat( itemService.deleteItem(item1.getItemId())).isEqualTo(true);
    }

    @Test
    public void should_return_false_with_wrong_itemId()
    {
        Item newItem=new Item();
        newItem.setItemId(3L);
        boolean wrongItemId= itemService.deleteItem(newItem.getItemId());
        assertThat(wrongItemId).isEqualTo(false);
    }

    @Test
    public void should_return_item_with_correct_itemId()
    {
        itemRepository.save(item1);
        Item item=itemRepository.findById(item1.getItemId()).get();
        itemResponse1=itemService.getItem(item.getItemId());
        assertThat(itemResponse1.getItemId()).isEqualTo(item.getItemId());

    }

    @Test
    public void should_not_return_item_with_wrong_itemid()
    {

        ItemResponse item=itemService.getItem(3L);
        assertThat(item).isNull();
    }
}
