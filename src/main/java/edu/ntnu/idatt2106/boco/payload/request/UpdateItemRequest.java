package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateItemRequest
{
    private String streetAddress;
    private String postalCode;
    private String postOffice;
    private Float price;
    private String description;
    private String category;
    private String title;
    private Boolean isPickupable;
    private Boolean isDeliverable;
    private Long userId;
    private Long imageId;
}
