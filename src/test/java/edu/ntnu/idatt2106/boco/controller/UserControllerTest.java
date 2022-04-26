package edu.ntnu.idatt2106.boco.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) // JUnit
//@SpringBootTest(webEnvironment = MOCK, classes = TestsDemoApplication.class) // Spring
@SpringBootTest
@AutoConfigureMockMvc // Trengs for å kunne autowire MockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void existentUserCanGetTokenAndAuthentication() throws Exception {

        LoginRequest loginRequest = new LoginRequest("test16@test.no", "string");
        

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
}

