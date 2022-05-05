package edu.ntnu.idatt2106.boco.payload.response;

import edu.ntnu.idatt2106.boco.models.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RentalResponse
{
    private Long rentalId;
    private Date startDate;
    private Date endDate;
    private String status;
    private UserResponse user;
    private ItemResponse item;
    private Rental.DeliverInfo deliveryInfo;
    private MessageResponse lastMessage;
}
