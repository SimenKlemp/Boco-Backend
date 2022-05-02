package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.RatingRequest;
import edu.ntnu.idatt2106.boco.payload.response.RatingResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RepositoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {
    @InjectMocks
    private RatingService ratingService;


    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private FeedbackWebPageRepository feedbackWebPageRepository;

    @BeforeEach
    public void beforeEach()
    {
        RepositoryMock.mockUserRepository(userRepository);
        RepositoryMock.mockImageRepository(imageRepository);
        RepositoryMock.mockItemRepository(itemRepository);
        RepositoryMock.mockRentalRepository(rentalRepository);
        RepositoryMock.mockFeedbackWebPageRepository(feedbackWebPageRepository);
        RepositoryMock.mockRatingRepository(ratingRepository);
        RepositoryMock.mockNotificationRepository(notificationRepository);
    }

    /*
    @Test
    public void registerRatingTest()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        RatingRequest request = new RatingRequest(
                "feedback",
                5,
                rental.getRentalId(),
                user.getUserId()
        );

        RatingResponse response = ratingService.registerRating(request);

        Rating rating = ratingRepository.findById(response.getRatingId()).orElseThrow();
        assertThat(rating.getFeedback()).isEqualTo(response.getFeedback()).isEqualTo(request.getFeedback());
        assertThat(rating.getRate()).isEqualTo(response.getRate()).isEqualTo(request.getRate());
        assertThat(rating.getRental()).isEqualTo(response.getRental());
    }

     */

}
