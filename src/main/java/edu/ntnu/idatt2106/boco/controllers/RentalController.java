package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateRentalRequest;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.service.RentalService;
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
    @Autowired
    private RentalService rentalService;

    Logger logger = LoggerFactory.getLogger(RentalController.class);


    /**
     * A method for creating a rental request
     * status is set later, when owner of item is responding to the request
     * @param request the rental request that is beeing stored
     * @return returns a status int
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> registerRental(@RequestBody RegisterRentalRequest request)
    {
        logger.info("Posting/storing a rentalRequest with ItemId '" + request.getItemId() + "' to Rental table");
        try
        {
            RentalResponse rental = rentalService.registerRental(request);
            if (rental == null)
            {
                return new ResponseEntity("Error: Cannot find User or Item", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rental, HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot create a new rental ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for updating a rental status
     * @param rentalId the rentalId that is beeing updated
     * @param request the rental request that is renewed
     * @return returns the updated rental object
     */
    @PutMapping(value = "/updateRentalRequest/{rentalId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<RentalResponse> updateRentalRequest(@PathVariable("rentalId") long rentalId, @RequestBody UpdateRentalRequest request)
    {
        logger.info("updating a rentalRequest based on rentalId");
        try
        {
            RentalResponse rental = rentalService.updateRental(rentalId, request);
            if (rental == null)
            {
                return new ResponseEntity("Error: Cannot find User or Item", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rental, HttpStatus.OK);
        }
        catch(Exception e)
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
            List<RentalResponse> rentals = rentalService.getAllRentalsForItem(itemId);
            if (rentals == null || rentals.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Could not fetch all rentals", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
