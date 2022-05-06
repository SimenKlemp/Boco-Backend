package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Notification;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.NotificationRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService
{
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    public NotificationResponse register(String notificationStatus, Long rentalId)
    {

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        if (notificationRepository.existsByNotificationStatusAndRental(notificationStatus, rental))
        {
            return null;
        }

        User user = null;
        switch (notificationStatus)
        {

            case "REQUEST":
            case "CANCELED":
            case "SEND_RATING_OWNER":
            case "RECEIVED_RATING_USER":
                user = rental.getItem().getUser();
                break;
            case "ACCEPTED":
            case "REJECTED":
            case "SEND_RATING_USER":
            case "RECEIVED_RATING_OWNER":
                user = rental.getUser();
                break;

            default:
                break;
        }


        Notification notification = new Notification(
                notificationStatus,
                false,
                rental,
                user
        );
        notification = notificationRepository.save(notification);
        return Mapper.ToNotificationResponse(notification);
    }

    public List<NotificationResponse> getAllMy(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();
        List<Rental> rentalsUser = rentalRepository.findAllByUser(user);

        Date today = new Date();
        for (Rental rental : rentalsUser)
        {
            if (rental.getStatus() == Rental.Status.ACCEPTED)
            {
                if (rental.getEndDate().before(today))
                {
                    register("SEND_RATING_USER", rental.getRentalId());
                }
            }
        }

        List<Item> items = itemRepository.findAllByUser(user);

        List<Rental> rentalsOwner = new ArrayList<>();
        for (Item item : items)
        {
            rentalsOwner.addAll(
                    rentalRepository.findAllByItem(item)
            );
        }
        for (Rental rental : rentalsOwner)
        {
            if (rental.getStatus() == Rental.Status.ACCEPTED)
            {
                if (rental.getEndDate().before(today))
                {
                    register("SEND_RATING_OWNER", rental.getRentalId());
                }
            }
        }

        List<Notification> notifications = notificationRepository.findAllByUser(user);

        return Mapper.ToNotificationResponses(notifications);
    }

    public NotificationResponse updateNotificationIsPressed(long notificationId)
    {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isEmpty()) return null;
        Notification notification = optionalNotification.get();

        notification.setPressed(true);

        notification = notificationRepository.save(notification);
        return Mapper.ToNotificationResponse(notification);
    }

    public boolean delete(Long notificationId)
    {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isEmpty()) return false;
        Notification notification = optionalNotification.get();

        notificationRepository.delete(notification);
        return true;
    }
}
