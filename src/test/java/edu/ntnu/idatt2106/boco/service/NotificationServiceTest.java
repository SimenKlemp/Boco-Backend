package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private FeedbackWebPageRepository feedbackWebPageRepository;

    @Before
    public void before()
    {
        for (Notification notification : notificationRepository.findAll())
        {
            notificationService.delete(notification.getNotificationId());
        }
    }

    @Test
    public void registerWithStatusRequested()
    {
        User existingUser = ModelFactory.getUser(null);
        existingUser = userRepository.save(existingUser);

        Item item = ModelFactory.getItem(null, existingUser);

        Rental existingRental = ModelFactory.getRental(existingUser, item);
        existingRental = rentalRepository.save(existingRental);

        NotificationResponse response = notificationService.register("REQUEST", existingRental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
    }

    @Test
    public void registerWithStatusSendRatingOwner()
    {
        User existingUser = ModelFactory.getUser(null);
        existingUser = userRepository.save(existingUser);

        Item item = ModelFactory.getItem(null, existingUser);

        Rental existingRental = ModelFactory.getRental(existingUser, item);
        existingRental = rentalRepository.save(existingRental);

        NotificationResponse response = notificationService.register("SEND_RATING_OWNER", existingRental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
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

        NotificationResponse response = notificationService.register("CANCELED", existingRental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
    }

}
