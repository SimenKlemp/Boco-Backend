package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.models.Rental;
import edu.ntnu.idatt2106.boco.payload.request.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Random;

public abstract class RequestFactory
{
    public static RegisterUserRequest getRegisterUserRequest(Long imageId)
    {
        return new RegisterUserRequest(
                "name",
                true,
                "streetAddress",
                "postalCode",
                "postOffice",
                "email" + new Random().nextInt() + "@email.com",
                "password",
                imageId
        );
    }

    public static UpdateUserRequest getUpdateUserRequest(Long imageId)
    {
        return new UpdateUserRequest(
                "new name",
                false,
                "new streetAddress",
                "new postalCode",
                "new postOffice",
                "new" + new Random().nextInt() + "@email.com",
                "new password",
                imageId
        );
    }

    public static RegisterItemRequest getRegisterItemRequest(Long userId, Long imageId)
    {
        return new RegisterItemRequest(
                "streetAddress",
                "postalCode",
                "postOffice",
                100f,
                "description",
                "category",
                "title",
                true,
                true,
                userId,
                imageId
        );
    }

    public static RegisterFeedbackWebPageRequest getRegisterFeedbackWebPageRequest(Long userId)
    {
        return new RegisterFeedbackWebPageRequest(
                "message",
                userId
        );
    }

    public static RegisterMessageRequest getRegisterMessageRequest(Long userId, Long rentalId)
    {
        return new RegisterMessageRequest(
                "text",
                userId,
                rentalId
        );
    }

    public static MultipartFile getMultipartFile()
    {
        return new MockMultipartFile("name", new byte[5]);
    }

    public static RegisterRatingRequest getRegisterRatingRequest(Long rentalId, Long userId)
    {
        return new RegisterRatingRequest(
                "feedback",
                5,
                rentalId,
                userId
        );
    }

    public static UpdateItemRequest getUpdateItemRequest(Long imageId)
    {
        return new UpdateItemRequest(
                "new streetAddress",
                "new postalCode",
                "new postOffice",
                200f,
                "new description",
                "new category",
                "new title",
                false,
                false,
                imageId
        );
    }

    public static RegisterRentalRequest getRegisterRentalRequest(Long userId, Long itemId)
    {
        return new RegisterRentalRequest(
                "message",
                new Date(),
                new Date(),
                userId,
                itemId,
                Rental.DeliverInfo.PICKUP
        );
    }

    public static SearchRequest getSearchRequest()
    {
        return new SearchRequest(
                "title",
                0,
                Integer.MAX_VALUE,
                SearchRequest.SortField.RELEVANCE,
                true,
                0f,
                Float.POSITIVE_INFINITY,
                true,
                true,
                "category"
        );
    }
}
