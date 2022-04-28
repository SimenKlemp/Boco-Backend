package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.FeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.FeedbackWebPageRepository;
import edu.ntnu.idatt2106.boco.service.FeedbackWebPageService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static edu.ntnu.idatt2106.boco.controllers.RentalControllerTest.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedbackWebPageController.class)
class FeedbackWebPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private FeedbackWebPageController feedbackWebPageController;

    @MockBean
    private FeedbackWebPageRepository feedbackWebPageRepository;

    @MockBean
    private FeedbackWebPageService feedbackWebPageService;

    @MockBean
    private TokenComponent tokenComponent;

    private FeedbackWebPageResponse feedbackWebPageResponse1;
    private FeedbackWebPage feedbackWebPage1;
    private FeedbackWebPage feedbackWebPage2;
    private FeedbackWebPageResponse feedbackWebPageResponse2;
    private List<FeedbackWebPageResponse> feedbackWebPageList;
    private User user;
    private String token;


    @BeforeEach
    void setUp() throws Exception{
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        user=new User("name",true,"address","email"
                ,"password","ADMIN",new Image());
        user.setUserId(1L);

        token= tokenComponent.generateToken(user.getUserId(),user.getRole());
        UserResponse userResponse = new UserResponse(user.getUserId(), user.getName(), user.isPerson(),
                user.getStreetAddress(), user.getPostalCode(), user.getPostOffice(), user.getEmail(),
                user.getRole(), user.getImage() != null ? user.getImage().getImageId() : null);

        feedbackWebPage1=new FeedbackWebPage("message1",user);
        feedbackWebPage2=new FeedbackWebPage("message2",user);
        feedbackWebPageResponse1=new FeedbackWebPageResponse(feedbackWebPage1.getFeedbackId(),feedbackWebPage1.getMessage(),userResponse);
        feedbackWebPageResponse1=new FeedbackWebPageResponse(feedbackWebPage2.getFeedbackId(),feedbackWebPage2.getMessage(),userResponse);


        feedbackWebPageList=new ArrayList();
        feedbackWebPageList.add(feedbackWebPageResponse1);
        feedbackWebPageList.add(feedbackWebPageResponse2);

    }

    @AfterEach
    void  tearDown() {
        feedbackWebPageResponse1=feedbackWebPageResponse2=null;
        feedbackWebPageList=null;
    }

    @Test
    void registerFeedbackWebPage() throws Exception {
        when(feedbackWebPageService.registerFeedbackWebPage(new FeedbackWebPageRequest("message1",user.getUserId())))
                .thenReturn(feedbackWebPageResponse1);
        mockMvc.perform(post("/registerFeedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(feedbackWebPageResponse1))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void getAllFeedbacksWebPage() throws Exception {

        when(feedbackWebPageService.getAllFeedbacksWebPage()).thenReturn(feedbackWebPageList);
        mockMvc.perform(get("/feedbackWebPage/getFeedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(feedbackWebPageList))
                        .header("Authentication","Bearer " + token))
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size").value(feedbackWebPageList.size()))
                .andDo(print());
    }
}