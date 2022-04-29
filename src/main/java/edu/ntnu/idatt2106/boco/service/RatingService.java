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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

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

        }

        rating = ratingRepository.save(rating);
        return Mapper.ToRatingResponse(rating);
    }
    public List<RatingResponse> getRatings(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        List<Rating> ratings = ratingRepository.findAllByUser(optionalUser.get());
        return Mapper.ToRatingResponses(ratings);
    }
}

