package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.service.ItemService;
import edu.ntnu.idatt2106.boco.service.NotificationService;
import edu.ntnu.idatt2106.boco.service.RentalService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class that represents a RentalController endpoint
 */
@RestController
@RequestMapping(value = "/rental")
@EnableAutoConfiguration
@CrossOrigin
public class RentalController
{
    Logger logger = LoggerFactory.getLogger(RentalController.class);
    @Autowired
    private RentalService rentalService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TokenComponent tokenComponent;

    /**
     * A method for creating a rental request
     * status is set later, when owner of item is responding to the request
     *
     * @param request the rental request that is beeing stored
     * @return returns a status int
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> registerRental(@RequestBody RegisterRentalRequest request)
    {
        logger.info("Posting/storing a rentalRequest for ItemId '" + request.getItemId() + "' to Rental table");
        try
        {
            if (!tokenComponent.haveAccessTo(request.getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            RentalResponse rental = rentalService.register(request);

            notificationService.register("REQUEST", rental.getRentalId());

            if (rental == null)
            {
                return new ResponseEntity("Error: Cannot find User or Item", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rental, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot create a new rental ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for accepting a rental
     *
     * @param rentalId the rentalId that is being updated
     * @return returns the updated rental object
     */
    @PutMapping(value = "/accept/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> acceptRental(@PathVariable("rentalId") long rentalId)
    {
        logger.info("updating a rental based on rentalId");
        try
        {
            RentalResponse rental = rentalService.getRental(rentalId);
            if (!tokenComponent.haveAccessTo(rental.getItem().getUser().getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            rental = rentalService.accept(rentalId);
            notificationService.register("ACCEPTED", rentalId);
            if (rental == null)
            {
                return new ResponseEntity("Error: Cannot find User or Item", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rental, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot update rental ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for rejecting a rental
     *
     * @param rentalId the rentalId that is being updated
     * @return returns the updated rental object
     */
    @PutMapping(value = "/reject/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> rejectRental(@PathVariable("rentalId") long rentalId)
    {
        logger.info("updating a rental based on rentalId");
        try
        {
            RentalResponse rental = rentalService.getRental(rentalId);
            if (!tokenComponent.haveAccessTo(rental.getItem().getUser().getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            rental = rentalService.reject(rentalId);
            notificationService.register("REJECTED", rentalId);
            if (rental == null)
            {
                return new ResponseEntity("Error: Cannot find User or Item", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rental, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot update rental ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for rejecting a rental
     *
     * @param rentalId the rentalId that is being updated
     * @return returns the updated rental object
     */
    @PutMapping(value = "/cancel/{rentalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> cancelRental(@PathVariable("rentalId") long rentalId)
    {
        logger.info("updating a rental based on rentalId");
        try
        {
            RentalResponse rental = rentalService.getRental(rentalId);
            if (!tokenComponent.haveAccessTo(rental.getUser().getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            rental = rentalService.cancel(rentalId);
            notificationService.register("CANCELED", rentalId);
            if (rental == null)
            {
                return new ResponseEntity("Error: Cannot find User or Item", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rental, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot update rental ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for retrieving all rental requests on a specific itemId
     *
     * @param itemId the itemId the rental requests belongs to
     * @return returns a list of rental requests for a specific itemId
     */
    @GetMapping("/for-item/{itemId}")
    public ResponseEntity<List<RentalResponse>> getAllRentalsForItem(@PathVariable("itemId") long itemId)
    {
        logger.info("Fetching all rentals for itemId=" + itemId);
        try
        {
            ItemResponse item = itemService.getItem(itemId);
            if (item == null)
            {
                return new ResponseEntity("Item can not be found ", HttpStatus.NOT_FOUND);
            }
            if (!tokenComponent.haveAccessTo(item.getUser().getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RentalResponse> rentals = rentalService.getAllForItem(itemId);
            //logger.info(rentals.get(0).getStartDate() + " " + rentals.get(0).getEndDate());
            if (rentals == null || rentals.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Could not fetch all rentals", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-my/{userId}/{status}")
    public ResponseEntity<List<RentalResponse>> getAllRentalsWhereUser(@PathVariable("userId") long userId, @PathVariable("status") Rental.Status status)
    {
        logger.info("Fetching all rentals for user " + userId + " and status " + status);

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RentalResponse> rentals = rentalService.getAllWhereUser(userId, status);
            if (rentals == null || rentals.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-my-owner/{userId}/{status}")
    public ResponseEntity<List<RentalResponse>> getAllRentalsOwner(@PathVariable("userId") long userId, @PathVariable("status") Rental.Status status)
    {
        logger.info("Fetching all rentals for user " + userId);

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<RentalResponse> rentals = rentalService.getAllWhereOwner(userId, status);
            if (rentals == null || rentals.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
