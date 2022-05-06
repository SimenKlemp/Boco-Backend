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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    RentalService rentalService;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    ItemController itemController;

    /**
     * A method for posting an item to database
     *
     * @param request the item that is being stored
     * @return returns an ItemResponse
     */
    public ItemResponse register(RegisterItemRequest request) throws Exception
    {
        double[] latLng = itemController.getGeocodeGoogle(request.getStreetAddress());

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
                (float) latLng[0],
                (float) latLng[1],
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
     *
     * @param page     the page nr
     * @param pageSize number of items per page
     * @return returns an item List
     */
    public List<ItemResponse> getAll(int page, int pageSize)
    {
        Sort sort = Sort.by("publicityDate").descending();
        PageRequest pageRequest = PageRequest.of(page, pageSize).withSort(sort);
        List<Item> items = itemRepository.findAll(pageRequest).getContent();
        return Mapper.ToItemResponses(items);
    }


    /**
     * A method for retrieving all items to a specific user on userId
     *
     * @param userId the userId the items belongs to
     * @return returns a list of items as a itemResponse
     */
    public List<ItemResponse> getAllMy(long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;
        List<Item> items = itemRepository.findAllByUser(optionalUser.get());
        return Mapper.ToItemResponses(items);
    }

    /**
     * A method for updating a specific Item on itemId
     * Finds the item from database and then assigns new values to the columns
     *
     * @param itemId  the itemId that that is being updated
     * @param request the data that is being renewed
     * @return returns the updated Item as an ItemResponse
     */
    public ItemResponse update(long itemId, UpdateItemRequest request) throws Exception
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) return null;
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

        double[] latLng = itemController.getGeocodeGoogle(item.getStreetAddress());

        item.setLat((float) latLng[0]);
        item.setLng((float) latLng[1]);

        if (request.getImageId() != null)
        {
            Image prevImage = item.getImage();

            Optional<Image> optionalImage = imageRepository.findById(request.getImageId());
            if (optionalImage.isPresent())
            {
                Image image = optionalImage.get();
                item.setImage(image);
                item = itemRepository.save(item);

                if (prevImage != null && !Objects.equals(image.getImageId(), prevImage.getImageId()))
                {
                    imageRepository.delete(prevImage);
                }
            }
        }

        item = itemRepository.save(item);
        return Mapper.ToItemResponse(item);
    }

    /**
     * A method for retrieving all items fulfilling search demands
     *
     * @param request The search data
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
        if (optionalItem.isEmpty()) return null;
        return Mapper.ToItemResponse(optionalItem.get());
    }

    public List<LocalDate> getAllOccupiedDatesForItem(Long itemId)
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) return null;
        Item item = optionalItem.get();

        List<Rental> rentals = rentalRepository.findALlByItemAndEndDateAfter(item, new Date());
        Set<LocalDate> occupiedDatesSet = new HashSet<>();

        LocalDate today = new Date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        for (Rental rental : rentals)
        {
            LocalDate startDate = rental.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate endDate = rental.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (startDate.isBefore(today)) startDate = today;
            endDate = endDate.plusDays(1);

            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
            {
                occupiedDatesSet.add(date);
            }
        }

        List<LocalDate> occupiedDates = new ArrayList<>(occupiedDatesSet);

        occupiedDates.sort(LocalDate::compareTo);

        return occupiedDates;
    }

    /**
     * A method for deleting a specific item in database
     *
     * @param itemId the item that is being deleted
     * @return returns a status boolean
     */
    public boolean delete(Long itemId)
    {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) return false;
        Item item = optionalItem.get();

        for (Rental rental : rentalRepository.findAllByItem(item))
        {
            rentalService.delete(rental.getRentalId());
        }

        Image image = item.getImage();

        itemRepository.delete(optionalItem.get());

        if (image != null)
        {
            imageService.delete(item.getImage().getImageId());
        }

        return true;
    }
}
