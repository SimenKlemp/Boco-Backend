package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterFeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class FeedbackWebPageServiceTest
{
    @Autowired
    private FeedbackWebPageService feedbackWebPageService;

    @Autowired
    private FeedbackWebPageRepository feedbackWebPageRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void before()
    {
        for (FeedbackWebPage feedbackWebPage : feedbackWebPageRepository.findAll())
        {
            feedbackWebPageService.delete(feedbackWebPage.getFeedbackId());
        }
    }

    @Test
    public void registerCorrect()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        RegisterFeedbackWebPageRequest request = RequestFactory.getRegisterFeedbackWebPageRequest(user.getUserId());

        FeedbackWebPageResponse response = feedbackWebPageService.register(request);
        FeedbackWebPage feedbackWebPage = feedbackWebPageRepository.findById(response.getFeedbackId()).orElseThrow();

        assertThat(feedbackWebPage.getFeedbackId()).isEqualTo(response.getFeedbackId());
        assertThat(feedbackWebPage.getMessage()).isEqualTo(response.getMessage()).isEqualTo(request.getMessage());
        assertThat(feedbackWebPage.getUser().getUserId()).isEqualTo(response.getUser().getUserId()).isEqualTo(request.getUserId());
    }

    @Test
    public void registerWrongUserId()
    {
        RegisterFeedbackWebPageRequest request = RequestFactory.getRegisterFeedbackWebPageRequest(1L);

        FeedbackWebPageResponse response = feedbackWebPageService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void getAllWithFeedback()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);
        FeedbackWebPage feedbackWebPage1 = ModelFactory.getFeedbackWebPage(user1);
        feedbackWebPage1 = feedbackWebPageRepository.save(feedbackWebPage1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);
        FeedbackWebPage feedbackWebPage2 = ModelFactory.getFeedbackWebPage(user2);
        feedbackWebPage2 = feedbackWebPageRepository.save(feedbackWebPage2);

        FeedbackWebPage[] feedbacks = {feedbackWebPage1, feedbackWebPage2};

        List<FeedbackWebPageResponse> responses = feedbackWebPageService.getAll();

        assertThat(feedbacks.length).isEqualTo(responses.size());
        for (int i = 0; i < feedbacks.length; i++)
        {
            FeedbackWebPage feedbackWebPage = feedbacks[i];
            FeedbackWebPageResponse response = responses.get(i);

            assertThat(feedbackWebPage.getFeedbackId()).isEqualTo(response.getFeedbackId());
            assertThat(feedbackWebPage.getMessage()).isEqualTo(response.getMessage());
            assertThat(feedbackWebPage.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        }
    }

    @Test
    public void getAllEmpty()
    {
        List<FeedbackWebPageResponse> responses = feedbackWebPageService.getAll();
        assertThat(responses.size()).isZero();
    }

    @Test
    public void deleteCorrect()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        FeedbackWebPage feedbackWebPage = ModelFactory.getFeedbackWebPage(user);
        feedbackWebPage = feedbackWebPageRepository.save(feedbackWebPage);

        boolean success = feedbackWebPageService.delete(feedbackWebPage.getFeedbackId());
        Optional<FeedbackWebPage> optionalFeedbackWebPage = feedbackWebPageRepository.findById(feedbackWebPage.getFeedbackId());

        assertThat(success).isTrue();
        assertThat(optionalFeedbackWebPage.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongFeedbackId()
    {
        boolean success = feedbackWebPageService.delete(0L);
        assertThat(success).isFalse();
    }
}
