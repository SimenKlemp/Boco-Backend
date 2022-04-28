package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.FeedbackWebPage;
import edu.ntnu.idatt2106.boco.models.Item;
import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.response.FeedbackWebPageResponse;
import edu.ntnu.idatt2106.boco.payload.response.ItemResponse;
import edu.ntnu.idatt2106.boco.payload.response.RentalResponse;
import edu.ntnu.idatt2106.boco.payload.response.UserResponse;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper
{
    public static UserResponse ToUserResponse(User user)
    {
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getIsPerson(),
                user.getStreetAddress(),
                user.getPostalCode(),
                user.getPostOffice(),
                user.getEmail(),
                user.getRole(),
                user.getImage() != null ? user.getImage().getImageId() : null
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
                item.getImage() != null ? item.getImage().getImageId() : null,
                item.getPublicityDate(),
                item.getIsPickupable(),
                item.getIsDeliverable(),
                ToUserResponse(item.getUser())
        );
    }

    public static List<ItemResponse> ToItemResponses(List<Item> items)
    {
        return items.stream().map(Mapper::ToItemResponse).collect(Collectors.toList());
    }

    public static RentalResponse ToRentalResponse(Rental rental)
    {
        String status = rental.getStatus().toString();
        Date today = new Date();

        if (rental.getStatus() == Rental.Status.ACCEPTED)
        {
            if (rental.getStartDate().before(today) && rental.getEndDate().after(today))
            {
                status = "ACTIVE";
            }
            else if (rental.getEndDate().before(today))
            {
                status = "FINISHED";
            }
        }

        return new RentalResponse(
                rental.getRentalId(),
                rental.getMessage(),
                rental.getStartDate(),
                rental.getEndDate(),
                status,
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
