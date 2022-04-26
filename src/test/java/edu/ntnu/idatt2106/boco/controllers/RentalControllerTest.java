package edu.ntnu.idatt2106.boco.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.ResponseItem;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RentalControllerTest {

    @Mock
    private RentalService rentalService;
    private Rental rental;
    private User user;
    private UserResponse userResponse;
    private ItemResponse item;
    private RegisterRentalRequest request1;
    private RentalResponse rentalResponse;
    private List<RentalResponse> rentalList;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private RentalController rentalController;

    @InjectMocks
    private TokenComponent tokenComponent;

    @BeforeEach
    public void setUp() throws UnsupportedEncodingException {
        user =new User("name",true,"address"
                ,"email","password","role",
                new Image());
        user.setUserId(1L);

        userResponse=new UserResponse(user.getUserId(),user.getName(),user.isPerson()
                ,user.getStreetAddress(),user.getPostalCode(),user.getPostOffice(),
                user.getEmail(),user.getRole(),1l);

        item=new ItemResponse();

        request1=new RegisterRentalRequest("message",new Date(),new Date(),1L,1L);
        rental=new Rental(request1.getMessage(), request1.getStartDate(),request1.getEndDate(),"status",user,new Item());
        rentalResponse=new RentalResponse(rental.getRentalId(), rental.getMessage()
                , rental.getStartDate(),rental.getEndDate(),rental.getStatus(),userResponse,item);

        rentalList=new ArrayList<>();
        rentalList.add(rentalResponse);
        mockMvc= MockMvcBuilders.standaloneSetup(rentalController).build();
        tokenComponent.generateToken(user.getUserId(),"USER");
    }

    @AfterEach
    void tearDown(){
        rental=null;
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
         */

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Mockito.when(rentalService.registerRental(Mockito.any())).thenReturn(rentalResponse);

        ResponseEntity<RentalResponse> responseEntity=rentalController.registerRental(request1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);

        assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("rental/register");
    }

    @Test
    void acceptRental() {
    }

    @Test
    void rejectRental() {
    }


    @Test
    void cancelRentalTest() throws Exception {
        Mockito.when(rentalService.cancelRental(rental.getRentalId()))
                .thenReturn(rentalResponse);
        mockMvc.perform(delete("rental/cancel/{1}")
                .contentType(MediaType.APPLICATION_JSON))
                .content(asJsonString(rentalResponse))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllRentalsForItemTest() throws Exception {
        Mockito.when(rentalService.getAllRentalsForItem(rental.getRentalId())).thenReturn(rentalList);
        mockMvc.perform(get("/retnal/for-item/{1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .content(asJsonString(rentalResponse))
                .andDo(MockMvcResultHandlers.print());

        verify(rentalService).getAllRentalsForItem(rentalResponse.getRentalId());
        verify(rentalService,times(1)).getAllRentalsForItem(rentalResponse.getRentalId());
    }

    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


}