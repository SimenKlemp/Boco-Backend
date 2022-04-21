package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
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
import java.util.Optional;

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

        User user = new User("Name",true,"Address", "email", "password", "Admin");

        Item item1 = new Item("Address", 200,"Description", "Category", "Title", user,1l);

        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);

        Mockito.lenient().when(itemRepository.findAllByUser(Mockito.any())).thenReturn(items);
        Mockito.lenient().when(itemRepository.findAll()).thenReturn(items);
        Mockito.lenient().when(itemRepository.save(Mockito.any())).thenReturn(0);
        Mockito.lenient().when(itemRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(item1));
        //Mockito.lenient().when(userRepository.findById(Mockito.anyLong())).thenReturn(user);

    }


    @Test
    void createItemTest(){
        ItemRegisterRequest item = new ItemRegisterRequest("Address", 200,"Description", "Category", "Title", 1L,1l);

        int response =  itemService.createItem(item);

        assertThat(response).isEqualTo(0);
    }

    @Test
    void getAllSubjectsTest() {
        List<Item> items = itemService.getAllItems();

        assertThat(items.get(0).getAddress()).isEqualTo("Address");
        assertThat(items.get(0).getCategory()).isEqualTo("Category");
        assertThat(items.get(0).getDescription()).isEqualTo("Description");
        assertThat(items.get(0).getPrice()).isEqualTo(200);
        assertThat(items.get(0).getImageid()).isEqualTo(1);
        assertThat(items.get(0).getTitle()).isEqualTo("Title");
        assertThat(items.get(0).getUser().getEmail()).isEqualTo("email");
    }
    @Test
    void getMyItemsTest() {
        List<Item> items = itemService.getMyItems(Mockito.anyLong());

        assertThat(items.get(0).getAddress()).isEqualTo("Address");
        assertThat(items.get(0).getCategory()).isEqualTo("Category");
        assertThat(items.get(0).getDescription()).isEqualTo("Description");
        assertThat(items.get(0).getPrice()).isEqualTo(200);
        assertThat(items.get(0).getImageid()).isEqualTo(1);
        assertThat(items.get(0).getTitle()).isEqualTo("Title");
        assertThat(items.get(0).getUser().getEmail()).isEqualTo("email");
    }


    @Test
    void updateSpecificItem(){
        ItemRegisterRequest itemRegisterRequest = new ItemRegisterRequest("Address", 200, "Description", "Category", "Title", 1L, 1L);
        Item response = itemService.updateSpecificItem(Mockito.anyLong(), itemRegisterRequest);

        assertThat(response.getAddress()).isEqualTo("Address");
        assertThat(response.getPrice()).isEqualTo(200);
        assertThat(response.getDescription()).isEqualTo("Description");
        assertThat(response.getCategory()).isEqualTo("Category");
        assertThat(response.getTitle()).isEqualTo("Title");
    }

    @Test
    void deleteSpecificItem(){
        int response = itemService.deleteSpecificItem(Mockito.anyLong());

        assertThat(response).isEqualTo(0);
    }



}
