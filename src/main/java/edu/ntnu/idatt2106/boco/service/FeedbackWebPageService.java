package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.FeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.repository.FeedbackWebPageRepository;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * @param feedbackWebPageRequest the feedback that is posted
     * @return returns status int
     */

    public int registerFeedbackWebPage(FeedbackWebPageRequest feedbackWebPageRequest){
        User user = userRepository.findById(feedbackWebPageRequest.getUserId()).get();
        if(user == null){
            return 0;
        }
        FeedbackWebPage feedbackWebPage = new FeedbackWebPage(feedbackWebPageRequest.getFeedbackMessage(), user);
        feedBackWebPageRepository.save(feedbackWebPage);
        return 1;

    }

    /**
     * A method for retrieving all feedbacks for the web page from users
     * @return returns a list of feedbacks of the web page
     */

    public List<FeedbackWebPage> getAllFeedbacksWebPage(){
        List<FeedbackWebPage> feedbacks = new ArrayList<FeedbackWebPage>();

        feedBackWebPageRepository.findAll().forEach(feedbacks::add);

        return feedbacks;

    }
}
