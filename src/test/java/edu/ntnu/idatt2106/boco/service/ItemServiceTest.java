package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;



    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRepository userRepository;

    @BeforeEach
    public void setUp() {

        User user = new User("Name",true,"streetAddress", "postalCode", "postOffice", "email", "password", "Admin", null);
        Item item1 = new Item("streetAddress", "postalCode", "postOffice", 200,"Description", "Category", "Title", null, user, null);

        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);

        Mockito.lenient().when(itemRepository.findAllByUser(Mockito.any())).thenReturn(items);
        Mockito.lenient().when(itemRepository.findAll()).thenReturn(items);
        Mockito.lenient().when(itemRepository.save(Mockito.any())).thenReturn(0);
        Mockito.lenient().when(itemRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(item1));
        //Mockito.lenient().when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

    }


    /*
    @Test
    void createItemTest(){
        RegisterItemRequest item = new RegisterItemRequest("streetAddress", "postalCode", "postOffice", 200,"Description", "Category", "Title", 1L,null);

        ItemResponse response =  itemService.registerItem(item);

        assertThat(response).isNotNull();
    }

     */

    @Test
    void getAllSubjectsTest() {
        List<ItemResponse> items = itemService.getAllItems();

        assertThat(items.get(0).getStreetAddress()).isEqualTo("streetAddress");
        assertThat(items.get(0).getPostalCode()).isEqualTo("postalCode");
        assertThat(items.get(0).getPostOffice()).isEqualTo("postOffice");
        assertThat(items.get(0).getCategory()).isEqualTo("Category");
        assertThat(items.get(0).getDescription()).isEqualTo("Description");
        assertThat(items.get(0).getPrice()).isEqualTo(200);
        assertThat(items.get(0).getImageId()).isEqualTo(-1);
        assertThat(items.get(0).getTitle()).isEqualTo("Title");
        assertThat(items.get(0).getUser().getEmail()).isEqualTo("email");
    }
    @Test
    void getMyItemsTest() {
        List<ItemResponse> items = itemService.getMyItems(Mockito.anyLong());

        assertThat(items.get(0).getStreetAddress()).isEqualTo("streetAddress");
        assertThat(items.get(0).getPostalCode()).isEqualTo("postalCode");
        assertThat(items.get(0).getPostOffice()).isEqualTo("postOffice");
        assertThat(items.get(0).getCategory()).isEqualTo("Category");
        assertThat(items.get(0).getDescription()).isEqualTo("Description");
        assertThat(items.get(0).getPrice()).isEqualTo(200);
        assertThat(items.get(0).getImageId()).isEqualTo(-1);
        assertThat(items.get(0).getTitle()).isEqualTo("Title");
        assertThat(items.get(0).getUser().getEmail()).isEqualTo("email");
    }


    @Test
    void updateSpecificItem(){
        UpdateItemRequest updateItemRequest = new UpdateItemRequest("strretAddress", "postalCode", "postOffice", 200f, "Description", "Category", "Title", 1L, null);
        ItemResponse response = itemService.updateItem(Mockito.anyLong(), updateItemRequest);

        assertThat(response.getPrice()).isEqualTo(200);
        assertThat(response.getDescription()).isEqualTo("Description");
        assertThat(response.getCategory()).isEqualTo("Category");
        assertThat(response.getTitle()).isEqualTo("Title");
    }

    @Test
    void deleteSpecificItem(){
        boolean response = itemService.deleteItem(Mockito.anyLong());
        assertThat(response).isFalse();
    }

}
