package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.NotificationRequest;
import edu.ntnu.idatt2106.boco.payload.request.RatingRequest;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;

import edu.ntnu.idatt2106.boco.payload.response.RatingResponse;
import edu.ntnu.idatt2106.boco.service.NotificationService;
import edu.ntnu.idatt2106.boco.service.RatingService;
import edu.ntnu.idatt2106.boco.service.RentalService;
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
 * A class that represents a ItemController
 */

@RestController
@RequestMapping(value = "/rating")
@EnableAutoConfiguration
@CrossOrigin
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private TokenComponent tokenComponent;

    Logger logger = LoggerFactory.getLogger(RentalController.class);

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingResponse> registerRate(@RequestBody RatingRequest request)
    {
        try
        {
            RatingResponse rating = ratingService.registerRating(request);
            if (rating == null)
            {
                return new ResponseEntity("Error: Rating can not be found ", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rating, HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot create a new rating ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-my/{userId}")
    public ResponseEntity<List<RatingResponse>> getRatings(@PathVariable("userId") long userId)
    {
        logger.info("Fetching all ratings for a user...");

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RatingResponse> ratings = ratingService.getRatings(userId);
            if (ratings == null || ratings.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(ratings, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
