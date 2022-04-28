package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

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
                "email",
                new BCryptPasswordEncoder().encode("password"),
                "USER",
                image
        );
    }

    public static Image getImage()
    {
        return new Image(
                "name",
                null
        );
    }

    public static Item getItem(Image image, User user)
    {
        return new Item(
                "streetAddress",
                "postalCode",
                "postOffice",
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
                "message",
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
}
