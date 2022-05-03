package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.NotificationRequest;
import edu.ntnu.idatt2106.boco.payload.request.RatingRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.payload.response.RatingResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService;

    public RatingResponse registerRating(RatingRequest request)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(request.getRentalId());
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        Rating rating = null;
        if(Objects.equals(request.getUserId(), rental.getItem().getUser().getUserId())){

            Optional<User> optionalUser = userRepository.findById(rental.getUser().getUserId());
            if (optionalRental.isEmpty()) return null;
            User user = optionalUser.get();

            rating = new Rating(
                    request.getRate(),
                    request.getFeedback(),
                    rental,
                    user
            );
            notificationService.registerNotification("RECEIVED_RATING_OWNER", request.getRentalId());
        }else{
            Optional<User> optionalUser = userRepository.findById(rental.getItem().getUser().getUserId());
            if (optionalRental.isEmpty()) return null;
            User user = optionalUser.get();

            rating = new Rating(
                    request.getRate(),
                    request.getFeedback(),
                    rental,
                    user
            );
            notificationService.registerNotification("RECEIVED_RATING_USER", request.getRentalId());
        }

        rating = ratingRepository.save(rating);
        return Mapper.ToRatingResponse(rating);
    }
    public List<RatingResponse> getRatingsOwner(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        List<Rating> allRatings = ratingRepository.findAllByUser(optionalUser.get());

        if (allRatings.isEmpty()) return null;

        ArrayList<Rating> ratings = new ArrayList<>();
        for (Rating allRating : allRatings) {
            if (allRating.getRental().getItem().getUser().getUserId() == userId) {
                ratings.add(allRating);
            }
        }
        return Mapper.ToRatingResponses(ratings);
    }

    public List<RatingResponse> getRatingsUser(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        List<Rating> allRatings = ratingRepository.findAllByUser(optionalUser.get());

        ArrayList<Rating> ratings = new ArrayList<>();

        for (Rating allRating : allRatings) {
            if (allRating.getRental().getUser().getUserId() == userId) {
                ratings.add(allRating);
            }
        }
        return Mapper.ToRatingResponses(ratings);
    }

    public Integer getMeanRating(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return 0;
        int rating = (int) ratingRepository.getMeanRating(userId);

        return rating;
    }
}

