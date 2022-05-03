package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterFeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FeedbackWebPageServiceTest
{
    @InjectMocks
    private FeedbackWebPageService feedbackWebPageService;

    @Mock
    private FeedbackWebPageRepository feedbackWebPageRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach()
    {
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockFeedbackWebPageRepository(feedbackWebPageRepository);
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
}
