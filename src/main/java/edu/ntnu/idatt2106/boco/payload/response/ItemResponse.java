package edu.ntnu.idatt2106.boco.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ItemResponse
{
    private Long itemId;
    private String streetAddress;
    private String postalCode;
    private String postOffice;
    private float lat;
    private float lng;
    private float price;
    private String description;
    private String category;
    private String title;
    private Long imageId;
    @JsonFormat(timezone = "GMT+02:00")
    private Date publicityDate;
    private Boolean isPickupable;
    private Boolean isDeliverable;
    private UserResponse user;
}
