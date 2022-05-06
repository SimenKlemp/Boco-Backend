package edu.ntnu.idatt2106.boco.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.SearchRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.service.ItemService;
import edu.ntnu.idatt2106.boco.token.TokenComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    Logger logger = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private ItemService itemService;
    @Autowired
    private TokenComponent tokenComponent;

    /**
     * A method for creating an item-post when logged in as a user
     *
     * @param request The item-post request that is being stored
     * @return returns a status int
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> registerItem(@RequestBody RegisterItemRequest request)
    {
        try
        {
            if (!tokenComponent.haveAccessTo(request.getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            ItemResponse item = itemService.register(request);
            if (item == null)
            {
                return new ResponseEntity("Error: User can not be found ", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(item, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Cannot create a new item ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for retrieving all items posts that is stored in database
     *
     * @param page     the page
     * @param pageSize how many items that is retrieved
     * @return Returns a list of items as a ResponseEntity
     */
    @GetMapping("/all/{page}/{pageSize}")
    public ResponseEntity<List<ItemResponse>> getAllItems(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize)
    {
        logger.info("Fetching all items...");
        try
        {
            List<ItemResponse> items = itemService.getAll(page, pageSize);
            if (items == null || items.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Could not fetch all items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for retrieving all items that is registered on a user
     * method for MyItems page frontend
     *
     * @param userId the userId the items belong to
     * @return returns a List of Items as a ResponseEntity
     */
    @GetMapping("/get-my/{userId}")
    public ResponseEntity<List<ItemResponse>> getMyItems(@PathVariable("userId") long userId)
    {
        logger.info("Fetching all items for a user...");

        try
        {
            if (!tokenComponent.haveAccessTo(userId))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            List<ItemResponse> items = itemService.getAllMy(userId);
            if (items == null || items.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * A method for updating a specific item on itemId
     * overwrites the old data in database
     * Edit item in frontend
     *
     * @param itemId  the item that is being updated
     * @param request the data that is renewed
     * @return returns the updated Item
     */
    @PutMapping(value = "/update/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemResponse> updateItem(@PathVariable("itemId") long itemId, @RequestBody UpdateItemRequest request)
    {
        try
        {
            ItemResponse item = itemService.getItem(itemId);

            if (!tokenComponent.haveAccessTo(item.getUser().getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            item = itemService.update(itemId, request);
            if (item == null)
            {
                return new ResponseEntity("Can not find item ", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(item, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error: Can not update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for deleting a specific item in database
     * DeleteMapping HTTP annotation
     *
     * @param itemId the item that is being deleted
     * @return returns a status int
     */

    @DeleteMapping(value = "/delete/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") long itemId)
    {
        try
        {
            ItemResponse item = itemService.getItem(itemId);
            if (item == null)
            {
                return new ResponseEntity<>("Item can not be found ", HttpStatus.NOT_FOUND);
            }
            if (!tokenComponent.haveAccessTo(item.getUser().getUserId()))
            {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

            boolean success = itemService.delete(itemId);
            if (!success)
            {
                return new ResponseEntity<>("Item can not be found ", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Item has been deleted successfully ", HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error: Can not delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for retrieving all items fulfilling search demands
     *
     * @param request The search request
     * @return returns a list of items as a ResponseEntity
     */
    @PutMapping("/search")
    public ResponseEntity<List<ItemResponse>> search(@RequestBody SearchRequest request)
    {
        logger.info("Fetching all items fulfilling search demand ...");
        try
        {
            List<ItemResponse> items = itemService.search(request);

            if (items.isEmpty())
            {
                return new ResponseEntity(0, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity(items, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A method for geocoding an address
     *
     * @param address The address that is being geocoded
     * @return Returns a String of geocoded address
     * @throws Exception The exception that is thrown
     */

    @GetMapping(path = "/geocodeGoogle/{address}")
    public double[] getGeocodeGoogle(@PathVariable("address") String address) throws Exception
    {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyC3ODA_2JmqfmDOMPBV4nJhBgma3gFRSCc")
                .build();
        GeocodingResult[] results = GeocodingApi.geocode(context,
                address).await();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        context.shutdown();

        if (results.length == 0) return new double[]{0, 0};

        LatLng location = results[0].geometry.location;

        return new double[]{location.lat, location.lng};
    }

    @GetMapping("/get-occupied-dates/{itemId}")
    public ResponseEntity<List<LocalDate>> getAllOccupiedDatesForItem(@PathVariable("itemId") long itemId)
    {
        try
        {
            List<LocalDate> occupiedDates = itemService.getAllOccupiedDatesForItem(itemId);
            if (occupiedDates == null || occupiedDates.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(occupiedDates, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
