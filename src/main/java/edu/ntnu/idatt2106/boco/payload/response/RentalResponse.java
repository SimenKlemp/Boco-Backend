package edu.ntnu.idatt2106.boco.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RentalResponse
{
    private Long rentalId;
    @JsonFormat(timezone = "GMT+02:00")
    private Date startDate;
    @JsonFormat(timezone = "GMT+02:00")
    private Date endDate;
    private String status;
    private UserResponse user;
    private ItemResponse item;
    private Rental.DeliverInfo deliveryInfo;
    private MessageResponse lastMessage;
}
