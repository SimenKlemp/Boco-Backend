package edu.ntnu.idatt2106.boco.controllers;

import edu.ntnu.idatt2106.boco.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
@EnableAutoConfiguration
@CrossOrigin
class ImageController
{
    @Autowired
    ImageService imageService;

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    Resource getImage(@PathVariable long imageId)
    {
        return imageService.getImage(imageId);
    }
}