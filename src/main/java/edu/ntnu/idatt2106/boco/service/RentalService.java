package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
import edu.ntnu.idatt2106.boco.payload.request.RentalRequest;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a RentalService
 */

@Service
public class RentalService {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * A method for creating a rental request
     * status is set later, when owner of item is responding to the request
     * @param rentalRequest the rental request that is being stored to database
     * @return returns a status int
     */

    public int createRental(RentalRequest rentalRequest){
        User user = userRepository.findById(rentalRequest.getUserId()).get();
        Rental rental = new Rental(rentalRequest.getMessage(), rentalRequest.getStartDate(), rentalRequest.getEndDate(), "PENDING", user, rentalRequest.getItemId());
        rentalRepository.save(rental);

        return 0;

    }

    /**
     * A method for retrieving all rental requests for a specific item
     * @param itemId the itemId the rentalRequests belongs to
     * @return returns a list of rentalRequests of an item
     */

    public List<Rental> getAllRentalRequestSpecificItem(Long itemId){
        List<Rental> rentalRequests = new ArrayList<Rental>();

        rentalRepository.findAllByItemId(itemId).forEach(rentalRequests::add);

        return rentalRequests;
    }

    /**
     * A method for updating a rental request based on rentalId
     * @param rentalId the rentalId that is being updated
     * @param rentalRequest the renewed rentalRequest data
     * @return returns the renewed Rental object 
     */

    public Rental updateRentalRequest(long rentalId, RentalRequest rentalRequest){
        Rental rental = rentalRepository.findById(rentalId).get();
        rental.setStatus(rentalRequest.getStatus());

        return rentalRepository.save(rental);
    }
}
