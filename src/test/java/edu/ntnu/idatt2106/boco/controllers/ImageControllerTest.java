package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageController imageController;

    @MockBean
    ImageService imageService;

    @Test
    void getImage() {
    }

    @Test
    void uploadImage() {
    }
}