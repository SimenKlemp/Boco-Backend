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
        logger.info("Prøver å opprette en item- annonse i controller");
        return itemService.createItem(itemRegisterRequest);
    }

    @GetMapping("")
    public List getAllItems() {
        logger.info("Hei, jeg har kommet meg inn i get getAllItems metoden");

        return itemService.getAllItems();

    }

    /**
     * A method for retrieving all items a user own
     * @return
     */
    /*
    @GetMapping("{userId}")
    public List getMyItems(@PathVariable("userId") int userId) {
        logger.info("Hei, jeg har kommet meg inn i get equation metoden");

        return itemService.getMyItems(userId);
    }

     */
}
