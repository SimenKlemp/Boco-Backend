package edu.ntnu.idatt2106.boco.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.LoginResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParseException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static edu.ntnu.idatt2106.boco.controllers.RentalControllerTest.asJsonString;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;
    @MockBean
    UserController userController;
    @MockBean
    UserController userService;
    @MockBean
    private TokenComponent tokenComponent;

    @Autowired
    private WebApplicationContext context;

    private User user;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private UserResponse userResponse;
    private List<LoginResponse> loginResponseList;
    private RegisterUserRequest registerUserRequest;
    private String token;
    private UpdateUserRequest updatedRegisterUserRequest;
    private UserResponse updateUserResponse;


    @BeforeEach
    public void setUp() throws UnsupportedEncodingException {

        JacksonTester.initFields(this, new ObjectMapper());
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        Image image =new Image();
        user=new User("name",true,"address","email"
                ,"password","role",image);
        user.setUserId(1L);
        token=tokenComponent.generateToken(user.getUserId(),token);
        loginRequest=new LoginRequest("email1","password");

        userResponse = new UserResponse(1l,"name",true,"streetAddress"
                ,"postalCode","postOffice","email","USER",new Image().getImageId());
        loginResponse=new LoginResponse(userResponse.getEmail(), userResponse);

        registerUserRequest=new RegisterUserRequest("name",true,"streetAddress","postalCode","postOffice"
                ,"email","password",image.getImageId());

       updatedRegisterUserRequest = new UpdateUserRequest("updated", true, "updated", "updated", "postOffice"
                , "email", "password", image.getImageId());

        updateUserResponse = new UserResponse(user.getUserId(), "updated", true, "updated", "updated",
                "postOffice", "email", "role", new Image().getImageId());
    }
    @AfterEach
    public void cleanup() {
        loginRequest=null;
        loginResponse=null;
        userRepository.deleteAll();
    }

    @Test
    void loginTest() throws Exception {

        //when(userService.login(loginRequest)).thenReturn(userResponse);
        mockMvc.perform(get("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userResponse))
                .with(csrf()))
        .andExpect(status().isOk())
                .andDo(print());
    }

    /*
    Test method for checking if the user has registered successfully

     */

    @Test
    public void registerTest() throws Exception {
      //when(userService.register(registerUserRequest)).thenReturn(userResponse);
      //mockMvc.perform(post("/user/register")
              //.contentType(MediaType.APPLICATION_JSON))
             /** .content(asJsonString(userResponse))
              .header("Authentication","Bearer "+ token))
        .andExpect(status().isOk())
              .andDo(print())*/;
    }

    /*
    Test method to update user info

     */

    @Test
    public void updateUserTest() throws Exception {
        long userId=user.getUserId();
        when(userService.updateUser(userId,updatedRegisterUserRequest));
    }

    /*
    Test method to delete user form database
     */
    @Test
    public void deleteUserTest() throws Exception {


    }

}