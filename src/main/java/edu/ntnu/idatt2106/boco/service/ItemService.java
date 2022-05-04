package edu.ntnu.idatt2106.boco.service;

import edu.ntnu.idatt2106.boco.controllers.ItemController;
import edu.ntnu.idatt2106.boco.models.Image;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.SearchRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateItemRequest;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.repository.ImageRepository;
import edu.ntnu.idatt2106.boco.repository.ItemRepository;
import edu.ntnu.idatt2106.boco.repository.RentalRepository;
import edu.ntnu.idatt2106.boco.repository.UserRepository;
import edu.ntnu.idatt2106.boco.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A class that represents an ItemService
 */

@Service
public class ItemService
{
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    ItemController itemController;

    /**
     * A method for creating an Item
     * finds the user that is creating an item
     * @param request
     * @return returns a status int
     */
    public ItemResponse registerItem(RegisterItemRequest request) throws Exception {


        String[] latLng = itemController.getGeocodeGoogle(request.getStreetAddress()).split(",");

        System.out.println(latLng);

        String lat = latLng[0].replace("\"", "");
        String lng = latLng[1].replace("\"", "");

        System.out.println(lat);
        System.out.println(lng);

        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        Image image = null;
        if (request.getImageId() != null)
        {
            Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
            if (optionalImage.isPresent())
            {
                image = optionalImage.get();
            }
        }

        Date currentDate = new Date();

        Item item = new Item(
                request.getStreetAddress(),
                request.getPostalCode(),
                request.getPostOffice(),
                Float.parseFloat(lat),
                Float.parseFloat(lng),
                request.getPrice(),
                request.getDescription(),
                request.getCategory(),
                request.getTitle(),
                currentDate,
                request.getIsPickupable(),
                request.getIsDeliverable(),
                image,
                user
        );
        item = itemRepository.save(item);
        return Mapper.ToItemResponse(item);
    }

    /**
     * A method for retrieving all the items that is stored in database
     * @param page the page nr
     * @param pageSize number of items per page
     * @return returns an item List
     */
    public List<ItemResponse> getAllItems(int page, int pageSize)
    {
        Sort sort = Sort.by("publicityDate").descending();
        PageRequest pageRequest = PageRequest.of(page, pageSize).withSort(sort);
        List<Item> items = itemRepository.findAll(pageRequest).getContent();
        return Mapper.ToItemResponses(items);
    }


    /**
     * A method for retrieving all items to a specific user on userId
     * @param userId the userId the items belongs to
     * @return returns a list of items
     */
    public List<ItemResponse> getMyItems(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        List<Item> items = itemRepository.findAllByUser(optionalUser.get());
        return Mapper.ToItemResponses(items);
    }

    /**
     * A method for updating a specific Item on itemId
     * Finds the item from database and then assigns new values to the columns
     * @param itemId
     * @param request
     * @return returns the updated Item
     */
    public ItemResponse updateItem(long itemId, UpdateItemRequest request)
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty()) return null;
        Item item = optionalItem.get();

        if (request.getStreetAddress() != null) item.setStreetAddress(request.getStreetAddress());
        if (request.getPostalCode() != null) item.setPostalCode(request.getPostalCode());
        if (request.getPostOffice() != null) item.setPostOffice(request.getPostOffice());
        if (request.getPrice() != null) item.setPrice(request.getPrice());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getCategory() != null) item.setCategory(request.getCategory());
        if (request.getTitle() != null) item.setTitle(request.getTitle());
        if (request.getIsPickupable() != null) item.setIsPickupable(request.getIsPickupable());
        if (request.getIsDeliverable() != null) item.setIsDeliverable(request.getIsDeliverable());

        if (request.getImageId() != null)
        {
            Image prevImage = item.getImage();

            Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
            if (optionalImage.isPresent()) item.setImage(optionalImage.get());

            if (prevImage != null && !Objects.equals(request.getImageId(), prevImage.getImageId()))
            {
                imageRepository.delete(prevImage);
            }
        }

        item = itemRepository.save(item);
        return Mapper.ToItemResponse(item);
    }

    /**
     * A method for deleting a specific item in database
     * @param itemId the item that is being deleted
     * @return returns a status int
     */
    public boolean deleteItem(long itemId)
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty()) return false;
        Item item = optionalItem.get();

        List<Rental> rentals = rentalRepository.findAllByItem(item);
        rentalRepository.deleteAll(rentals);

        itemRepository.delete(optionalItem.get());
        return true;
    }

    /**
     * A method for retrieving all items connected to a search
     * @param request what is being searched for
     * @return returns a list of Items belonging to a search
     */
    public List<ItemResponse> search(SearchRequest request)
    {
        List<Item> items = itemRepository.search(request);
        return Mapper.ToItemResponses(items);
    }

    public ItemResponse getItem(long itemId)
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty()) return null;
        return Mapper.ToItemResponse(optionalItem.get());
    }
}
