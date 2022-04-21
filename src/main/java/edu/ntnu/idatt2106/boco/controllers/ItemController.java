package edu.ntnu.idatt2106.boco.controllers;


import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
import edu.ntnu.idatt2106.boco.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a ItemController
 */

@RestController
@RequestMapping(value = "/item")
@EnableAutoConfiguration
@CrossOrigin
public class ItemController {

    @Autowired
    private ItemService itemService;

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * A method for creating an item-post when logged in as a user
     * @param itemRegisterRequest The item-post request that is being stored
     * @return returns a status int
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   // @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> createItem(@RequestBody ItemRegisterRequest itemRegisterRequest) {
       try {
           if (itemService.createItem(itemRegisterRequest) == 0) {
               return new ResponseEntity("Error: User can not be found ", HttpStatus.NO_CONTENT);
           }
           return new ResponseEntity("A new item has been added successfully ", HttpStatus.CREATED);
       }catch(Exception e){
           return new ResponseEntity("Error: Cannot create a new item ",HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * A method for retrieving all items posts that is stored in database
     * @return Returns a list of items
     */

    @GetMapping("")
    public ResponseEntity<List> getAllItems() {
        logger.info("Fetching all all items...");
        try {
            if (itemService.getAllItems().isEmpty()) {
                return new ResponseEntity(0, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(itemService.getAllItems(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for retrieving all items that is registered on a user
     * method for MyItems page frontend
     * @return returns a List of Items
     */

    @GetMapping("{userId}")
    public ResponseEntity<List> getMyItems(@PathVariable("userId") int userId) {
        logger.info("Hei, jeg har kommet meg inn i get equation metoden");
        try {
            if (itemService.getMyItems(userId).isEmpty()) {
                return new ResponseEntity(0, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(itemService.getMyItems(userId), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * A method for updating a specific item on itemId
     * overwrites the old data in database
     * Edit item in frontend
     * @param itemId the item that is being updated
     * @param itemRegisterRequest the data that is renewed
     * @return returns the updated Item
     */

    @PutMapping(value = "/updateItem/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   // @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> updateSpecificItem(@PathVariable("itemId") int itemId, @RequestBody ItemRegisterRequest itemRegisterRequest) {
        logger.info("Prøver å oppdatere en spesifikk item annonse på itemId");
        try {
           if(!itemService.updateSpecificItem(itemId, itemRegisterRequest)){
               return new ResponseEntity("Can not found item ",HttpStatus.NOT_FOUND);
           }
           return new ResponseEntity("Item has been updated",HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity("Error: Can not update",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for deleting a specific item in database
     * DeleteMapping HTTP annotation
     * @param itemId the item that is being deleted
     * @return returns a status int
     */

    @DeleteMapping(value = "/deleteItem/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
   // @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> deleteSpecificItem(@PathVariable("itemId") int itemId) {
        logger.info("Prøver å slette et spesifikt item på itemId");
        try {
            if (itemService.deleteSpecificItem(itemId) == 0) {
                return new ResponseEntity("Item can not be found ", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity("Item has been deleted successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error: Can not delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
