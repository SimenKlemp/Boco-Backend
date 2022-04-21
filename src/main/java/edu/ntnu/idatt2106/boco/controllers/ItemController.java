package edu.ntnu.idatt2106.boco.controllers;


import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
import edu.ntnu.idatt2106.boco.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping(value = "/item")
@EnableAutoConfiguration
@CrossOrigin
public class ItemController {

    @Autowired
    private ItemService itemService;

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public int createItem(@RequestBody ItemRegisterRequest itemRegisterRequest) {
        logger.info("Posting/storing an item with the title '" + itemRegisterRequest.getTitle() + "' to the database");
        return itemService.createItem(itemRegisterRequest);
    }

    /**
     * A method for fetching all items
     * @return
     */
    @GetMapping("/all")
    public List getAllItems() {
        logger.info("Fetching all all items...");

        return itemService.getAllItems();
    }

    /**
     * A method for retrieving all items to a user
     * @return
     */
    @GetMapping("{userId}")
    public List getMyItems(@PathVariable("userId") long userId) {
        logger.info("Fetching items to a user with userId: " + userId + "...");

        return itemService.getMyItems(userId);
    }

}
