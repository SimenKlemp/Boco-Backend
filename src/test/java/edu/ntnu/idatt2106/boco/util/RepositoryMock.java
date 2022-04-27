package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.repository.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .save(Mockito.any(User.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(Mockito.any(User.class));

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
                .save(Mockito.any(Image.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(Mockito.any(Image.class));
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
                .save(Mockito.any(Item.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(Mockito.any(Item.class));

        Mockito.lenient().when(repository.findAllByUser(Mockito.any(User.class)))
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
                .save(Mockito.any(Rental.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(Mockito.any(Rental.class));
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
                .save(Mockito.any(FeedbackWebPage.class));

        Mockito.lenient()
                .doAnswer(i ->
                        list.remove(i.getArguments()[0])
                )
                .when(repository)
                .delete(Mockito.any(FeedbackWebPage.class));
    }
}
