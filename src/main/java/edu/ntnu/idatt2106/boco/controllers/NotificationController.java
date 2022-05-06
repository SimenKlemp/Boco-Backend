package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.payload.response.NotificationResponse;
import edu.ntnu.idatt2106.boco.service.NotificationService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class that represents a ItemController
 */

@RestController
@RequestMapping(value = "/notification")
@EnableAutoConfiguration
@CrossOrigin
public class NotificationController
{

    Logger logger = LoggerFactory.getLogger(RentalController.class);
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TokenComponent tokenComponent;

    /*
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationResponse> registerNotification(@RequestBody NotificationRequest request)
    {
        try
        {
            NotificationResponse notification = notificationService.registerNotification(request);
            if (notification == null)
            {
                return new ResponseEntity("Error: Notification can not be found ", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(notification, HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot create a new notification ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     */

    @GetMapping("/get-my/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable("userId") long userId)
    {
        logger.info("Fetching all notifications for for a user...");

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<NotificationResponse> notifications = notificationService.getAllMy(userId);
            if (notifications == null || notifications.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/update/{notificationId}")
    public ResponseEntity<NotificationResponse> updateNotificationIsPressed(@PathVariable("notificationId") long notificationId)
    {
        try
        {
            NotificationResponse notificationResponse = notificationService.updateNotificationIsPressed(notificationId);
            if (notificationResponse == null)
            {
                return new ResponseEntity("Can not find notification ", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(notificationResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
