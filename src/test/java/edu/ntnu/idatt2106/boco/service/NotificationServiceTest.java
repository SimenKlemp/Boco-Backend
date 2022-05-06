package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Notification;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.NotificationRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
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
public class NotificationServiceTest
{

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @BeforeEach
    public void beforeEach()
    {
        for (Notification notification : notificationRepository.findAll())
        {
            notificationService.delete(notification.getNotificationId());
        }
    }

    @Test
    public void registerWithStatusRequested()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        NotificationResponse response = notificationService.register("REQUEST", rental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getRental().getItem().getUser().getUserId()).isEqualTo(rental.getUser().getUserId());
    }

    @Test
    public void registerWithStatusSendRatingOwner()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        NotificationResponse response = notificationService.register("SEND_RATING_OWNER", rental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
    }

    @Test
    public void registerWithStatusRejected()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        NotificationResponse response = notificationService.register("CANCELED", rental.getRentalId());

        Notification notification = notificationRepository.findById(response.getNotificationId()).orElseThrow();
        assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
        assertThat(notification.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
    }

    @Test
    public void getAllMyWithNotification()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        Notification notification1 = ModelFactory.getNotification(rental, user);
        notification1 = notificationRepository.save(notification1);

        Notification notification2 = ModelFactory.getNotification(rental, user);
        notification2 = notificationRepository.save(notification2);

        Notification[] notifications = {notification1, notification2};

        List<NotificationResponse> responses = notificationService.getAllMy(user.getUserId());

        assertThat(notifications.length).isEqualTo(responses.size());
        for (int i = 0; i < notifications.length; i++)
        {
            Notification notification = notifications[i];
            NotificationResponse response = responses.get(i);

            assertThat(notification.getNotificationStatus()).isEqualTo(response.getNotificationStatus());
            assertThat(notification.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
            assertThat(notification.getRental().getUser().getUserId()).isEqualTo(response.getRental().getUser().getUserId());
        }
    }

    @Test
    public void getAllMyEmpty()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        List<NotificationResponse> responses = notificationService.getAllMy(user.getUserId());

        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getAllMyWrongUserId()
    {
        List<NotificationResponse> responses = notificationService.getAllMy(0L);

        assertThat(responses).isNull();
    }

    @Test
    public void updateNotificationIsPressedCorrect()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        Item item = ModelFactory.getItem(null, user);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user, item);
        rental = rentalRepository.save(rental);

        Notification notification = ModelFactory.getNotification(rental, user);
        notification = notificationRepository.save(notification);

        NotificationResponse response = notificationService.updateNotificationIsPressed(notification.getNotificationId());
        notification = notificationRepository.findById(notification.getNotificationId()).orElseThrow();

        assertThat(notification.isPressed()).isEqualTo(response.isPressed()).isTrue();
    }

    @Test
    public void updateNotificationIsPressedWrongNotificationId()
    {
        NotificationResponse response = notificationService.updateNotificationIsPressed(0L);
        assertThat(response).isNull();
    }

    @Test
    public void deleteCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Notification notification = ModelFactory.getNotification(rental, user2);
        notification = notificationRepository.save(notification);

        boolean success = notificationService.delete(notification.getNotificationId());
        Optional<Notification> optionalMessage = notificationRepository.findById(notification.getNotificationId());

        assertThat(success).isTrue();
        assertThat(optionalMessage.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongNotificationId()
    {
        boolean success = notificationService.delete(0L);
        assertThat(success).isFalse();
    }
}
