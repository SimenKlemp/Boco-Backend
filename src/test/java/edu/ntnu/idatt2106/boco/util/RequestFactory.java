package edu.ntnu.idatt2106.boco.util;

import edu.ntnu.idatt2106.boco.payload.request.RegisterItemRequest;
import edu.ntnu.idatt2106.boco.payload.request.RegisterUserRequest;
import edu.ntnu.idatt2106.boco.payload.request.UpdateUserRequest;

import java.util.Date;

public class RequestFactory
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
}
