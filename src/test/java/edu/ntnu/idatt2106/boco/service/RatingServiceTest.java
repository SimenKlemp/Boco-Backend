package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rating;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRatingRequest;
import edu.ntnu.idatt2106.boco.payload.response.RatingResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.RatingRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class RatingServiceTest
{
    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    public void beforeEach()
    {
        for (Rating rating : ratingRepository.findAll())
        {
            ratingService.delete(rating.getRatingId());
        }
    }

    @Test
    public void registerAsOwner()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterRatingRequest request = RequestFactory.getRegisterRatingRequest(rental.getRentalId(), user1.getUserId());

        RatingResponse response = ratingService.register(request);
        Rating rating = ratingRepository.findById(response.getRatingId()).orElseThrow();

        assertThat(rating.getRatingId()).isEqualTo(response.getRatingId());
        assertThat(rating.getRate()).isEqualTo(response.getRate()).isEqualTo(request.getRate());
        assertThat(rating.getFeedback()).isEqualTo(response.getFeedback()).isEqualTo(request.getFeedback());
        assertThat(rating.getRental().getRentalId()).isEqualTo(response.getRental().getRentalId()).isEqualTo(request.getRentalId());
        assertThat(rating.getUser().getUserId()).isEqualTo(user2.getUserId());
    }

    @Test
    public void registerAsUser()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterRatingRequest request = RequestFactory.getRegisterRatingRequest(rental.getRentalId(), user2.getUserId());

        RatingResponse response = ratingService.register(request);
        Rating rating = ratingRepository.findById(response.getRatingId()).orElseThrow();

        assertThat(rating.getRatingId()).isEqualTo(response.getRatingId());
        assertThat(rating.getRate()).isEqualTo(response.getRate()).isEqualTo(request.getRate());
        assertThat(rating.getFeedback()).isEqualTo(response.getFeedback()).isEqualTo(request.getFeedback());
        assertThat(rating.getRental().getRentalId()).isEqualTo(response.getRental().getRentalId()).isEqualTo(request.getRentalId());
        assertThat(rating.getUser().getUserId()).isEqualTo(user1.getUserId());
    }


    @Test
    public void registerWrongRentalId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        RegisterRatingRequest request = RequestFactory.getRegisterRatingRequest(0L, user1.getUserId());

        RatingResponse response = ratingService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerWrongUserId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RegisterRatingRequest request = RequestFactory.getRegisterRatingRequest(rental.getRentalId(), 0L);

        RatingResponse response = ratingService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerWrongUserIdForRentalId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        User user3 = ModelFactory.getUser(null);
        user3 = userRepository.save(user3);

        RegisterRatingRequest request = RequestFactory.getRegisterRatingRequest(rental.getRentalId(), user3.getUserId());

        RatingResponse response = ratingService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void getRatingsOwnerWithRating()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating1 = ModelFactory.getRating(rental, user1);
        rating1 = ratingRepository.save(rating1);

        Rating rating2 = ModelFactory.getRating(rental, user1);
        rating2 = ratingRepository.save(rating2);

        Rating[] ratings = {rating1, rating2};

        List<RatingResponse> responses = ratingService.getRatingsOwner(user1.getUserId());

        assertThat(ratings.length).isEqualTo(responses.size());
        for (int i = 0; i < ratings.length; i++)
        {
            Rating rating = ratings[i];
            RatingResponse response = responses.get(i);

            assertThat(rating.getRate()).isEqualTo(response.getRate());
            assertThat(rating.getFeedback()).isEqualTo(response.getFeedback());
            assertThat(rating.getRental().getRentalId()).isEqualTo(response.getRental().getRentalId());
            assertThat(rating.getUser().getUserId()).isEqualTo(user1.getUserId());
        }
    }

    @Test
    public void getRatingsOwnerEmpty()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating = ModelFactory.getRating(rental, user2);
        rating = ratingRepository.save(rating);

        List<RatingResponse> responses = ratingService.getRatingsOwner(user1.getUserId());

        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getRatingsOwnerWrongUserId()
    {
        List<RatingResponse> responses = ratingService.getRatingsOwner(0L);
        assertThat(responses).isNull();
    }

    @Test
    public void getRatingsUserWithRating()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating1 = ModelFactory.getRating(rental, user2);
        rating1 = ratingRepository.save(rating1);

        Rating rating2 = ModelFactory.getRating(rental, user2);
        rating2 = ratingRepository.save(rating2);

        Rating[] ratings = {rating1, rating2};

        List<RatingResponse> responses = ratingService.getRatingsUser(user2.getUserId());

        assertThat(ratings.length).isEqualTo(responses.size());
        for (int i = 0; i < ratings.length; i++)
        {
            Rating rating = ratings[i];
            RatingResponse response = responses.get(i);

            assertThat(rating.getRate()).isEqualTo(response.getRate());
            assertThat(rating.getFeedback()).isEqualTo(response.getFeedback());
            assertThat(rating.getRental().getRentalId()).isEqualTo(response.getRental().getRentalId());
            assertThat(rating.getUser().getUserId()).isEqualTo(user2.getUserId());
        }
    }

    @Test
    public void getRatingsUserEmpty()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating = ModelFactory.getRating(rental, user1);
        rating = ratingRepository.save(rating);

        List<RatingResponse> responses = ratingService.getRatingsUser(user1.getUserId());

        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getRatingsUserWrongUserId()
    {
        List<RatingResponse> responses = ratingService.getRatingsUser(0L);
        assertThat(responses).isNull();
    }

    @Test
    public void getMeanRatingWithRating()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating1 = ModelFactory.getRating(rental, user1);
        rating1.setRate(1);
        rating1 = ratingRepository.save(rating1);

        Rating rating2 = ModelFactory.getRating(rental, user1);
        rating2.setRate(5);
        rating2 = ratingRepository.save(rating2);

        Integer rate = ratingService.getMeanRating(user1.getUserId());

        assertThat(rate).isEqualTo(3);
    }

    @Test
    public void getMeanRatingEmpty()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Integer rate = ratingService.getMeanRating(user.getUserId());

        assertThat(rate).isEqualTo(5);
    }

    @Test
    public void getMeanRatingWrongUserId()
    {
        Integer rate = ratingService.getMeanRating(0L);

        assertThat(rate).isNull();
    }

    @Test
    public void deleteCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating = ModelFactory.getRating(rental, user1);
        rating = ratingRepository.save(rating);

        boolean success = ratingService.delete(rating.getRatingId());
        Optional<Rating> optionalRating = ratingRepository.findById(rating.getRatingId());

        assertThat(success).isTrue();
        assertThat(optionalRating.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongRatingId()
    {
        boolean success = ratingService.delete(0L);

        assertThat(success).isFalse();
    }
}
