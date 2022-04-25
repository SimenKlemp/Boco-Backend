package edu.ntnu.idatt2106.boco.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse
{
    private Long userId;
    private String name;
    private boolean isPerson;
    private String streetAddress;
    private String postalCode;
    private String postOffice;
    private String email;
    private String role;
    private long imageId;
}
