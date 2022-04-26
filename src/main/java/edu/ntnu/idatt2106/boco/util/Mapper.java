package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper
{
    public static UserResponse ToUserResponse(User user)
    {
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.isPerson(),
                user.getStreetAddress(),
                user.getPostalCode(),
                user.getPostOffice(),
                user.getEmail(),
                user.getRole(),
                user.getImage() != null ? user.getImage().getImageId() : -1
        );
    }

    public static List<UserResponse> ToUserResponses(List<User> users)
    {
        return users.stream().map(Mapper::ToUserResponse).collect(Collectors.toList());
    }

    public static ItemResponse ToItemResponse(Item item)
    {
        return new ItemResponse(
                item.getItemId(),
                item.getStreetAddress(),
                item.getPostalCode(),
                item.getPostOffice(),
                item.getPrice(),
                item.getDescription(),
                item.getCategory(),
                item.getTitle(),
                item.getImage() != null ? item.getImage().getImageId() : -1,
                ToUserResponse(item.getUser()),
                item.getPublicityDate()
        );
    }

    public static List<ItemResponse> ToItemResponses(List<Item> items)
    {
        return items.stream().map(Mapper::ToItemResponse).collect(Collectors.toList());
    }

    public static RentalResponse ToRentalResponse(Rental rental)
    {
        return new RentalResponse(
                rental.getRentalId(),
                rental.getMessage(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getStatus(),
                ToUserResponse(rental.getUser()),
                ToItemResponse(rental.getItem()),
                rental.getDeliveryInfo()
        );
    }

    public static List<RentalResponse> ToRentalResponses(List<Rental> rentals)
    {
        return rentals.stream().map(Mapper::ToRentalResponse).collect(Collectors.toList());
    }

    public static FeedbackWebPageResponse ToFeedbackWebPageResponse(FeedbackWebPage feedbackWebPage) {
        return new FeedbackWebPageResponse(
                feedbackWebPage.getFeedbackId(),
                feedbackWebPage.getMessage(),
                ToUserResponse(feedbackWebPage.getUser())
        );

    }
    public static List<FeedbackWebPageResponse> ToFeedbackWebPageResponses(List<FeedbackWebPage> feedbacks)
    {
        return feedbacks.stream().map(Mapper::ToFeedbackWebPageResponse).collect(Collectors.toList());
    }


}
