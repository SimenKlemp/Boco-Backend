package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.payload.request.RegisterRentalRequest;
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

@RestController
@RequestMapping(value = "/rental")
@EnableAutoConfiguration
@CrossOrigin
public class RentalController
{
    @Autowired
    private RentalService rentalService;

    Logger logger = LoggerFactory.getLogger(RentalController.class);

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
            return new ResponseEntity("Error: Cannot create a new rental ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
            return new ResponseEntity("Could not fetch all rentals", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
