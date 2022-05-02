package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Notification;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.NotificationRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RentalRepository rentalRepository;

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
        RepositoryMock.mockNotificationRepository(notificationRepository);
    }

    @Test
    public void registerWithStatusRequested()
    {
        User existingUser = ModelFactory.getUser(null);
        existingUser = userRepository.save(existingUser);

        Item item = ModelFactory.getItem(null, existingUser);

        Rental existingRental = ModelFactory.getRental(existingUser, item);
        existingRental = rentalRepository.save(existingRental);

        NotificationResponse response = notificationService.registerNotification("REQUEST", existingRental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getRental().getItem().getUser().getUserId()).isEqualTo(existingRental.getUser().getUserId());
    }

    @Test
    public void registerWithStatusSendRatingOwner()
    {
        User existingUser = ModelFactory.getUser(null);
        existingUser = userRepository.save(existingUser);

        Item item = ModelFactory.getItem(null, existingUser);

        Rental existingRental = ModelFactory.getRental(existingUser, item);
        existingRental = rentalRepository.save(existingRental);

        NotificationResponse response = notificationService.registerNotification("SEND_RATING_OWNER", existingRental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
    }

    @Test
    public void registerWithStatusRejected()
    {
        User existingUser = ModelFactory.getUser(null);
        existingUser = userRepository.save(existingUser);

        Item item = ModelFactory.getItem(null, existingUser);

        Rental existingRental = ModelFactory.getRental(existingUser, item);
        existingRental = rentalRepository.save(existingRental);

        NotificationResponse response = notificationService.registerNotification("CANCELED", existingRental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
    }

}
