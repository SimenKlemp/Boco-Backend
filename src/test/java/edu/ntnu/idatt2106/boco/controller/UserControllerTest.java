package edu.ntnu.idatt2106.boco.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static java.awt.SystemColor.info;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) // JUnit
//@SpringBootTest(webEnvironment = MOCK, classes = TestsDemoApplication.class) // Spring
@SpringBootTest
@AutoConfigureMockMvc // Trengs for å kunne autowire MockMvc
public class UserControllerTest {
    @MockBean
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void existentUserCanGetTokenAndAuthentication() throws Exception {

        LoginRequest loginRequest = new LoginRequest("test16@test.no", "passord");

        mockMvc.perform(post("http://localhost:8080/api/user/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").exists());
    }
    @Test
    public void nonexistentUserCannotGetToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("WRONGusernameTEST", "WRONGpasswordTEST");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/user/login")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden()).andReturn();
    }

    @Test
    public void should_return_created_user() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("test user");

        User user = new User();
        user.setName(request.getName());

        //when(userService.register(any(RegisterUserRequest.class)));

        /*mockMvc.perform(post("http://localhost:8080/api/user/register")
                .contentType(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
             //   .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()));

         */
    }
}

