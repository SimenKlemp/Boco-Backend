package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.FeedbackWebPageRequest;
import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
import edu.ntnu.idatt2106.boco.service.FeedbackWebPageService;
import edu.ntnu.idatt2106.boco.service.ItemService;
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
 *
 */

@RestController
@RequestMapping(value = "/item")
@EnableAutoConfiguration
@CrossOrigin
public class FeedbackWebPageController {

    @Autowired
    private FeedbackWebPageService feedbackWebPageService;

    Logger logger = LoggerFactory.getLogger(FeedbackWebPageController.class);


    /**
     * A method for registration of a feedback for the web page
     * @param feedbackWebPageRequest the feedback that is posted
     * @return returns a ResponseEntity with a http content status
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> registerFeedbackWebPage(@RequestBody FeedbackWebPageRequest feedbackWebPageRequest) {
        try {
            if (feedbackWebPageService.registerFeedbackWebPage(feedbackWebPageRequest) == 0) {
                return new ResponseEntity("Error: No feedbacks can be found ", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity("A new feedback for web page has been added successfully ", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity("Error: Cannot create a new feedback ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("")
    public ResponseEntity<List> getAllFeedbacksWebPage() {
        logger.info("Fetching all all feedbacks for the web page...");
        try {
            if (feedbackWebPageService.getAllFeedbacksWebPage().isEmpty()) {
                return new ResponseEntity(0, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(feedbackWebPageService.getAllFeedbacksWebPage(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
