package edu.ntnu.idatt2106.boco.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Krever at @AutoConfigureMockMvc er satt, brukes til å fyre av requests

    @Autowired
    private ObjectMapper objectMapper;

    public String token;
    @BeforeEach
    void doSomethingBeforeEveryTest() throws Exception {
        //Trying to fetch a token
        LoginRequest loginRequest = new LoginRequest("test@email.com", "EncryptedPassword");

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        //Find the token
        String response = result.getResponse().getContentAsString();

        //set the token
    }

    /*
    //Denne testen lager nye fag hver gang du kjører
    @Test
    void createItemTest() throws Exception {
        // Det er en del forskjellige libs som brukes her, se static imports øverst
        // En har også tilsvarende metoder for POST/PUT/DELETE osv.

        RegisterItemRequest itemRequest = new RegisterItemRequest("streetAddress", "postalCode", "postOffice", 200f, "This is a item made from test", "Category", "Title", true, true, 1L, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/item").header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").exists());
    }


     */

    @Test
    void getAllItemsTest() throws Exception {
        // Det er en del forskjellige libs som brukes her, se static imports øverst
        // En har også tilsvarende metoder for POST/PUT/DELETE osv.
        mockMvc.perform(get("/item/all/").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].address", is("Address")))
                .andExpect(jsonPath("$[0].price", is(200)))
                .andExpect(jsonPath("$[0].description", is("This is a description")))
                .andExpect(jsonPath("$[0].category", is("Category")))
                .andExpect(jsonPath("$[0].title", is("Title")));
    }
    @Test
    void getMyItemsTest() throws Exception {
        // Det er en del forskjellige libs som brukes her, se static imports øverst
        // En har også tilsvarende metoder for POST/PUT/DELETE osv.
        mockMvc.perform(get("/item/1/").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].address", is("Address")))
                .andExpect(jsonPath("$[0].price", is(200)))
                .andExpect(jsonPath("$[0].description", is("This is a description")))
                .andExpect(jsonPath("$[0].category", is("Category")))
                .andExpect(jsonPath("$[0].title", is("Title")));
    }
}
