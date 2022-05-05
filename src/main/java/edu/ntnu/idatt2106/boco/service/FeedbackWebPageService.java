package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterFeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.repository.FeedbackWebPageRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A class that represents a FeedbackWebPageService
 */

@Service
public class FeedbackWebPageService {

    @Autowired
    FeedbackWebPageRepository feedBackWebPageRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * A method for registration of a feedback for the webpage
     * @param registerFeedbackWebPageRequest the feedback that is posted
     * @return returns status int
     */

    public FeedbackWebPageResponse register(RegisterFeedbackWebPageRequest registerFeedbackWebPageRequest)
    {
        Optional<User> optionalUser = userRepository.findById(registerFeedbackWebPageRequest.getUserId());
        if(optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        FeedbackWebPage feedbackWebPage = new FeedbackWebPage(registerFeedbackWebPageRequest.getMessage(), user);

        feedbackWebPage = feedBackWebPageRepository.save(feedbackWebPage);
        return Mapper.ToFeedbackWebPageResponse(feedbackWebPage);
    }

    /**
     * A method for retrieving all feedbacks for the web page from users
     * @return returns a list of feedbacks of the web page
     */
    public List<FeedbackWebPageResponse> getAll()
    {
        List<FeedbackWebPage> feedbacks = feedBackWebPageRepository.findAll();

        return Mapper.ToFeedbackWebPageResponses(feedbacks);
    }

    public boolean delete(Long feedbackId)
    {
        Optional<FeedbackWebPage> optionalFeedbackWebPage = feedBackWebPageRepository.findById(feedbackId);
        if (optionalFeedbackWebPage.isEmpty()) return false;
        FeedbackWebPage feedbackWebPage = optionalFeedbackWebPage.get();

        feedBackWebPageRepository.delete(feedbackWebPage);
        return true;
    }

}
