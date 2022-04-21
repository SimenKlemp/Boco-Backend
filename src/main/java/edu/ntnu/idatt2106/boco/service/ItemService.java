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
import java.util.Collections;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;



    public int createItem(ItemRegisterRequest request){
        User user = userRepository.findById(request.getUserId()).get();
        Item item = new Item(request.getAddress(), request.getPrice(), request.getDescription(), request.getCategory(), request.getTitle(), user, request.getImageId());
        itemRepository.save(item);

        return 0;

    }

    public List getAllItems(){
        List<Item> items = new ArrayList<Item>();

        itemRepository.findAll().forEach(items::add);

        return items;
    }

    public List getMyItems(long userId){
        List<Item> items = new ArrayList<Item>();

        itemRepository.findItemsByUser(userId).forEach(items::add);

        return items;
    }

}
