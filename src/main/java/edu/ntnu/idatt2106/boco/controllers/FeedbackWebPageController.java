package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.RegisterFeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.service.FeedbackWebPageService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class that represents a FeedbackWebPageController endpoint
 */


@RestController
@RequestMapping(value = "/feedbackWebPage")
@EnableAutoConfiguration
@CrossOrigin
public class FeedbackWebPageController
{

    Logger logger = LoggerFactory.getLogger(FeedbackWebPageController.class);
    @Autowired
    private FeedbackWebPageService feedbackWebPageService;
    @Autowired
    private TokenComponent tokenComponent;

    /**
     * A method for registration of a feedback for the web page
     *
     * @param registerFeedbackWebPageRequest the feedback that is posted
     * @return returns a ResponseEntity with a http content status
     */
    @PostMapping(value = "registerFeedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<FeedbackWebPageResponse> registerFeedbackWebPage(@RequestBody RegisterFeedbackWebPageRequest registerFeedbackWebPageRequest)
    {
        logger.info("registration of a feedback");
        logger.info(registerFeedbackWebPageRequest.getMessage());
        try
        {
            FeedbackWebPageResponse feedback = feedbackWebPageService.register(registerFeedbackWebPageRequest);
            if (feedback == null)
            {
                return new ResponseEntity("Error: No feedbacks can be found ", HttpStatus.NO_CONTENT);
            }
            logger.info("Managed to post feedback");
            return new ResponseEntity(feedback, HttpStatus.CREATED);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot create a new feedback ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * A method for retrieving all feedbacks
     * Is only possible if user is admin
     *
     * @return returns FeedbackWebPageResponse as ResponseEntity
     */
    @GetMapping("/getFeedbacks")
    public ResponseEntity<List<FeedbackWebPageResponse>> getAllFeedbacksWebPage()
    {
        logger.info("Fetching all feedbacks for the web page...");
        try
        {
            if (!tokenComponent.isAdmin())
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<FeedbackWebPageResponse> feedbacks = feedbackWebPageService.getAll();
            if (feedbacks.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(feedbacks, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity("Could not fetch all feedbacks error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
