package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.BocoApplication;
import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.ModelFactory;
import edu.ntnu.idatt2106.boco.util.RequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BocoApplication.class)
public class RentalServiceTest
{
    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Before
    public void before()
    {
        for (Rental rental : rentalRepository.findAll())
        {
            rentalService.delete(rental.getRentalId());
        }
    }

    @Test
    public void registerCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        RegisterRentalRequest request = RequestFactory.getRegisterRentalRequest(user2.getUserId(), item.getItemId());

        RentalResponse response = rentalService.register(request);
        Rental rental = rentalRepository.findById(response.getRentalId()).orElseThrow();

        assertThat(rental.getRentalId()).isEqualTo(response.getRentalId());
        assertThat(rental.getStatus().toString()).isEqualTo(response.getStatus());
        assertThat(rental.getUser().getUserId()).isEqualTo(response.getUser().getUserId()).isEqualTo(request.getUserId());
        assertThat(rental.getItem().getItemId()).isEqualTo(response.getItem().getItemId()).isEqualTo(request.getItemId());
        assertThat(rental.getDeliveryInfo()).isEqualTo(response.getDeliveryInfo()).isEqualTo(request.getDeliveryInfo());
    }

    @Test
    public void registerWrongUserId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        RegisterRentalRequest request = RequestFactory.getRegisterRentalRequest(0L, item.getItemId());
        RentalResponse response = rentalService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerWrongItemId()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        RegisterRentalRequest request = RequestFactory.getRegisterRentalRequest(user1.getUserId(), 0L);
        RentalResponse response = rentalService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void registerWrongDeliveryInfo()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item.setIsPickupable(false);
        item = itemRepository.save(item);

        RegisterRentalRequest request = RequestFactory.getRegisterRentalRequest(user2.getUserId(), item.getItemId());
        request.setDeliveryInfo(Rental.DeliverInfo.PICKUP);

        RentalResponse response = rentalService.register(request);

        assertThat(response).isNull();
    }

    @Test
    public void getCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RentalResponse response = rentalService.getRental(rental.getRentalId());

        assertThat(rental.getRentalId()).isEqualTo(response.getRentalId());
        assertThat(rental.getStatus().toString()).isEqualTo(response.getStatus());
        assertThat(rental.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
        assertThat(rental.getItem().getItemId()).isEqualTo(response.getItem().getItemId());
        assertThat(rental.getDeliveryInfo()).isEqualTo(response.getDeliveryInfo());
    }

    @Test
    public void getWrongRentalId()
    {
        RentalResponse response = rentalService.getRental(0L);
        assertThat(response).isNull();
    }

    @Test
    public void getAllForItemWithRentals()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental1 = ModelFactory.getRental(user2, item);
        rental1 = rentalRepository.save(rental1);

        Rental rental2 = ModelFactory.getRental(user2, item);
        rental2 = rentalRepository.save(rental2);

        Rental[] rentals = {rental1, rental2};

        List<RentalResponse> responses = rentalService.getAllForItem(item.getItemId());

        assertThat(rentals.length).isEqualTo(responses.size());
        for (int i = 0; i < rentals.length; i++)
        {
            Rental rental = rentals[i];
            RentalResponse response = responses.get(i);

            assertThat(rental.getStatus().toString()).isEqualTo(response.getStatus());
            assertThat(rental.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
            assertThat(rental.getItem().getItemId()).isEqualTo(response.getItem().getItemId());
            assertThat(rental.getDeliveryInfo()).isEqualTo(response.getDeliveryInfo());
        }
    }

    @Test
    public void getAllForItemEmpty()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        List<RentalResponse> responses = rentalService.getAllForItem(item.getItemId());

        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getAllForItemWrongItemId()
    {
        List<RentalResponse> responses = rentalService.getAllForItem(0L);
        assertThat(responses).isNull();
    }

    @Test
    public void getAllWhereUserWithRentalsPending()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item1 = ModelFactory.getItem(null, user1);
        item1 = itemRepository.save(item1);

        Rental rental1 = ModelFactory.getRental(user2, item1);
        rental1 = rentalRepository.save(rental1);

        Rental rental2 = ModelFactory.getRental(user2, item1);
        rental2 = rentalRepository.save(rental2);

        Rental[] rentals = {rental1, rental2};

        Rental rental3 = ModelFactory.getRental(user2, item1);
        rental3.setStatus(Rental.Status.CANCELED);
        rental3 = rentalRepository.save(rental3);

        Item item2 = ModelFactory.getItem(null, user2);
        item2 = itemRepository.save(item2);
        Rental rental4 = ModelFactory.getRental(user1, item2);
        rental4 = rentalRepository.save(rental4);

        List<RentalResponse> responses = rentalService.getAllWhereUser(user2.getUserId(), Rental.Status.PENDING);

        assertThat(rentals.length).isEqualTo(responses.size());
        for (int i = 0; i < rentals.length; i++)
        {
            Rental rental = rentals[i];
            RentalResponse response = responses.get(i);

            assertThat(rental.getStatus().toString()).isEqualTo(response.getStatus());
            assertThat(rental.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
            assertThat(rental.getItem().getItemId()).isEqualTo(response.getItem().getItemId());
            assertThat(rental.getDeliveryInfo()).isEqualTo(response.getDeliveryInfo());
        }
    }

    @Test
    public void getAllWhereUserWithRentalsCanceled()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item1 = ModelFactory.getItem(null, user1);
        item1 = itemRepository.save(item1);

        Rental rental1 = ModelFactory.getRental(user2, item1);
        rental1.setStatus(Rental.Status.CANCELED);
        rental1 = rentalRepository.save(rental1);

        Rental rental2 = ModelFactory.getRental(user2, item1);
        rental2.setStatus(Rental.Status.CANCELED);
        rental2 = rentalRepository.save(rental2);

        Rental[] rentals = {rental1, rental2};

        Rental rental3 = ModelFactory.getRental(user2, item1);
        rental3 = rentalRepository.save(rental3);

        Item item2 = ModelFactory.getItem(null, user2);
        item2 = itemRepository.save(item2);
        Rental rental4 = ModelFactory.getRental(user1, item2);
        rental4 = rentalRepository.save(rental4);

        List<RentalResponse> responses = rentalService.getAllWhereUser(user2.getUserId(), Rental.Status.CANCELED);

        assertThat(rentals.length).isEqualTo(responses.size());
        for (int i = 0; i < rentals.length; i++)
        {
            Rental rental = rentals[i];
            RentalResponse response = responses.get(i);

            assertThat(rental.getStatus().toString()).isEqualTo(response.getStatus());
            assertThat(rental.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
            assertThat(rental.getItem().getItemId()).isEqualTo(response.getItem().getItemId());
            assertThat(rental.getDeliveryInfo()).isEqualTo(response.getDeliveryInfo());
        }
    }

    @Test
    public void getAllWhereUserEmpty()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        List<RentalResponse> responses = rentalService.getAllWhereUser(user.getUserId(), Rental.Status.PENDING);

        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getAllWhereUserWrongUserId()
    {
        List<RentalResponse> responses = rentalService.getAllWhereUser(0L, Rental.Status.PENDING);

        assertThat(responses).isNull();
    }

    @Test
    public void getAllWhereOwnerWithRentals()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item1 = ModelFactory.getItem(null, user1);
        item1 = itemRepository.save(item1);

        Rental rental1 = ModelFactory.getRental(user2, item1);
        rental1 = rentalRepository.save(rental1);

        Rental rental2 = ModelFactory.getRental(user2, item1);
        rental2 = rentalRepository.save(rental2);

        Rental[] rentals = {rental1, rental2};

        Item item2 = ModelFactory.getItem(null, user2);
        item2 = itemRepository.save(item2);
        Rental rental3 = ModelFactory.getRental(user1, item2);
        rental3 = rentalRepository.save(rental3);

        List<RentalResponse> responses = rentalService.getAllWhereOwner(user1.getUserId(), Rental.Status.PENDING);

        assertThat(rentals.length).isEqualTo(responses.size());
        for (int i = 0; i < rentals.length; i++)
        {
            Rental rental = rentals[i];
            RentalResponse response = responses.get(i);

            assertThat(rental.getStatus().toString()).isEqualTo(response.getStatus());
            assertThat(rental.getUser().getUserId()).isEqualTo(response.getUser().getUserId());
            assertThat(rental.getItem().getItemId()).isEqualTo(response.getItem().getItemId());
            assertThat(rental.getDeliveryInfo()).isEqualTo(response.getDeliveryInfo());
        }
    }

    @Test
    public void getAllWhereOwnerEmpty()
    {
        User user = ModelFactory.getUser(null);
        user = userRepository.save(user);

        List<RentalResponse> responses = rentalService.getAllWhereOwner(user.getUserId(), Rental.Status.PENDING);

        assertThat(responses.isEmpty()).isTrue();
    }

    @Test
    public void getAllWhereOwnerWrongUserId()
    {
        List<RentalResponse> responses = rentalService.getAllWhereOwner(0L, Rental.Status.PENDING);
        assertThat(responses).isNull();
    }

    @Test
    public void acceptCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RentalResponse response = rentalService.accept(rental.getRentalId());
        rental = rentalRepository.findById(rental.getRentalId()).orElseThrow();

        assertThat(rental.getStatus()).isEqualTo(Rental.Status.ACCEPTED);
    }

    @Test
    public void acceptWrongRentalId()
    {
        RentalResponse response = rentalService.accept(0L);
        assertThat(response).isNull();
    }

    @Test
    public void rejectCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RentalResponse response = rentalService.reject(rental.getRentalId());
        rental = rentalRepository.findById(rental.getRentalId()).orElseThrow();

        assertThat(rental.getStatus()).isEqualTo(Rental.Status.REJECTED);
    }

    @Test
    public void rejectWrongRentalId()
    {
        RentalResponse response = rentalService.reject(0L);
        assertThat(response).isNull();
    }

    @Test
    public void cancelCorrect()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        RentalResponse response = rentalService.cancel(rental.getRentalId());
        rental = rentalRepository.findById(rental.getRentalId()).orElseThrow();

        assertThat(rental.getStatus()).isEqualTo(Rental.Status.CANCELED);
    }

    @Test
    public void cancelWrongRentalId()
    {
        RentalResponse response = rentalService.cancel(0L);
        assertThat(response).isNull();
    }

    @Test
    public void deleteWithoutAnything()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        boolean success = rentalService.delete(rental.getRentalId());
        Optional<Rental> optionalRental = rentalRepository.findById(rental.getRentalId());

        assertThat(success).isTrue();
        assertThat(optionalRental.isEmpty()).isTrue();
    }

    @Test
    public void deleteWithRating()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Rating rating1 = ModelFactory.getRating(rental, user1);
        rating1 = ratingRepository.save(rating1);

        Rating rating2 = ModelFactory.getRating(rental, user2);
        rating2 = ratingRepository.save(rating2);

        boolean success = rentalService.delete(rental.getRentalId());
        Optional<Rental> optionalRental = rentalRepository.findById(rental.getRentalId());
        Optional<Rating> optionalRating1 = ratingRepository.findById(rating1.getRatingId());
        Optional<Rating> optionalRating2 = ratingRepository.findById(rating2.getRatingId());

        assertThat(success).isTrue();
        assertThat(optionalRental.isEmpty()).isTrue();
        assertThat(optionalRating1.isEmpty()).isTrue();
        assertThat(optionalRating2.isEmpty()).isTrue();
    }

    @Test
    public void deleteWithMessage()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Message message1 = ModelFactory.getMessage(user1, rental);
        message1 = messageRepository.save(message1);

        Message message2 = ModelFactory.getMessage(user2, rental);
        message2 = messageRepository.save(message2);

        boolean success = rentalService.delete(rental.getRentalId());
        Optional<Rental> optionalRental = rentalRepository.findById(rental.getRentalId());
        Optional<Message> optionalMessage1 = messageRepository.findById(message1.getMessageId());
        Optional<Message> optionalMessage2 = messageRepository.findById(message2.getMessageId());

        assertThat(success).isTrue();
        assertThat(optionalRental.isEmpty()).isTrue();
        assertThat(optionalMessage1.isEmpty()).isTrue();
        assertThat(optionalMessage2.isEmpty()).isTrue();
    }

    @Test
    public void deleteWithNotification()
    {
        User user1 = ModelFactory.getUser(null);
        user1 = userRepository.save(user1);

        User user2 = ModelFactory.getUser(null);
        user2 = userRepository.save(user2);

        Item item = ModelFactory.getItem(null, user1);
        item = itemRepository.save(item);

        Rental rental = ModelFactory.getRental(user2, item);
        rental = rentalRepository.save(rental);

        Notification notification1 = ModelFactory.getNotification(rental, user1);
        notification1 = notificationRepository.save(notification1);

        Notification notification2 = ModelFactory.getNotification(rental, user1);
        notification2 = notificationRepository.save(notification2);

        boolean success = rentalService.delete(rental.getRentalId());
        Optional<Rental> optionalRental = rentalRepository.findById(rental.getRentalId());
        Optional<Notification> optionalNotification1 = notificationRepository.findById(notification1.getNotificationId());
        Optional<Notification> optionalNotification2 = notificationRepository.findById(notification2.getNotificationId());

        assertThat(success).isTrue();
        assertThat(optionalRental.isEmpty()).isTrue();
        assertThat(optionalNotification1.isEmpty()).isTrue();
        assertThat(optionalNotification2.isEmpty()).isTrue();
    }

    @Test
    public void deleteWrongRentalId()
    {
        boolean success = rentalService.delete(0L);
        assertThat(success).isFalse();
    }
}
