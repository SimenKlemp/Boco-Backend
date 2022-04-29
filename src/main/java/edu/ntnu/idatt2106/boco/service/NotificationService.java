package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.NotificationRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.NotificationRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    public NotificationResponse registerNotification(NotificationRequest request)
    {
        System.out.println(request.getRentalId());
        Optional<Rental> optionalRental = rentalRepository.findById(request.getRentalId());
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        User user = null;
        switch(request.getNotificationStatus()) {

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
                request.getNotificationStatus(),
                false,
                rental,
                user
        );
        notification = notificationRepository.save(notification);
        return Mapper.ToNotificationResponse(notification);
    }

    public List<NotificationResponse> getNotifications(long userId)
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
