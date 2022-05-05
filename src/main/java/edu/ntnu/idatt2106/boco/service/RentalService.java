package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class that represents a RentalService
 */
@Service
public class RentalService
{
    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    /**
     * A method for creating a rental request
     * status is set later, when owner of item is responding to the request
     *
     * @param request the rental request that is being stored to database
     * @return returns a status int
     */
    public RentalResponse register(RegisterRentalRequest request)
    {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        Optional<Item> optionalItem = itemRepository.findById(request.getItemId());
        if (optionalItem.isEmpty()) return null;
        Item item = optionalItem.get();

        if (request.getDeliveryInfo() == Rental.DeliverInfo.PICKUP && !item.getIsPickupable()) return null;
        if (request.getDeliveryInfo() == Rental.DeliverInfo.DELIVERED && !item.getIsDeliverable()) return null;

        Rental rental = new Rental(
                request.getStartDate(),
                request.getEndDate(),
                Rental.Status.PENDING,
                user,
                item,
                request.getDeliveryInfo()
        );
        rental = rentalRepository.save(rental);

        Message message = new Message(
                request.getMessage(),
                true,
                new Date(),
                user,
                rental
        );
        messageRepository.save(message);

        return Mapper.ToRentalResponse(rental);
    }

    public RentalResponse getRental(long rentalId)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        return Mapper.ToRentalResponse(rental);
    }

    /**
     * A method for retrieving all rental requests for a specific item
     *
     * @param itemId the itemId the rentalRequests belongs to
     * @return returns a list of rentalRequests of an item
     */
    public List<RentalResponse> getAllForItem(long itemId)
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) return null;
        Item item = optionalItem.get();

        List<Rental> rentals = rentalRepository.findAllByItem(item);
        return Mapper.ToRentalResponses(rentals);
    }

    public List<RentalResponse> getAllWhereUser(long userId, Rental.Status status)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        List<Rental> rentals = rentalRepository.findAllByUser(user);

        return filterByStatus(status, rentals);
    }

    private List<RentalResponse> filterByStatus(Rental.Status status, List<Rental> rentals)
    {
        if (status == Rental.Status.CANCELED || status == Rental.Status.REJECTED)
        {
            rentals = rentals.stream().filter(r -> {
                Rental.Status rentalStatus = r.getStatus();
                boolean rejectedOrCanceled = rentalStatus == Rental.Status.REJECTED || rentalStatus == Rental.Status.CANCELED;
                boolean finished = rentalStatus == Rental.Status.ACCEPTED && r.getEndDate().before(new Date());

                return rejectedOrCanceled || finished;
            }).collect(Collectors.toList());
        }
        else
        {
            rentals = rentals.stream().filter(r -> {
                Rental.Status rentalStatus = r.getStatus();
                return rentalStatus == Rental.Status.ACCEPTED || rentalStatus == Rental.Status.PENDING;
            }).collect(Collectors.toList());
        }

        return Mapper.ToRentalResponses(rentals);
    }

    public List<RentalResponse> getAllWhereOwner(long userId, Rental.Status status)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        List<Item> items = itemRepository.findAllByUser(user);

        List<Rental> rentals = new ArrayList<>();
        for (Item item : items)
        {
            rentals.addAll(
                    rentalRepository.findAllByItem(item)
            );
        }

        return filterByStatus(status, rentals);
    }

    /**
     * A method for accepting a rental request based on rentalId
     *
     * @param rentalId the rentalId that is being updated
     * @return returns the renewed Rental object
     */
    public RentalResponse accept(long rentalId)
    {
        return updateStatus(rentalId, Rental.Status.ACCEPTED);
    }

    /**
     * A method for rejecting a rental request based on rentalId
     *
     * @param rentalId the rentalId that is being updated
     * @return returns the renewed Rental object
     */
    public RentalResponse reject(long rentalId)
    {
        return updateStatus(rentalId, Rental.Status.REJECTED);
    }

    /**
     * A method for canceling a rental request based on rentalId
     *
     * @param rentalId the rentalId that is being updated
     * @return returns the renewed Rental object
     */
    public RentalResponse cancel(long rentalId)
    {
        return updateStatus(rentalId, Rental.Status.CANCELED);
    }

    /**
     * A method for updating a rental request based on rentalId
     *
     * @param rentalId the rentalId that is being updated
     * @param status   the new status
     * @return returns the renewed Rental object
     */
    private RentalResponse updateStatus(long rentalId, Rental.Status status)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        rental.setStatus(status);
        rental = rentalRepository.save(rental);
        return Mapper.ToRentalResponse(rental);
    }

    public boolean delete(Long rentalId)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);

        if (optionalRental.isEmpty()) return false;
        Rental rental = optionalRental.get();

        for (Rating rating : ratingRepository.findAllByRental(rental))
        {
            ratingService.delete(rating.getRatingId());
        }

        for (Message message : messageRepository.findAllByRental(rental, Sort.unsorted()))
        {
            messageService.delete(message.getMessageId());
        }

        for (Notification notification : notificationRepository.findAllByRental(rental))
        {
            notificationService.delete(notification.getNotificationId());
        }

        rentalRepository.delete(rental);
        return true;
    }
}
