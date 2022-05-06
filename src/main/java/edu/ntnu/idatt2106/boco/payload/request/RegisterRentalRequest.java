package edu.ntnu.idatt2106.boco.payload.request;

import edu.ntnu.idatt2106.boco.models.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRentalRequest
{
    @NotBlank
    private String message;

    @NotBlank
    private Date startDate;

    @NotBlank
    private Date endDate;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long itemId;

    @NotBlank
    private Rental.DeliverInfo deliveryInfo;
}
