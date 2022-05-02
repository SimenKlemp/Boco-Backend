package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.factories.modelFactroies.FeedBackWebPageFactory;
import edu.ntnu.idatt2106.boco.factories.modelFactroies.UserFactory;
import edu.ntnu.idatt2106.boco.factories.requestFactroies.FeedbackWebPageRequestFactory;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.FeedBackWebPageResponseFactory;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.ResponseFactories;
import edu.ntnu.idatt2106.boco.factories.responseFactroies.UserResponseFactory;
import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.FeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.FeedbackWebPageRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
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
import java.util.Objects;

import static edu.ntnu.idatt2106.boco.controllers.RentalControllerTest.asJsonString;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    private UserRepository userRepository;

    private FeedBackWebPageResponseFactory feedbackWebPageResponse1;
    private FeedBackWebPageResponseFactory feedbackWebPageResponse2;
    private List<FeedbackWebPageResponse> feedbackWebPageList;
    private String token;
    private UserResponseFactory userResponse;
    private FeedbackWebPageRequestFactory feedbackWebPageRequest;
    private UserFactory user;
    private ResponseFactories responseFactories;

    @BeforeEach
    void setUp() throws Exception{
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        user=new UserFactory();
        assert user != null;
        userRepository.save(user.getObject());

        userResponse=new UserResponseFactory();
       assert userResponse != null;


        feedbackWebPageResponse1=new FeedBackWebPageResponseFactory();
       assert feedbackWebPageResponse1 !=null;

        feedbackWebPageResponse2=new FeedBackWebPageResponseFactory();
       assert feedbackWebPageResponse2 !=null;


        feedbackWebPageRequest=new FeedbackWebPageRequestFactory();
       assert feedbackWebPageRequest !=null;
       FeedBackWebPageFactory feedBackWebPage=new FeedBackWebPageFactory();

       feedbackWebPageRepository.save (feedBackWebPage.getObject());

        feedbackWebPageList=new ArrayList();
        feedbackWebPageList.add(feedbackWebPageResponse1.getObject());
        feedbackWebPageList.add(feedbackWebPageResponse2.getObject());

    }

    @AfterEach
    void  tearDown() {
        feedbackWebPageResponse1=feedbackWebPageResponse2=null;
        feedbackWebPageList=null;
        feedbackWebPageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerFeedbackWebPage() throws Exception {
        when(feedbackWebPageService.registerFeedbackWebPage(any()))
               .thenReturn(feedbackWebPageResponse1.getObject());
        mockMvc.perform(post("/registerFeedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(feedbackWebPageResponse1.getObject()))
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