package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.repository.NotificationRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    public NotificationResponse register(String notificationStatus, Long rentalId)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        User user = null;
        switch(notificationStatus) {

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
        List<Notification> notifications = notificationRepository.findAllByUser(optionalUser.get());
        return Mapper.ToNotificationResponses(notifications);
    }

    public NotificationResponse updateNotificationIsPressed(long notificationId)
    {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if(optionalNotification.isEmpty()) return null;
        Notification notification = optionalNotification.get();

        notification.setPressed(true);


        notification = notificationRepository.save(notification);
        return Mapper.ToNotificationResponse(notification);
    }


}
