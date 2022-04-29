package edu.ntnu.idatt2106.boco.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long notificationId;
    private String notificationStatus;
    private boolean isPressed;
    private RentalResponse rental;
    private UserResponse user;
}
