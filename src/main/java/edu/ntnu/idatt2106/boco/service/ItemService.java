package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.ItemRegisterRequest;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents an ItemService
 */

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;


    /**
     * A method for creating an Item
     * finds the user that is creating an item
     * @param request
     * @return returns a status int
     */

    public int createItem(ItemRegisterRequest request){
        User user = userRepository.findById(request.getUserId()).get();
        Item item = new Item(request.getAddress(), request.getPrice(), request.getDescription(), request.getCategory(), request.getTitle(), user, request.getImageId());
        itemRepository.save(item);

        return 0;

    }

    /**
     * A method for retrieving all the items that is stored in database
     * @return returns an item List
     */

    public List getAllItems(){
        List<Item> items = new ArrayList<Item>();

        itemRepository.findAll().forEach(items::add);

        return items;
    }


    /**
     * A method for retrieving all items to a specific user on userId
     * @param userId the userId the items belongs to
     * @return returns a list of items
     */
    public List getMyItems(int userId){
        List<Item> items = new ArrayList<Item>();

        itemRepository.findAllByUser(userId).forEach(items::add);

        return items;
    }

    /**
     * A method for updating a specific Item on itemId
     * Finds the item from database and then assigns new values to the columns
     * @param itemId
     * @param itemRegisterRequest
     * @return returns the updated Item
     */
    public Item updateSpecificItem(int itemId, ItemRegisterRequest itemRegisterRequest){
        Item item = itemRepository.findByUser(itemId);
        item.setAddress(itemRegisterRequest.getAddress());
        item.setPrice(itemRegisterRequest.getPrice());
        item.setDescription(itemRegisterRequest.getDescription());
        item.setCategory(itemRegisterRequest.getCategory());
        item.setTitle(itemRegisterRequest.getTitle());
        item.setImageid(itemRegisterRequest.getImageId());

        return itemRepository.save(item);
    }







}
