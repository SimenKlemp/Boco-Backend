package edu.ntnu.idatt2106.boco.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemRequest
{
    private String address;
    private Float price;
    private String description;
    private String category;
    private String title;
    private Long userId;
    private MultipartFile image;
}
