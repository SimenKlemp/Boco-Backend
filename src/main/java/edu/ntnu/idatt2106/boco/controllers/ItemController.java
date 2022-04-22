package edu.ntnu.idatt2106.boco.controllers;


import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
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

import java.util.List;

/**
 * A class that represents a ItemController
 */

@RestController
@RequestMapping(value = "/item")
@EnableAutoConfiguration
@CrossOrigin
public class ItemController
{
    @Autowired
    private ItemService itemService;

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * A method for creating an item-post when logged in as a user
     * @param request The item-post request that is being stored
     * @return returns a status int
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> registerItem(@RequestBody RegisterItemRequest request)
    {
       try
       {
           ItemResponse item = itemService.registerItem(request);
           if (item == null)
           {
               return new ResponseEntity("Error: User can not be found ", HttpStatus.NO_CONTENT);
           }
           return new ResponseEntity<>(item, HttpStatus.CREATED);
       }
       catch(Exception e)
       {
           return new ResponseEntity("Error: Cannot create a new item ",HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * A method for retrieving all items posts that is stored in database
     * @return Returns a list of items
     */
    @GetMapping("")
    public ResponseEntity<List<ItemResponse>> getAllItems()
    {
        logger.info("Fetching all all items...");
        try
        {
            List<ItemResponse> items = itemService.getAllItems();
            if (items == null || items.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity("Could not fetch all items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for retrieving all items that is registered on a user
     * method for MyItems page frontend
     * @return returns a List of Items
     */
    @GetMapping("/get-my/{userId}")
    public ResponseEntity<List<ItemResponse>> getMyItems(@PathVariable("userId") long userId)
    {
        try
        {
            List<ItemResponse> items = itemService.getMyItems(userId);
            if (items == null || items.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * A method for updating a specific item on itemId
     * overwrites the old data in database
     * Edit item in frontend
     * @param itemId the item that is being updated
     * @param request the data that is renewed
     * @return returns the updated Item
     */
    @PutMapping(value = "/update/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> updateItem(@PathVariable("itemId") long itemId, @RequestBody UpdateItemRequest request)
    {
        logger.info("Prøver å oppdatere en spesifikk item annonse på itemId");
        try
        {
            ItemResponse item = itemService.updateItem(itemId, request);
            if(item == null)
            {
                return new ResponseEntity("Can not find item ", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(item, HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity("Error: Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for deleting a specific item in database
     * DeleteMapping HTTP annotation
     * @param itemId the item that is being deleted
     * @return returns a status int
     */

    @DeleteMapping(value = "/delete/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") long itemId)
    {
        logger.info("Prøver å slette et spesifikt item på itemId");
        try
        {
            boolean success = itemService.deleteItem(itemId);
            if (!success)
            {
                return new ResponseEntity<>("Item can not be found ", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Item has been deleted successfully ", HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Error: Can not delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
