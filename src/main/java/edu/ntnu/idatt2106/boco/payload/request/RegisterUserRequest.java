package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest
{
    @NotBlank
    private String name;

    @NotBlank
    private Boolean isPerson;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String postOffice;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private Long imageId;
}
