package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.factories.requestFactroies.RegisterItemRequestFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.UpdateItemRequestFactory;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.ResponseFactories;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.UserResponseFactory;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.service.ItemService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;


import static edu.ntnu.idatt2106.boco.controllers.RentalControllerTest.asJsonString;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ItemController.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc(addFilters = false)

class ItemControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    ItemService itemService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    private TokenComponent tokenComponent;



    private UserResponseFactory userFactory=new UserResponseFactory();
    private UserResponse user=new UserResponse();
    private List<ItemResponse> itemList=new ArrayList<>();
    private RegisterItemRequest itemRequest1=new RegisterItemRequest();
    private RegisterItemRequest itemRequest2=new RegisterItemRequest();
    private RegisterItemRequestFactory request1=new RegisterItemRequestFactory();
    private RegisterItemRequestFactory request2=new RegisterItemRequestFactory();
    private ItemResponse itemResponse1=new ItemResponse();
    private ItemResponse itemResponse2=new ItemResponse();
    private UpdateItemRequestFactory updateItemRequest=new UpdateItemRequestFactory();
    private ItemResponse updateItemResponse=new ItemResponse();
    private UpdateItemRequest updateRequest=new UpdateItemRequest();
    private Optional<User> newUser;
    private ResponseFactories responseFactories=new ResponseFactories();

    @BeforeEach
    void setUp() throws Exception {

        this.mockMvc= MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        user=userFactory.getObject();
        user.setUserId(1L);
        assert user != null;

        itemRequest1 =request1.getObject();
        itemRequest1.setUserId(user.getUserId());
        itemRequest1.setUserId(user.getUserId());
        assert itemRequest1 !=null;

        itemRequest2=request2.getObject();
        itemRequest1.setUserId(user.getUserId());
        itemRequest2.setUserId(user.getUserId());
        assert itemRequest2 != null;

        itemResponse1=responseFactories.itemResponseFactory(itemRequest1,user);
        itemResponse1.getUser().setUserId(itemRequest1.getUserId());
        assert itemResponse1 != null;

        itemResponse1=responseFactories.itemResponseFactory(itemRequest1,user);
        itemResponse1.getUser().setUserId(itemRequest1.getUserId());
        assert itemResponse1 != null;

        updateRequest=updateItemRequest.getObject();

        itemList=new ArrayList<>();
        itemList.add(itemResponse1);
        itemList.add(itemResponse2);

        assert itemList != null;
    }

    @AfterEach
    void cleanUp(){
        itemResponse1=null;
        itemResponse2=null;
        itemList=null;
        itemRepository.deleteAll();
    }

    @Test
    void registerItemWithoutAuthenticationAndThenReturn403() throws Exception {
        when(itemService.registerItem(any())).thenReturn(itemResponse1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/item/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemRequest1))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void registerItemWithAuthenticationAndThenReturnItemResponse() throws Exception {
        when(itemService.registerItem(any())).thenReturn(itemResponse1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/item/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemRequest1))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void GetAllItemsBySendPageAndPageSizeThenReturnItemList() throws Exception {
        int page=1;
        int pageSize=2;

        when(itemService.getAllItems(page,pageSize)).thenReturn(itemList);
        mockMvc.perform(get("/item/all/{page}/{pageSize}", page, pageSize)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemList)))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.size()").value(itemList.size()))
                .andDo(print());
    }

    @Test
    void getMyItemsBySendingUserIdAndTheReturnUserItemList() throws Exception {
        long userId=user.getUserId();
            when(itemService.getMyItems(userId)).thenReturn(itemList);
            mockMvc.perform(get("/get-my/{userId}",userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(itemList))
                            .with(csrf())
                    )
                    .andExpect((ResultMatcher) jsonPath("$.length()",is(2)))
                    .andDo(print())
                    .andExpect(status().isOk());

    }

    @Test
    void updateItemBySendingUserIdAndUpdateRequestAndThenReturnUpdateResponseItem() throws Exception {
        when(itemService.updateItem(user.getUserId(),updateRequest)).thenReturn(updateItemResponse);
        mockMvc.perform(put("/item/update/{itemId}",itemResponse1.getItemId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemResponse1))
                        .with(csrf()))
                .andExpect( jsonPath("$.category").value(itemResponse1.getCategory()))
                .andExpect( jsonPath("$.isDeliverable").value(itemResponse1.getIsDeliverable()))
                .andExpect( jsonPath("$.title").value(itemResponse1.getTitle()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    void deleteItemBySendingItemIdAndReturnTrue() throws Exception {
        Long itemId= user.getUserId();
        when(itemService.deleteItem(itemId)).thenReturn(true);
        mockMvc.perform(delete("/item/delete/{itemId}",itemId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getAllSearchedItems() throws Exception {

        when(itemService.search(any())).thenReturn(itemList);
        mockMvc.perform(get("search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(itemList.size()))
                        .andDo(print());
    }
}