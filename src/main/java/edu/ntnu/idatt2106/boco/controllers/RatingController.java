package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.request.NotificationRequest;
import edu.ntnu.idatt2106.boco.payload.response.*;

import edu.ntnu.idatt2106.boco.service.NotificationService;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRatingRequest;

import edu.ntnu.idatt2106.boco.payload.response.RatingResponse;
import edu.ntnu.idatt2106.boco.service.RatingService;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.service.UserService;
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
 * A class that represents a RatingController
 */

@RestController
@RequestMapping(value = "/rating")
@EnableAutoConfiguration
@CrossOrigin
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenComponent tokenComponent;

    Logger logger = LoggerFactory.getLogger(RentalController.class);

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingResponse> registerRate(@RequestBody RegisterRatingRequest request)
    {
        try
        {
            RatingResponse rating = ratingService.register(request);
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

    @GetMapping("/get-my-owner/{userId}")
    public ResponseEntity<List<RatingResponse>> getRatingOwner(@PathVariable("userId") long userId)
    {
        logger.info("Fetching all ratings for a user...");

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RatingResponse> ratings = ratingService.getRatingsOwner(userId);
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

    @GetMapping("/get-my-user/{userId}")
    public ResponseEntity<List<RatingResponse>> getRatingUser(@PathVariable("userId") long userId)
    {
        logger.info("Fetching all items for a user...");

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RatingResponse> ratings = ratingService.getRatingsUser(userId);
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

    @GetMapping("/get-sent/{userId}/{rentalId}")
    public ResponseEntity<List<RatingResponse>> getRatingSent(@PathVariable("userId") long userId, @PathVariable("rentalId") long rentalId)
    {
        logger.info("Fetching all ratings sent...");

        try
        {
            RentalResponse response = rentalService.getRental(rentalId);
            boolean userBelongsToRental = ((response.getUser().getUserId() == userId) || (response.getItem().getUser().getUserId() == userId));

            if (!tokenComponent.haveAccessTo(userId) || !userBelongsToRental)
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RatingResponse> ratings = ratingService.getRatingsSent(userId, rentalId);
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

    @GetMapping("/getMeanRating/{userId}")
    public ResponseEntity<Integer> getMeanRating(@PathVariable("userId") long userId)
    {
        try
        {
            /*
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

             */
            return new ResponseEntity<>(ratingService.getMeanRating(userId), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
