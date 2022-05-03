package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.*;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.repository.*;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    ChatService chatService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;


    /**
     * A method for creating a rental request
     * status is set later, when owner of item is responding to the request
     * @param request the rental request that is being stored to database
     * @return returns a status int
     * */
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

    /**
     * A method for retrieving all rental requests for a specific item
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

    public List<RentalResponse> getAllForUser(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        List<Rental> rentals = rentalRepository.findAllByUser(optionalUser.get());
        return Mapper.ToRentalResponses(rentals);
    }


    /**
     * A method for accepting a rental request based on rentalId
     * @param rentalId the rentalId that is being updated
     * @return returns the renewed Rental object
     */
    public RentalResponse accept(long rentalId)
    {
        return updateStatus(rentalId, Rental.Status.ACCEPTED);
    }

    /**
     * A method for rejecting a rental request based on rentalId
     * @param rentalId the rentalId that is being updated
     * @return returns the renewed Rental object
     */
    public RentalResponse reject(long rentalId)
    {
        return updateStatus(rentalId, Rental.Status.REJECTED);
    }

    /**
     * A method for canceling a rental request based on rentalId
     * @param rentalId the rentalId that is being updated
     * @return returns the renewed Rental object
     */
    public RentalResponse cancel(long rentalId)
    {
        return updateStatus(rentalId, Rental.Status.CANCELED);
    }

    /**
     * A method for updating a rental request based on rentalId
     * @param rentalId the rentalId that is being updated
     * @param status the new status
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

    public RentalResponse getRental(long rentalId)
    {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        return Mapper.ToRentalResponse(optionalRental.get());
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
            chatService.delete(message.getMessageId());
        }

        rentalRepository.delete(rental);
        return true;
    }
}
