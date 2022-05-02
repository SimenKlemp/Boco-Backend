package edu.ntnu.idatt2106.boco.factories.responseFactroies;

import edu.ntnu.idatt2106.boco.models.User;
import edu.ntnu.idatt2106.boco.payload.request.*;
import edu.ntnu.idatt2106.boco.payload.response.*;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class ResponseFactories {

    Random random =new Random();

    public UserResponse updateUserResponse(UpdateUserRequest request,User user){
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(request.getName())
                .isPerson(request.getIsPerson())
                .streetAddress(request.getStreetAddress())
                .postalCode(request.getPostalCode())
                .postOffice(request.getPostOffice())
                .email(request.getEmail())
                .role(user.getRole())
                .imageId(user.getImage().getImageId())
                .build();
    }

    public ItemResponse itemResponseFactory(RegisterItemRequest request, UserResponse user){
        return ItemResponse.builder()
                .itemId(UUID.randomUUID().getLeastSignificantBits())
                .streetAddress(request.getStreetAddress())
                .postalCode(request.getPostalCode())
                .postOffice(request.getPostOffice())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .title(request.getTitle())
                .imageId(user.getImageId())
                .publicityDate(new Date())
                .isPickupable(request.getIsPickupable())
                .isDeliverable(request.getIsDeliverable())
                .user(user)
                .build();
    }

    public ItemResponse updateItemResponse(UpdateItemRequest request,UserResponse user){
        return ItemResponse.builder()
                .itemId(UUID.randomUUID().getLeastSignificantBits())
                .streetAddress(request.getStreetAddress())
                .postalCode(request.getPostalCode())
                .postOffice(request.getPostOffice())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .title(request.getTitle())
                .imageId(user.getImageId())
                .publicityDate(new Date())
                .isPickupable(request.getIsPickupable())
                .isDeliverable(request.getIsDeliverable())
                .user(user)
                .build();
    }


    public RentalResponse rentalResponse(RegisterRentalRequest request,UserResponse user,ItemResponse item) {
        return RentalResponse.builder()
                .rentalId(UUID.randomUUID().getLeastSignificantBits())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status("ACCEPTED")
                .user(user)
                .item(item)
                .deliveryInfo(request.getDeliveryInfo())
                .build();
    }

    public FeedbackWebPageResponse feedbackWebPageResponse(FeedbackWebPageRequest feedbackWebPageRequest,UserResponse  user){
        return FeedbackWebPageResponse.builder()
                .feedbackId(UUID.randomUUID().getLeastSignificantBits())
                .message(feedbackWebPageRequest.getMessage())
                .user(user)
                .build();
    }

}
