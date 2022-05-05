package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Random;

public abstract class ModelFactory
{
    public static User getUser(Image image)
    {
        return new User(
                "name",
                true,
                "streetAddress",
                "postalCode",
                "postOffice",
                "email" + new Random().nextInt() + "@email.com",
                new BCryptPasswordEncoder().encode("password"),
                "USER",
                image
        );
    }

    public static Image getImage()
    {
        return new Image(
                "name",
                new byte[5]
        );
    }

    public static Item getItem(Image image, User user)
    {
        return new Item(
                "streetAddress",
                "postalCode",
                "postOffice",
                1.0000f,
                2.0000f,
                100f,
                "description",
                "category",
                "title",
                new Date(),
                true,
                true,
                image,
                user
        );
    }

    public static Rental getRental(User user, Item item)
    {
        return new Rental(
                new Date(),
                new Date(),
                Rental.Status.PENDING,
                user,
                item,
                Rental.DeliverInfo.PICKUP
        );
    }

    public static FeedbackWebPage getFeedbackWebPage(User user)
    {
        return new FeedbackWebPage(
                "message",
                user
        );
    }

    public static Message getMessage(User user, Rental rental)
    {
        return new Message(
                "text",
                true,
                new Date(),
                user,
                rental
        );
    }

    public static Notification getNotification(Rental rental, User user)
    {
        return new Notification(
                "notificationStatus",
                false,
                rental,
                user
        );
    }

    public static Rating getRating(Rental rental, User user)
    {
        return new Rating(
                5,
                "feedback",
                rental,
                user
        );
    }
}
