package edu.ntnu.idatt2106.boco.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponse
{
    private Long rentalId;
    private String message;
    private Date startDate;
    private Date endDate;
    private String status;
    private UserResponse user;
    private ItemResponse item;
    private int deliveryInfo;
}
