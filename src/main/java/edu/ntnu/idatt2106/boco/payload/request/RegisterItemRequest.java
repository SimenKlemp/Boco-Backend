package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private Long userId;

    private long imageId;
}
