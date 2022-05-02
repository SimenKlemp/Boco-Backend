package edu.ntnu.idatt2106.boco.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.ResponseItem;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.RegisterRentalRequestFactory;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.ItemResponseFactory;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.RentalResponseFactory;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.UserResponseFactory;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(RentalController.class)
@WebMvcTest(RentalController.class)
class RentalControllerTest {

    @MockBean
    private RentalRepository rentalRepository;

    @MockBean
    private RentalService rentalService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired

    protected WebApplicationContext wac;


    private UserFactory user;
    private UserResponseFactory userResponse;
    private ItemResponseFactory item;
    private RegisterRentalRequestFactory request1;
    private RegisterRentalRequestFactory request2;
    private RentalResponseFactory updatedRental;
    private RentalResponseFactory rentalResponse;
    private List<RentalResponse> rentalList;



    @InjectMocks
    private TokenComponent tokenComponent;

    @BeforeEach
    public void setUp() throws Exception {
       user=new UserFactory();
        tokenComponent.generateToken(user.getObject().getUserId(),"USER");

        userResponse=new UserResponseFactory();

        item=new ItemResponseFactory();

        request1=new RegisterRentalRequestFactory();



        rentalResponse=new RentalResponseFactory();


        rentalList=new ArrayList<>();
        rentalList.add(rentalResponse.getObject());
    }

    @AfterEach
    void tearDown(){
        rentalList=null;
        rentalRepository.deleteAll();
    }


    @Test
    void registerRentalTest() throws Exception {
        /**
        Mockito.when(rentalService.registerRental(any())).thenReturn(rentalResponse);
        mockMvc.perform(post("/rental/register").
                contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentalResponse))).
                andExpect(status().isCreated());

        verify(rentalService,times(1)).registerRental(any());


        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Mockito.when(rentalService.registerRental(Mockito.any())).thenReturn(rentalResponse);

        ResponseEntity<RentalResponse> responseEntity=rentalController.registerRental(request1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);

        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("user/rental/register");
   */
        mockMvc.perform(post("rental/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
/**
    @Test
    void acceptRental() throws Exception {
        long rentalId = rentalResponse.getObject().getRentalId();
        when(rentalRepository.findById(rentalId)).thenReturn();
        updatedRental=new RentalResponseFactory();
        updatedRental.getObject().setRentalId(rentalResponse.getObject().getRentalId());
        when(rentalRepository.save(any(Rental.class))).thenReturn(updatedRental.getObject());
        mockMvc.perform(put("rental/accept/{rentalId}",rentalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRental.getObject())))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.status").value(rental1.getStatus()))
                .andDo(print());
    }

    @Test
    void rejectRental() throws Exception {

        long rentalId = rental1.getRentalId();
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental1));
        rental1.setStatus(Rental.Status.PENDING);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental1);
        mockMvc.perform(put("rental/reject/{rentalId}",rentalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rental1)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.status").value(rental1.getStatus()))
                .andDo(print());

    }
    */
/**
    @Test
    void cancelRentalTest() throws Exception {
        /**
        Mockito.when(rentalService.cancelRental(rental.getRentalId()))
                .thenReturn(rentalResponse);
        mockMvc.perform(delete("rental/cancel/{1}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(rentalResponse)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
         */
/**
        long rentalId = rental1.getRentalId();
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental1));
        rental1.setStatus(Rental.Status.ACCEPTED);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental1);
        mockMvc.perform(put("rental/cancel/{rentalId}",rentalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(rental1)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.status").value(rental1.getStatus()))
                .andDo(print());
        
    }
*/
/**
    @Test
    void getAllRentalsForItemTest() throws Exception {
        /**
        Mockito.when(rentalService.getAllRentalsForItem(rental.getRentalId())).thenReturn(rentalList);
        mockMvc.perform(get("/rental/for-item/{1}")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(rentalResponse)))
                .andDo(print());

        verify(rentalService).getAllRentalsForItem(rentalResponse.getRentalId());
        verify(rentalService,times(1)).getAllRentalsForItem(rentalResponse.getRentalId());
         */
/**
        for(Rental rental:rentalList) {
            Item item = rental.getItem();
            long id=rental.getItem().getItemId();
            when(rentalRepository.findAllByItem(item)).thenReturn(rentalList);
            mockMvc.perform(get("rental/for-item/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect((ResultMatcher) jsonPath("$.size()").value(rentalList.size()))
                    .andDo(print());
        }
    }
 */
/**
    @Test
    void NoFoundRentalForItemTest() throws Exception {

        for(Rental rental1 : rentalList) {
            Item item=rental1.getItem();
            long id= rental1.getItem().getItemId();
            Mockito.when(rentalRepository.findAllByItem(item)).thenReturn(rentalList);
            rentalList=Collections.emptyList();
            mockMvc.perform(get("rental/for-item/{id}", id))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }
    }

    @Test
    void getAllRentalsUserTest() throws Exception {

        for(Rental rental: rentalList){
            long userId=rental.getUser().getUserId();
            User user =rental.getUser();
            Mockito.when(rentalRepository.findAllByUser(user)).thenReturn(rentalList);

            mockMvc.perform(get("rental/get-my/{userId}",userId))
                    .andExpect((ResultMatcher) jsonPath("$.size()").value(rentalList.size()))
                    .andDo(print());
        }
    }

    @Test
    void getNoRentalsByUserTest() throws Exception {

        for(Rental rental: rentalList){
            long userId=rental.getUser().getUserId();
            User user =rental.getUser();
            Mockito.when(rentalRepository.findAllByUser(user)).thenReturn(rentalList);

            mockMvc.perform(get("rental/get-my/{userId}",userId))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }
    }

*/
    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


}
