package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest
{
    @Size(max = 20)
    private String name;

    private Boolean isPerson;

    @Size(max = 20)
    private String streetAddress;

    @Size(max = 20)
    private String postalCode;

    @Size(max = 20)
    private String postOffice;

    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 120)
    private String password;

    private Long imageId;
}
