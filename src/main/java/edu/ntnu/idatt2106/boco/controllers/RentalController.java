package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
import edu.ntnu.idatt2106.boco.payload.request.RentalRequest;
import edu.ntnu.idatt2106.boco.service.ItemService;
import edu.ntnu.idatt2106.boco.service.RentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rental")
@EnableAutoConfiguration
@CrossOrigin
public class RentalController {

    @Autowired
    private RentalService rentalService;

    Logger logger = LoggerFactory.getLogger(RentalController.class);


    @PostMapping(value = "createRental", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public int createRental(@RequestBody RentalRequest rentalRequest) {
        logger.info("Posting/storing a rentalRequest with ItemId '" + rentalRequest.getItemId() + "' to Rental table");
        return rentalService.createRental(rentalRequest);
    }

    @GetMapping("{itemId}")
    public List<Rental> getAllRentalRequestSpecificItem(@PathVariable("itemId") Long itemId) {
        logger.info("Fetching all all rentalRequests for an item...");

        return rentalService.getAllRentalRequestSpecificItem(itemId);

    }

    @PutMapping(value = "/updateRentalRequest/{rentalId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Rental updateRentalRequest(@PathVariable("rentalId") int rentalId, @RequestBody RentalRequest rentalRequest) {
        logger.info("updating a rentalRequest based on rentalId");
        return rentalService.updateRentalRequest(rentalId, rentalRequest);
    }


}
