package edu.ntnu.idatt2106.boco.payload.response;

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
public class UserResponse
{
    private Long userId;
    private String name;
    private Boolean isPerson;
    private String streetAddress;
    private String postalCode;
    private String postOffice;
    private String email;
    private String role;
    private Long imageId;

}
