package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterItemRequest
{
    @NotBlank
    private String streetAddress;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String postOffice;

    @NotBlank
    private float price;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NotBlank
    private String title;

    @NotBlank
    private Boolean isPickupable;

    @NotBlank
    private Boolean isDeliverable;

    @NotBlank
    private Long userId;

    private Long imageId;
}
