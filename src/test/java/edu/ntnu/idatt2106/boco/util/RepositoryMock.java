package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.repository.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

public abstract class RepositoryMock
{
    public static void mockUserRepository(UserRepository repository)
    {
        List<User> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getUserId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    User user = (User) i.getArguments()[0];
                    if (user.getUserId() == null)
                    {
                        user.setUserId((long) (list.size() + 1));
                        list.add(user);
                    }
                    return user;
                })
                .when(repository)
                .save(any(User.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(User.class));

        Mockito.lenient().when(repository.existsByEmail(Mockito.anyString()))
                .then(i -> list.stream().anyMatch(
                        u -> u.getEmail().equals(i.getArguments()[0])
                ));

        Mockito.lenient().when(repository.findByEmail(Mockito.anyString()))
                .then(i -> list.stream().filter(
                        u -> u.getEmail().equals(i.getArguments()[0])
                ).findFirst());
    }

    public static void mockImageRepository(ImageRepository repository)
    {
        List<Image> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getImageId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    Image image = (Image) i.getArguments()[0];
                    if (image.getImageId() == null)
                    {
                        image.setImageId((long) (list.size() + 1));
                        list.add(image);
                    }
                    return image;
                })
                .when(repository)
                .save(any(Image.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(Image.class));
    }

    public static void mockItemRepository(ItemRepository repository)
    {
        List<Item> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getItemId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    Item item = (Item) i.getArguments()[0];
                    if (item.getItemId() == null)
                    {
                        item.setItemId((long) (list.size() + 1));
                        list.add(item);
                    }
                    return item;
                })
                .when(repository)
                .save(any(Item.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(Item.class));

        Mockito.lenient().when(repository.findAllByUser(any(User.class)))
                .then(i -> list.stream().filter(
                        u -> u.getUser().equals(i.getArguments()[0])
                ).collect(Collectors.toList()));
    }

    public static void mockRentalRepository(RentalRepository repository)
    {
        List<Rental> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getRentalId().equals(i.getArguments()[0])
                ).findFirst());


        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient().when(repository.findAllByItem(any(Item.class)))
                        .then(i->list.stream().filter(
                                u -> u.getItem().equals(i.getArguments()[0])
                        ).collect(Collectors.toList()));

        Mockito.lenient().when(repository.findAllByUser(any(User.class)))
                .then(i->list.stream().filter(
                        u -> u.getUser().equals(i.getArguments()[0])
                ).collect(Collectors.toList()));


        Mockito.lenient()
                .doAnswer(i -> {
                    Rental rental = (Rental) i.getArguments()[0];
                    if (rental.getRentalId() == null)
                    {
                        rental.setRentalId((long) (list.size() + 1));
                        list.add(rental);
                    }
                    return rental;
                })
                .when(repository)
                .save(any(Rental.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(Rental.class));
    }

    public static void mockFeedbackWebPageRepository(FeedbackWebPageRepository repository)
    {
        List<FeedbackWebPage> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getFeedbackId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    FeedbackWebPage feedback = (FeedbackWebPage) i.getArguments()[0];
                    if (feedback.getFeedbackId() == null)
                    {
                        feedback.setFeedbackId((long) (list.size() + 1));
                        list.add(feedback);
                    }
                    return feedback;
                })
                .when(repository)
                .save(any(FeedbackWebPage.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(FeedbackWebPage.class));
    }
    public static void mockRatingRepository(RatingRepository repository)
    {
        List<Rating> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getRatingId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    Rating rating = (Rating) i.getArguments()[0];
                    if (rating.getRatingId() == null)
                    {
                        rating.setRatingId((long) (list.size() + 1));
                        list.add(rating);
                    }
                    return rating;
                })
                .when(repository)
                .save(any(Rating.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(Rating.class));
    }

    public static void mockNotificationRepository(NotificationRepository repository)
    {
        List<Notification> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getNotificationId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    Notification notification = (Notification) i.getArguments()[0];
                    if (notification.getNotificationId() == null)
                    {
                        notification.setNotificationId((long) (list.size() + 1));
                        list.add(notification);
                    }
                    return notification;
                })
                .when(repository)
                .save(any(Notification.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(Notification.class));

    }

    public static void mockMessageRepository(MessageRepository repository)
    {
        List<Message> list = new ArrayList<>();

        Mockito.lenient().when(repository.findById(Mockito.anyLong()))
                .then(i -> list.stream().filter(
                        u -> u.getMessageId().equals(i.getArguments()[0])
                ).findFirst());

        Mockito.lenient().when(repository.findAll())
                .then(i -> list);

        Mockito.lenient()
                .doAnswer(i -> {
                    Message message = (Message) i.getArguments()[0];
                    if (message.getMessageId() == null)
                    {
                        message.setMessageId((long) (list.size() + 1));
                        list.add(message);
                    }
                    return message;
                })
                .when(repository)
                .save(any(Message.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(any(Message.class));

    }
}
