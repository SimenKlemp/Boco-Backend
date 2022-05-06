package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest
{
    private String name;

    private Boolean isPerson;

    private String streetAddress;

    private String postalCode;

    private String postOffice;

    @Email
    private String email;

    private String password;

    private Long imageId;
}
