package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.payload.request.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
                "email",
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
                "new email",
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
}
