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
public class ItemResponse
{
    private Long itemId;
    private String address;
    private float price;
    private String description;
    private String category;
    private String title;
    private long imageId;
    private UserResponse user;
}
