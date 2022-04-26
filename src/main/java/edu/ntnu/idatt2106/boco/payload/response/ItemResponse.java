package edu.ntnu.idatt2106.boco.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
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
    private float price;
    private String description;
    private String category;
    private String title;
    private Long imageId;
    private Date publicityDate;
    private Boolean isPickupable;
    private Boolean isDeliverable;
    private UserResponse user;
}
