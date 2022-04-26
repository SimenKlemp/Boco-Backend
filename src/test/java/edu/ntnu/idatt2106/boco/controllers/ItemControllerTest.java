package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static edu.ntnu.idatt2106.boco.controllers.RentalControllerTest.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest {

    @Mock
    ItemRepository itemRepository;

    private Item item1;
    private Item item2;
    private List<Item> itemList;
    private User user;
    private RegisterItemRequest request1;
    private UpdateItemRequest updateRequest;

    @Autowired
    private TokenComponent tokenComponent;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        user =new User("name",true,"address"
                ,"email","password","role",
                new Image());
        user.setUserId(1L);
        tokenComponent.generateToken(user.getUserId(),"USER");

        item1=new Item("streetAddress1",
                "postalCode1","postOffice1",1f,"description1"
                ,"category1","title1",new Image(),user,new Date());

        item2=new Item("streetAddress2",
                "postalCode2","postOffice2",1f,"description2"
                ,"category2","title1",new Image(),user,new Date());

        request1=new RegisterItemRequest(item1.getStreetAddress()
                ,item1.getPostalCode(),item1.getPostOffice(),item1.getPrice()
                ,item1.getDescription(),item1.getCategory(),item1.getTitle(),item1
                .getUser().getUserId(), item1.getItemId());

        updateRequest=new UpdateItemRequest("Updated"
                ,"updated","Updated",2f
                ,"Updated","Updated","Updated",item1
                .getUser().getUserId(), item1.getItemId());

        itemList.add(item1);
        itemList.add(item2);
    }

    @AfterEach
    void cleanUp(){
        item1=item2=null;
        itemList=null;
        itemRepository.deleteAll();


    }

    @Test
    void registerItem() throws Exception {
        mockMvc.perform(post("item/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void getAllItems() throws Exception {

            when(itemRepository.findAll()).thenReturn(itemList);
            mockMvc.perform(get("item/all"))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) jsonPath("$.size()").value(itemList.size()))
                    .andDo(print());
    }

    @Test
    void getMyItems() throws Exception {
        for(Item item: itemList) {
            Long userId = item.getUser().getUserId();
            when(itemRepository.findById(userId)).thenReturn(Optional.of(item));
            mockMvc.perform(get("/get-my/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(item1)))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) jsonPath("$.itemId").value(item.getItemId()))
                            .andExpect((ResultMatcher) jsonPath("$.streetAddress").value(item.getStreetAddress()))
                    .andExpect((ResultMatcher) jsonPath("$.postalCode").value(item.getPostalCode()))
                    .andExpect((ResultMatcher) jsonPath("$.postOffice").value(item.getPostOffice()))
                    .andExpect((ResultMatcher) jsonPath("$.price").value(item.getPrice()))
                    .andExpect((ResultMatcher) jsonPath("$.description").value(item.getDescription()))
                    .andExpect((ResultMatcher) jsonPath("$.category").value(item.getCategory()))
                    .andExpect((ResultMatcher) jsonPath("$.title").value(item.getTitle()))
                    .andExpect((ResultMatcher) jsonPath("$.imageId").value(item.getImage()))
                    .andExpect((ResultMatcher) jsonPath("$.userId").value(item.getUser()))
                    .andDo(print());
        }
        
    }

    @Test
    void updateItem() {
    }

    @Test
    void deleteItem() {
    }

    @Test
    void getAllSearchedItems() {
    }
}